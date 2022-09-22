package st169656.server.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import st169656.server.Model;
import st169656.server.managers.ConsoleInputManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LogController implements Initializable
  {

    @FXML
    private TextArea server_log;

    @FXML
    private TextField server_input;

    @FXML
    private Button server_send;

    private Model m = Model.getInstance ();

    private ConsoleInputManager inputManager = new ConsoleInputManager ();

    public LogController ()
      {

      }

    @Override
    public void initialize (URL location, ResourceBundle resources)
      {
        server_log.setWrapText (true);
        server_log.textProperty ().bind (m.getLog ().logProperty ());

        m.getLog ().logProperty ().addListener ((observable, oldValue, newValue) ->
        {
          if (newValue.length () > 500)
            {
              server_log.selectPositionCaret (server_log.getLength ()-1);
              server_log.deselect ();
            }
        });


        server_send.setOnAction (event ->
        {
          String command = server_input.getText ();
          inputManager.parse (command);
          server_input.setText ("");
          m.getLog ().setLog ("executing: " + command);
        });
      }
  }
