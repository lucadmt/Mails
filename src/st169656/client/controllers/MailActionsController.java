package st169656.client.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import st169656.client.ClientExecutor;
import st169656.client.Model;
import st169656.client.NewMailWindow;
import st169656.client.alerts.CheckConnectionAlert;
import st169656.client.alerts.ErrorAlert;
import st169656.client.alerts.InvalidInputAlert;
import st169656.client.dialogs.ForwardDialog;
import st169656.client.threads.ManagerTask;
import st169656.model.Operation;
import st169656.model.ServerMessage;
import st169656.model.SimpleMail;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MailActionsController implements Initializable, EventHandler <MouseEvent>
  {

    @FXML
    private ImageView actions_new;

    @FXML
    private ImageView actions_forward;

    @FXML
    private ImageView actions_reply;

    @FXML
    private ImageView actions_reply_all;

    @FXML
    private ImageView actions_delete;

    @FXML
    private ImageView actions_renew;

    private Model m = Model.getInstance ();

    public MailActionsController () {}

    @Override
    public void initialize (URL location, ResourceBundle resources)
      {
        if (Model.getInstance ().getMyAccount () == null)
          {
            actions_renew.setImage (new Image ("/st169656/client/assets/img/renew.png"));
          }

        actions_new.setOnMouseClicked (this);
        actions_forward.setOnMouseClicked (this);
        actions_reply.setOnMouseClicked (this);
        actions_reply_all.setOnMouseClicked (this);
        actions_delete.setOnMouseClicked (this);
        actions_renew.setOnMouseClicked (this);
      }

    @Override
    public void handle (MouseEvent event)
      {
        // Operations different than "new" require a selected element as parameter

        m.myAccountProperty ().addListener ((observable, oldValue, newValue) ->
        {
          if (newValue.getAccount ().getAddress () != null)
            actions_renew.setVisible (false);
          else
            actions_renew.setVisible (true);
        });

        if (m.currentProperty ().get () == null && ! (event.getSource ().equals (actions_new) || event.getSource ().equals (actions_renew)))
          {
            Alert a = new Alert (Alert.AlertType.ERROR);
            a.setTitle ("Can't execute " + event.getSource ());
            a.setHeaderText (null);
            a.setContentText ("No mail selected for action: " + event.getSource ());
            a.showAndWait ();
          }
        else
          {
            if (event.getSource ().equals (actions_renew))
              Model.getInstance ().setup ();

            if (event.getSource ().equals (actions_forward))
              new ForwardDialog (new SimpleMail (m.currentProperty ().get ()));

            if (event.getSource ().equals (actions_delete))
              {
                ServerMessage msg;
                FutureTask<ServerMessage> out = new FutureTask <> (new ManagerTask (Operation.DELETE, new SimpleMail (m.currentProperty ().get ())));
                ClientExecutor.getInstance ().add (out);

                try
                  {
                    msg = out.get ();
                    if (msg.getResult () == Operation.FAILED)
                      {
                        new InvalidInputAlert ("It seems that you don't exist. " + msg.getMessage ());
                      }
                  }
                catch (InterruptedException ie)
                  {
                    new ErrorAlert ("Thread Interrupted", "This thread was terminated prematurely");
                  }
                catch (ExecutionException ee)
                  {
                    new CheckConnectionAlert ("This thread encountered an exception while executing.");
                  }
              }

            if (event.getSource ().equals (actions_reply))
              new NewMailWindow (Operation.REPLY, m.currentProperty ().get ());

            if (event.getSource ().equals (actions_reply_all))
              new NewMailWindow (Operation.REPLY_ALL, m.currentProperty ().get ());

            if (event.getSource ().equals (actions_new))
              new NewMailWindow (Operation.SEND, null);
          }
      }
  }
