package st169656.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerExecutor
  {
    private static ServerExecutor instance = new ServerExecutor ();
    private ExecutorService clientexec = Executors.newFixedThreadPool (5);

    private ServerExecutor ()
      {

      }

    public static ServerExecutor getInstance ()
      {
        return instance;
      }

    public void add (Runnable r)
      {
        clientexec.execute (r);
      }

    public void shutdown ()
      {
        clientexec.shutdown ();
      }
  }
