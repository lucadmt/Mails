package st169656.server.threads;

import st169656.model.Account;
import st169656.model.ConfigManager;
import st169656.server.Model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SendMailListTask implements Runnable
  {
    private String accounts;

    public SendMailListTask (String acc)
      {
        accounts = acc;
      }

    @Override
    public void run ()
      {
        String[] dests = accounts.toLowerCase ()
            .replace (", ", ",")
            .replace (" ", "")
            .split (",");

        for (String dest : dests)
          {
            for (int port : Model.getInstance ().getPorts (dest))
              {
                try
                  {
                    Model.getInstance ().getLog ().setLog ("Updating list for account: " + dest + ", connecting on port: " + port);
                    Socket client = new Socket (ConfigManager.getInstance ().getServerAddress (), port);
                    new ObjectOutputStream (client.getOutputStream ()).writeObject (Account.getMails (dest));
                    client.close ();
                  }
                catch (IOException ioe)
                  {
                    ioe.printStackTrace ();
                  }
                catch (NullPointerException npe)
                  {

                  }
              }
          }
      }
  }
