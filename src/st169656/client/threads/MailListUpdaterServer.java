package st169656.client.threads;

import javafx.application.Platform;
import st169656.client.Model;
import st169656.client.alerts.NewMessageAlert;
import st169656.model.SimpleMail;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class MailListUpdaterServer extends Thread
  {
    private static boolean runtoggle = true;
    ServerSocket sock = null;
    private ArrayList <SimpleMail> updatedList = null;

    public MailListUpdaterServer ()
      {
        super ("MailListUpdaterServer" + Model.getInstance ().getMyAccount ().getAddress ());
        // Close socket on jvm exit
        Runtime.getRuntime ().addShutdownHook (new Thread (this::softStop));
        start ();
      }

    @Override
    public void run ()
      {
        try
          {
            sock = new ServerSocket (Model.getInstance ().getPort ());

            while (runtoggle)
              {

                Socket client = sock.accept ();
                updatedList = (ArrayList <SimpleMail>) new ObjectInputStream (client.getInputStream ()).readObject ();
                Platform.runLater (() ->
                {
                  if (updatedList.size () > Model.getInstance ().getList ().size ())
                    new NewMessageAlert ();
                  Model.getInstance ().setMails (updatedList);
                });
              }
          }
        catch (SocketException se)
          {
            System.out.println ("ServerSocket closed successfully");
          }
        catch (IOException ioe)
          {
            System.out.println ("IOE while socket management");
            ioe.printStackTrace ();
          }
        catch (NullPointerException npe)
          {
            System.out.println ("NPE while socket management");
            npe.printStackTrace ();
          }
        catch (ClassNotFoundException cnfe)
          {
            System.out.println ("CNFE while socket management");
            cnfe.printStackTrace ();
          }
      }


    public void softStop ()
      {
        runtoggle = false;
        close ();
      }

    private void close ()
      {
        try
          {
            sock.close ();
          }
        catch (IOException e)
          {
            System.out.println ("ServerSocket closed correctly");
            e.printStackTrace ();
          }
      }
  }
