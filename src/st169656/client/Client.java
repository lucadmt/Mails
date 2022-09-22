package st169656.client;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application
  {
    public static void main (String[] args)
      {
        launch (args);
      }

    @Override
    public void start (Stage primaryStage) throws Exception
      {
        Model.getInstance ().setup ();

        BorderPane root = new BorderPane ();

        FXMLLoader mail_list = new FXMLLoader (getClass ().getResource ("/st169656/client/views/mail_list.fxml"));
        root.setLeft (mail_list.load ());

        FXMLLoader mail_details = new FXMLLoader (getClass ().getResource ("/st169656/client/views/mail_detail.fxml"));
        root.setCenter (mail_details.load ());

        FXMLLoader mail_actions = new FXMLLoader (getClass ().getResource ("/st169656/client/views/mail_actions.fxml"));
        root.setRight (mail_actions.load ());

        Scene s = new Scene (root, 700, 400);

        primaryStage.setOnCloseRequest (value ->
        {
          ClientExecutor.getInstance ().shutdown ();
          Model.getInstance ().getUpdater().softStop();
        });

        primaryStage.setScene (s);
        primaryStage.setResizable (false);
        if (Model.getInstance ().getMyAccount () != null)
          primaryStage.titleProperty ().bind (new SimpleStringProperty (Model.getInstance ().getMyAccount ().getAddress ()));
        else
          primaryStage.setTitle ("MailBox");
        primaryStage.show ();
      }
  }
