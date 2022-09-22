package st169656.client.alerts;

import javafx.scene.control.Alert;

public class NewMessageAlert extends Alert
  {
    public NewMessageAlert ()
      {
        super (AlertType.INFORMATION);
        this.setTitle ("New Message");
        this.setHeaderText ("New Message");
        this.setContentText ("You have a new message");
        this.showAndWait ();
      }
  }
