package st169656.server.threads;

import st169656.model.*;
import st169656.server.Model;
import st169656.server.ServerExecutor;
import st169656.server.model.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

public class ManagerTask implements Runnable
  {
    private Log managerLog;
    private Socket incoming;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ManagerTask (Socket socket, Log l)
      {
        incoming = socket;
        managerLog = l;
        try
          {
            in = new ObjectInputStream (socket.getInputStream ());
            out = new ObjectOutputStream (socket.getOutputStream ());
          }
        catch (IOException ioe)
          {
            l.setLog ("I/O Exception while getting streams");
            ioe.printStackTrace ();
          }
      }

    @Override
    public void run ()
      {
        MailOperation mop = null;
        try
          {
            mop = (MailOperation) in.readObject ();
            managerLog.setLog ("Managing request: " + mop);

            if (mop.getOperation () != Operation.NEW_CLIENT_PORT &&
                mop.getOperation () != Operation.GET_MAILBOX &&
                mop.getOperation () != Operation.LIST)
              {
                if (checkCorrect (mop))
                  {
                    out.writeObject (new ServerMessage (Operation.SUCCESS, "Operation will soon be executed"));
                    switch (mop.getOperation ())
                      {
                        case SEND:
                          Mail.saveMail (mop.getMail ());
                          ServerExecutor.getInstance ().add (new SendMailListTask (mop.getMail ().getDestination ()));
                          break;
                        case FORWARD:
                          Mail.forward (mop.getMail ());
                          ServerExecutor.getInstance ().add (new SendMailListTask (mop.getMail ().getForwardTo ()));
                          break;
                        case REPLY:
                        case REPLY_ALL:
                          Mail.saveMail (mop.getMail ());
                          ServerExecutor.getInstance ().add (new SendMailListTask (mop.getMail ().getDestination ()));
                          break;
                        case DELETE:
                          Mail.delete (mop.getAccount (), mop.getMail ());
                          ServerExecutor.getInstance ().add (new SendMailListTask (mop.getAccount ().getAddress ()));
                          break;
                      }
                  }
                else out.writeObject (new ServerMessage (Operation.FAILED, "Incorrect recipients for operation"));
              }
            else
              {
                switch (mop.getOperation ())
                  {
                    case NEW_CLIENT_PORT:
                      Model.getInstance ().newPort (mop.getAccount ().getAddress ());
                      ArrayList <Integer> ports = Model.getInstance ().getPorts (mop.getAccount ().getAddress ());
                      managerLog.setLog ("New port for client: " + mop.getAccount ().getAddress () + " is: " + ports.get (ports.size () - 1));
                      out.writeObject (ports.get (ports.size () - 1));
                      break;
                    case GET_MAILBOX:
                      MailBox current = Model.getInstance ().getNextMailBox ();
                      out.writeObject (current);
                      break;
                    case LIST:
                      out.writeObject (Account.getMails (mop.getAccount ().getAddress ()));
                      break;
                  }
              }
          }
        catch (ConnectException ce)
          {
            managerLog.setLog ("Cannot connect to client! " + mop.getAccount ());
          }
        catch (IOException ioe)
          {
            managerLog.setLog ("I/O error while managing request: " + mop);
            System.out.println ("I/O error while managing request: " + mop);
            ioe.printStackTrace ();
          }
        catch (ClassNotFoundException cnfe)
          {
            managerLog.setLog ("Class Not Found for object: " + mop);
            System.out.println ("Class Not Found for object: " + mop);
            cnfe.printStackTrace ();
          }
        catch (NullPointerException npe)
          {
            managerLog.setLog ("While managing request: " + mop);
            System.out.println ("While managing request: " + mop);
            npe.printStackTrace ();
          }
        finally
          {
            try
              {
                managerLog.setLog ("Closing connection with client: " + incoming);
                incoming.close ();
              }
            catch (IOException ioe)
              {
                System.out.println ("I/O Exception while closing connection with: " + incoming);
                managerLog.setLog ("I/O Exception while closing connection with: " + incoming);
              }
          }
      }

    private boolean checkCorrect (MailOperation mop)
      {
        switch (mop.getOperation ())
          {
            case GET_MAILBOX:
            case LIST:
            case NEW_CLIENT_PORT:
            case SUCCESS:
            case FAILED:
              return true;
            case SEND:
            case REPLY:
            case REPLY_ALL:
              return checkRecipients (mop.getMail ().getDestination ());
            case FORWARD:
              return checkRecipients (mop.getMail ().getForwardTo ());
            case DELETE:
              return checkRecipients (mop.getAccount ().getAddress ());
            default:
              return false;
          }
      }

    private boolean checkRecipients (String whotocheck)
      {
        if (whotocheck == null) return false;
        boolean existing = true;
        for (String rcpt : whotocheck.split (","))
          {
            if (! new Account (rcpt).exists ())
              {
                existing = false;
                break;
              }
          }
        return existing;
      }

  }
