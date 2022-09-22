package st169656.server;

import javafx.util.Pair;
import st169656.model.Account;
import st169656.model.MailBox;
import st169656.server.managers.MailBoxManager;
import st169656.server.model.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Model
  {
    private static Model instance = new Model ();
    private static int clientCount = 0;
    private Log serverLog;
    private File data_dir;
    private MailBoxManager accountManager;
    private ArrayList <Pair <String, ArrayList <Integer>>> clientPorts = new ArrayList <> ();

    private Model ()
      {
        serverLog = new Log ();
        serverLog.setLog ("checking data dir...");
        data_dir = new File ("./data/");

        if (! data_dir.exists ()) serverLog.setLog ("created data directory, exit code: " + data_dir.mkdir ());
        serverLog.setLog ("done.");

        accountManager = new MailBoxManager (); // my method to know which accounts there exists

        /**
         * Demo part, in a real situation we would need to
         * take each account from another data source and init()
         * their correspondent folder.
         */
        accountManager.add (new Account ("ram@motherboard.com"));
        accountManager.add (new Account ("gpu@motherboard.com"));
        accountManager.add (new Account ("cpu@motherboard.com"));

        try
          {
            accountManager.populate ();
          }
        catch (IOException ioe)
          {
            serverLog.setLog ("I/O Exception while searching for mails");
          }
        catch (IllegalStateException ise)
          {
            serverLog.setLog (ise.getMessage ());
          }
      }

    public static Model getInstance ()
      {
        return instance;
      }

    public void add (Pair <String, ArrayList <Integer>> accountPair)
      {
        clientPorts.add (accountPair);
      }

    public void newPort (String forAccount)
      {
        Pair <String, ArrayList <Integer>> x = search (forAccount);

        if (x == null)
          {
            x = new Pair <> (forAccount, new ArrayList <> ());

            x.getValue ().add (genUniquePort ());
            clientPorts.add (x);

            serverLog.setLog ("Assigned to " + forAccount + " on port: " + x.getValue ());
          }
        else
          {
            x.getValue ().add (genUniquePort ());
          }
      }

    public ArrayList <Integer> getPorts (String forAccount)
      {
        Pair <String, ArrayList <Integer>> x = search (forAccount);
        ArrayList <Integer> targetPorts = null;

        if (! (x == null))
          targetPorts = x.getValue ();

        return targetPorts;
      }

    public Pair <String, ArrayList <Integer>> search (String forAccount)
      {
        Pair <String, ArrayList <Integer>> target = null;
        for (Pair <String, ArrayList <Integer>> a : clientPorts)
          {
            if (a.getKey ().equals (forAccount))
              {
                target = a;
                break;
              }
          }
        return target;
      }

    private Integer genUniquePort ()
      {
        int port = genPort ();

        // Not really quick, neither the best way to do it.
        while (getAllPorts ().contains (port))
          port = genPort ();

        return port;
      }

    private Integer genPort ()
      {
        Random r = new Random ();
        return (r.nextInt (65536) + 1024) % 65536;
      }

    private ArrayList <Integer> getAllPorts ()
      {
        ArrayList <Integer> ports = new ArrayList <> ();
        for (Pair <String, ArrayList <Integer>> a : clientPorts)
          ports.addAll (a.getValue ());
        ports.add (1337);
        return ports;
      }

    public MailBox getNextMailBox ()
      {
        clientCount = (clientCount + 1) % accountManager.getLength ();
        return accountManager.get (clientCount);
      }

    public Log getLog ()
      {
        return serverLog;
      }
  }
