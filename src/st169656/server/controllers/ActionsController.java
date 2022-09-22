package st169656.server.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import st169656.server.Model;
import st169656.server.Server;

import java.net.URL;
import java.util.ResourceBundle;

public class ActionsController implements Initializable
  {

    private Model m = Model.getInstance ();

    @FXML
    private Button server_restart;

    @FXML
    private Button server_stop;

    @FXML
    private Button server_clear;

    private Server server;

    public ActionsController (Server server)
      {
        this.server = server;
      }

    @Override
    public void initialize (URL location, ResourceBundle resources)
      {
        server_restart.setDisable (true);

        server_restart.setOnAction (event ->
        {
          m.getLog ().setLog ("Starting Server...");
          server = new Server (m.getLog ());
          server.start ();
          m.getLog ().setLog ("Server started.");
          server_stop.setDisable (false);
          server_restart.setDisable (true);
        });

        server_stop.setOnAction (event ->
        {
          server_restart.setDisable (false);
          server_stop.setDisable (true);
          m.getLog ().setLog ("Stopping Server...");
          server.softStop();
          m.getLog ().setLog ("Server stopped.");
        });

        server_clear.setOnAction (event ->
            m.getLog ().clearLog ());
      }
  }
