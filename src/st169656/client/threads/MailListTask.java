package st169656.client.threads;

import st169656.client.Model;
import st169656.model.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MailListTask implements Callable <ArrayList <Mail>>
  {
    @Override
    public ArrayList <Mail> call () throws Exception
      {
        ArrayList <SimpleMail> sout;
        ArrayList <Mail> result = new ArrayList <> ();

        Socket client = new Socket (ConfigManager.getInstance ().getServerAddress (), ConfigManager.getInstance ().getServerPort ());
        ObjectOutputStream out = new ObjectOutputStream (client.getOutputStream ());
        out.writeObject (new MailOperation (Model.getInstance ().getMyAccount (), null, Operation.LIST));
        ObjectInputStream in = new ObjectInputStream (client.getInputStream ());
        sout = (ArrayList <SimpleMail>) in.readObject ();

        for (SimpleMail elem : sout)
          result.add (new Mail (elem));

        return result;
      }
  }
