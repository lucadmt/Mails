package st169656.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import st169656.client.controllers.MailInputController;
import st169656.model.Mail;
import st169656.model.Operation;

import java.io.IOException;

public class NewMailWindow
  {
    public NewMailWindow (Operation op, Mail prev)
      {
        BorderPane root = new BorderPane ();
        FXMLLoader new_mail_loader = new FXMLLoader (getClass ().getResource ("/st169656/client/views/mail_input.fxml"));
        Stage send = null;
        try
          {
            send = new Stage ();
            send.setScene (new Scene (root));
            MailInputController mic = new MailInputController (send, prev, op);
            new_mail_loader.setController (mic);
            root.setCenter (new_mail_loader.load ());
          }
        catch (IOException e)
          {
            System.out.println ("IOE while creating send iface");
            e.printStackTrace ();
          }

        send.setResizable (false);
        send.setTitle ("Input new mail");
        send.show ();
      }
  }
