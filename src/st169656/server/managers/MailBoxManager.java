package st169656.server.managers;

import st169656.model.Account;
import st169656.model.MailBox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MailBoxManager
  {
    private ArrayList <MailBox> mailBoxes;
    private String mainDir = "./data/";

    public MailBoxManager ()
      {
        mailBoxes = new ArrayList <> ();
      }

    public void populate () throws IOException
      {
        for (Account account : allAccounts ())
          {
            if (new File (mainDir + account.getAddress () + "/").isDirectory ())
              {
                int idx = mailBoxes.indexOf (new MailBox (account, new ArrayList <> ()));
                mailBoxes.get (idx).setInbox (Account.getMails (account.getAddress ()));
              }
          }
      }

    public MailBox get (int i)
      {
        MailBox returning = mailBoxes.get (i);

        try
          {
            returning.setInbox (Account.getMails (returning.getAccount ().getAddress ()));
          }
        catch (IOException e)
          {
            System.out.println ("I/O Exception while getting updated mail list.");
            e.printStackTrace ();
          }

        // For extensibility purposes
        if (! returning.getAccount ().isActive ())
          returning.getAccount ().setActive (true);

        return returning;
      }

    public ArrayList <Account> allAccounts ()
      {
        ArrayList <Account> ret = new ArrayList <> ();

        for (MailBox item : mailBoxes)
          ret.add (item.getAccount ());

        return ret;
      }

    public int getLength ()
      {
        return mailBoxes.size ();
      }

    /*
     * Expects an unexisting account
     * if passed one exists as a directory throws an IllegalArgumentException
     * else, it creates the data directory, therefore its list is void.
     */
    public void add (Account account)
      {
        File newAccount = new File (mainDir + account.getAddress ());

        if (! newAccount.exists ())
          newAccount.mkdir ();

        mailBoxes.add (new MailBox (account, new ArrayList <> ()));
      }
  }
