package st169656.client.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import st169656.client.ClientExecutor;
import st169656.client.Model;
import st169656.client.alerts.CheckConnectionAlert;
import st169656.client.alerts.ErrorAlert;
import st169656.client.alerts.InvalidInputAlert;
import st169656.client.threads.ManagerTask;
import st169656.model.Mail;
import st169656.model.Operation;
import st169656.model.ServerMessage;
import st169656.model.SimpleMail;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MailInputController implements Initializable, EventHandler <MouseEvent>
  {
    @FXML
    private TextField new_subject;

    @FXML
    private TextField new_recipients;

    @FXML
    private TextArea new_content;

    @FXML
    private ImageView new_send;

    private Model m = Model.getInstance ();
    private Stage stage;
    private Mail prev;
    private Operation op;

    public MailInputController (Stage stage, Mail previous, Operation op)
      {
        this.stage = stage;
        this.prev = previous;
        this.op = op;
      }

    @Override
    public void initialize (URL location, ResourceBundle resources)
      {
        if (op == Operation.REPLY)
          {
            new_subject.setText ("[RE]: " + prev.getSubject ());
            new_recipients.setText (prev.getSender ());
          }

        if (op == Operation.FORWARD)
          {
            new_subject.setText ("[FWD]:" + prev.getSubject ());
          }

        if (op == Operation.REPLY_ALL)
          {
            new_subject.setText ("[RE:] " + prev.getSubject ());
            new_recipients.setText (prev.getSender () + ", " + prev.getDestination ().replace (Model.getInstance ().getMyAccount ().getAddress () + ", ", ""));
          }

        new_send.setOnMouseClicked (this);
      }

    @Override
    public void handle (MouseEvent event)
      {
        SimpleMail send = new SimpleMail (0,
            Model.getInstance ().getMyAccount ().getAddress (),
            new_recipients.getText (),
            new_subject.getText (),
            new_content.getText (),
            new Date ().toString ());

        if (op == Operation.REPLY || op == Operation.REPLY_ALL)
          send.setReplyTo (prev.getId ());

        if (! send.getContent ().equals ("") && ! send.getSubject ().equals (""))
          {
            if (checkInputMails (send.getDestination ()))
              {
                FutureTask <ServerMessage> out = new FutureTask <> (new ManagerTask (op, send));
                ClientExecutor.getInstance ().add (out);
                try
                  {
                    ServerMessage msg = out.get ();
                    if (stage != null && msg.getResult () != Operation.FAILED) stage.close ();
                    else new ErrorAlert ("Error", msg.getMessage ());
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
          }
        else
          {
            new InvalidInputAlert ("One or more field were left void");
          }
      }

    public boolean checkInputMails (String mails)
      {
        boolean valid = true;
        for (String dst : mails.split (","))
          {
            // check that seems like a mail. (RFC 5322)
            if (! dst.matches ("(?:[a-z0-9!#$%&'*+/=?^_{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"))
              {
                valid = false;
                new InvalidInputAlert ("The mail address specified isn't valid");
                break;
              }
          }
        return valid;
      }
  }
