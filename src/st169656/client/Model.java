package st169656.client;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import st169656.client.alerts.CheckConnectionAlert;
import st169656.client.threads.MailListUpdaterServer;
import st169656.client.threads.RetrieveAccountTask;
import st169656.client.threads.RetrievePortTask;
import st169656.model.Account;
import st169656.model.Mail;
import st169656.model.MailBox;
import st169656.model.SimpleMail;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Model
  {
    private static Model instance = new Model ();

    private ObservableList <Mail> mails;
    private MailListUpdaterServer mlu = null;
    private int port;

    private ObjectProperty <MailBox> myMailBox = new SimpleObjectProperty <> ();
    private ObjectProperty <Mail> current = new SimpleObjectProperty <> ();

    private Model ()
      {
        mails = FXCollections.observableArrayList ();
        myMailBox.setValue (new MailBox (null, null));
      }

    public static Model getInstance ()
      {
        return instance;
      }

    public void add (Mail e)
      {
        mails.add (e);
      }

    public ObjectProperty <Mail> currentProperty ()
      {
        return current;
      }

    public ObservableList <Mail> getList ()
      {
        return mails;
      }

    public Account getMyAccount ()
      {
        return myMailBox.get ().getAccount ();
      }

    public ObjectProperty <MailBox> myAccountProperty ()
      {
        return myMailBox;
      }

    public void setCurrent (Mail current)
      {
        this.current.set (current);
      }

    public int getPort ()
      {
        return port;
      }

    void setPort (Integer port)
      {
        this.port = port;
      }

    public void setup ()
      {
        try
          {
            FutureTask <MailBox> retrieveAccount = new FutureTask <> (new RetrieveAccountTask ());
            ClientExecutor.getInstance ().add (retrieveAccount);
            myMailBox.setValue (retrieveAccount.get ());
            setMails (myMailBox.getValue ().getInbox ());

            // Probably not the best way, myAccount could still be null at this point.
            FutureTask <Integer> retrievePort = new FutureTask <> (new RetrievePortTask ());
            ClientExecutor.getInstance ().add (retrievePort);
            port = retrievePort.get ();

            mlu = new MailListUpdaterServer ();
          }
        catch (ExecutionException ee)
          {
            new CheckConnectionAlert ("Can't get account info");
          }
        catch (InterruptedException ie)
          {
            ie.printStackTrace ();
          }
      }

    public MailListUpdaterServer getUpdater ()
      {
        return mlu;
      }

    public void setMails (ArrayList <SimpleMail> mails)
      {
        this.mails.clear ();

        for (SimpleMail m : mails)
          this.mails.add (new Mail (m)); // avoids memory leak
      }
  }
