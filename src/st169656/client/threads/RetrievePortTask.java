package st169656.client.threads;

import st169656.client.Model;
import st169656.model.ConfigManager;
import st169656.model.MailOperation;
import st169656.model.Operation;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class RetrievePortTask implements Callable <Integer>
  {
    @Override
    public Integer call () throws Exception
      {
        Socket client = new Socket (ConfigManager.getInstance ().getServerAddress (), ConfigManager.getInstance ().getServerPort ());
        ObjectOutputStream out = new ObjectOutputStream (client.getOutputStream ());
        out.writeObject (new MailOperation (Model.getInstance ().getMyAccount (), null, Operation.NEW_CLIENT_PORT));
        ObjectInputStream in = new ObjectInputStream (client.getInputStream ());
        return (Integer) in.readObject ();
      }
  }
