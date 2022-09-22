package st169656.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import st169656.client.Model;
import st169656.model.Mail;

import java.net.URL;
import java.util.ResourceBundle;

public class MailListController implements Initializable
  {
    private Model m = Model.getInstance ();

    @FXML
    private ListView <Mail> mail_list;

    public MailListController ()
      {
      }

    @Override
    public void initialize (URL location, ResourceBundle resources)
      {
        mail_list.setItems (m.getList ());

        mail_list.setCellFactory (lv -> new ListCell <Mail> ()
          {
            @Override
            public void updateItem (Mail mail, boolean empty)
              {
                super.updateItem (mail, empty);
                if (empty)
                  {
                    setText (null);
                  }
                else
                  {
                    setText (mail.getSubject ());
                  }
              }
          });

        mail_list.getSelectionModel ().selectedItemProperty ()
            .addListener ((obs, oldSelection, newSelection) -> m.setCurrent (newSelection));

        m.currentProperty ().addListener ((obs, oldPerson, newPerson) ->
        {
          if (newPerson.equals (null)) mail_list.getSelectionModel ().clearSelection ();
          else mail_list.getSelectionModel ().select (newPerson);
        });
      }
  }
