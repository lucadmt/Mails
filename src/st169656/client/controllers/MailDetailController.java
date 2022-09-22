package st169656.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import st169656.client.Model;
import st169656.model.Mail;

import java.net.URL;
import java.util.ResourceBundle;

public class MailDetailController implements Initializable
  {

    private Model m = Model.getInstance ();

    @FXML
    private Label mail_name;

    @FXML
    private Label mail_address;

    @FXML
    private Label mail_date;

    @FXML
    private Label mail_subject;

    @FXML
    private TextArea mail_content;

    public MailDetailController ()
      {
      }

    @Override
    public void initialize (URL location, ResourceBundle resources)
      {
        mail_content.setWrapText (true);
        m.currentProperty ().addListener ((obs, oldSelection, newSelection) ->
        {
          if (oldSelection != null) unbind (oldSelection);
          if (newSelection == null) reset ();
          else bind (newSelection);
        });
      }

    private void reset ()
      {
        mail_name.setText ("");
        mail_address.setText ("");
        mail_subject.setText ("");
        mail_content.setText ("");
        mail_date.setText ("");
      }

    private void unbind (Mail oldMail)
      {
        mail_name.setText ("");
        mail_address.textProperty ().unbindBidirectional (oldMail.senderProperty ());
        mail_date.textProperty ().unbindBidirectional (oldMail.dateProperty ());
        mail_subject.textProperty ().unbindBidirectional (oldMail.subjectProperty ());
        mail_content.textProperty ().unbindBidirectional (oldMail.contentProperty ());
      }

    private void bind (Mail newMail)
      {
        mail_name.setText (newMail.getSenderName ());
        mail_address.textProperty ().bindBidirectional (newMail.senderProperty ());
        mail_date.textProperty ().bindBidirectional (newMail.dateProperty ());
        mail_subject.textProperty ().bindBidirectional (newMail.subjectProperty ());
        mail_content.textProperty ().bindBidirectional (newMail.contentProperty ());
      }
  }
