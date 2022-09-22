package st169656.client.threads;

import st169656.client.Model;
import st169656.model.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ManagerTask implements Callable <ServerMessage>
  {
    private Socket clientSocket;
    private MailOperation mop;

    public ManagerTask (Operation op, SimpleMail mail)
      {
        mop = new MailOperation (Model.getInstance ().getMyAccount (), mail, op);
      }

    @Override
    public ServerMessage call () throws Exception
      {
        ServerMessage msg = null;

        clientSocket = new Socket (ConfigManager.getInstance ().getServerAddress (), ConfigManager.getInstance ().getServerPort ());
        ObjectOutputStream out = new ObjectOutputStream (clientSocket.getOutputStream ());
        out.writeObject (mop);

        if (mop.getOperation () != Operation.LIST ||
            mop.getOperation () != Operation.GET_MAILBOX ||
            mop.getOperation () != Operation.NEW_CLIENT_PORT)
          {
            ObjectInputStream in = new ObjectInputStream (clientSocket.getInputStream ());
            msg = (ServerMessage) in.readObject ();
          }
        else
          {
            msg = new ServerMessage (Operation.SUCCESS, "All right");
          }

        clientSocket.close ();

        return msg;
      }
  }
