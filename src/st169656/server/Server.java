package st169656.server;

import st169656.model.ConfigManager;
import st169656.server.model.Log;
import st169656.server.threads.ManagerTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server extends Thread
  {
    private boolean runToggle = true;
    private Log serverlog;
    private Socket incomingConnection;
    private ServerExecutor connectionPool;
    private ScheduledExecutorService exec;
    private ServerSocket srv = null;

    public Server (Log l)
      {
        setDaemon (true);
        serverlog = l;
        connectionPool = ServerExecutor.getInstance ();
        exec = Executors.newScheduledThreadPool (0);

        // Save log every minute
        exec.scheduleAtFixedRate (() ->
        {
          if (runToggle)
            {
              serverlog.setLog ("[Service]: Saving log...");
              serverlog.saveLog ();
              serverlog.setLog ("[Service]: Log saved.");
            }
        }, 0, 1, TimeUnit.MINUTES);
      }

    @Override
    public void run ()
      {
        try
          {
            serverlog.setLog ("Opening connection on port: 1337");
            srv = new ServerSocket (ConfigManager.getInstance ().getServerPort ());
            serverlog.setLog ("opened.");

            while (runToggle)
              {
                incomingConnection = srv.accept ();
                serverlog.setLog ("Accepted new Connection from client: " + incomingConnection);
                connectionPool.add (new ManagerTask (incomingConnection, serverlog));
              }
          }
        catch (SocketException se)
          {
            serverlog.setLog ("ServerSocket closed successfully");
          }
        catch (IOException ioe)
          {
            serverlog.setLog ("I/O Exception while opening serversocket or receiving connection.");
            serverlog.setLog (ioe.getMessage ());
          }
      }

    /*
     * Halts serversocket in a certain moment, it needs to be here, so
     * if the serversocket is locked onto srv.accept(); Closing it will result into a
     * SocketException, but will unlock it.
     */
    private void close ()
      {
        try
          {
            srv.close ();
            System.out.println ("The server is shut down!");
          }
        catch (IOException e)
          {
            serverlog.setLog ("I/O Exception on closing");
            serverlog.setLog (e.getMessage ());
          }
        catch (NullPointerException npe)
          {
            serverlog.setLog ("NullPointerException, probably srvsocket isn't available");
            serverlog.setLog (npe.getMessage ());
          }
      }

    public void softStop ()
      {
        runToggle = false;
        close ();
      }
  }
