package st169656.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import st169656.server.controllers.ActionsController;

public class ServerConsole extends Application
  {
    @Override
    public void start (Stage primaryStage) throws Exception
      {
        // Start server
        Server server = new Server (Model.getInstance ().getLog ());
        server.start ();

        // Close on jvm exit
        Runtime.getRuntime ().addShutdownHook (new Thread (server::softStop));

        BorderPane root = new BorderPane ();

        FXMLLoader logPaneLoader = new FXMLLoader (getClass ().getResource ("/st169656/server/views/log_pane.fxml"));
        root.setLeft (logPaneLoader.load ());

        FXMLLoader actionsPaneLoader = new FXMLLoader (getClass ().getResource ("/st169656/server/views/actions_pane.fxml"));
        actionsPaneLoader.setController (new ActionsController (server));
        root.setCenter (actionsPaneLoader.load ());

        Scene s = new Scene (root, 600, 400);
        primaryStage.setScene (s);
        primaryStage.setResizable (false);
        primaryStage.setTitle ("Server Control Panel");
        //primaryStage.setResizable (false);
        primaryStage.show ();
      }
  }
