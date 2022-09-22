package st169656.client.threads;

import st169656.model.ConfigManager;
import st169656.model.MailBox;
import st169656.model.MailOperation;
import st169656.model.Operation;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class RetrieveAccountTask implements Callable <MailBox>
  {
    @Override
    public MailBox call () throws Exception
      {
        Socket client = new Socket (ConfigManager.getInstance ().getServerAddress (), ConfigManager.getInstance ().getServerPort ());
        ObjectOutputStream out = new ObjectOutputStream (client.getOutputStream ());
        out.writeObject (new MailOperation (null, null, Operation.GET_MAILBOX));
        ObjectInputStream in = new ObjectInputStream (client.getInputStream ());
        return (MailBox) in.readObject ();
      }
  }
