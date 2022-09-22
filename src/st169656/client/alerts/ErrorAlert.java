package st169656.client.alerts;

import javafx.scene.control.Alert;

public class ErrorAlert extends Alert
  {
    public ErrorAlert (String title, String details)
      {
        super (AlertType.ERROR);
        this.setTitle (title);
        this.setHeaderText (null);
        this.setContentText (details);
        this.showAndWait ();
      }
  }
