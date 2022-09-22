package st169656.client.dialogs;

import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import st169656.client.ClientExecutor;
import st169656.client.alerts.CheckConnectionAlert;
import st169656.client.alerts.ErrorAlert;
import st169656.client.alerts.InvalidInputAlert;
import st169656.client.threads.ManagerTask;
import st169656.model.Operation;
import st169656.model.ServerMessage;
import st169656.model.SimpleMail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ForwardDialog extends TextInputDialog
  {
    private final SimpleMail mMail;

    public ForwardDialog (SimpleMail mail)
      {
        super ();
        mMail = mail;
        this.setTitle ("Forward to");
        this.setContentText ("To whom?");
        this.show();
        this.getDialogPane ().lookupButton (ButtonType.OK).addEventFilter (ActionEvent.ACTION, event -> isInvalid (this.getEditor ().getText (), event));
      }

    private void isInvalid (String text, ActionEvent event)
      {
        mMail.setForwardTo (text);
        FutureTask <ServerMessage> out = new FutureTask <> (new ManagerTask (Operation.FORWARD, mMail));
        ClientExecutor.getInstance ().add (out);

        try
          {
            ServerMessage msg = out.get ();
            if (msg.getResult () == Operation.FAILED)
              {
                new InvalidInputAlert (msg.getMessage ());
                event.consume ();
              }
            else this.close ();
          }
        catch (InterruptedException ie)
          {
            new ErrorAlert ("Thread Interrupted", "This thread was terminated prematurely");
          }
        catch (ExecutionException ee)
          {
            new CheckConnectionAlert ("This thread encountered an exception while executing.");
            ee.printStackTrace ();
          }
      }
  }
