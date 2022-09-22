package st169656.client;

import st169656.model.ServerMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ClientExecutor
  {
    private static ClientExecutor instance = new ClientExecutor ();
    private ExecutorService clientexec = Executors.newFixedThreadPool (5);

    private ClientExecutor ()
      {
      }

    public static ClientExecutor getInstance ()
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
