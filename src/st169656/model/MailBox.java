package st169656.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class MailBox implements Serializable
  {
    Account account;
    ArrayList <SimpleMail> inbox;

    public MailBox (Account account, ArrayList <SimpleMail> inbox)
      {
        this.account = account;
        this.inbox = inbox;
      }

    public Account getAccount ()
      {
        return account;
      }

    public ArrayList <SimpleMail> getInbox ()
      {
        return inbox;
      }

    public void setInbox (ArrayList <SimpleMail> inbox)
      {
        if (this.inbox != null)
          this.inbox.clear ();

        this.inbox.addAll (inbox);
      }

    @Override
    public boolean equals (Object o)
      {
        if (this == o) return true;
        if (! (o instanceof MailBox)) return false;
        MailBox mailBox = (MailBox) o;
        return getAccount ().equals (mailBox.getAccount ()) &&
            Objects.equals (getInbox (), mailBox.getInbox ());
      }

    @Override
    public int hashCode ()
      {
        return Objects.hash (getAccount (), getInbox ());
      }

    @Override
    public String toString ()
      {
        return "MailBox{" +
            "account=" + account +
            ", inbox=" + inbox +
            '}';
      }
  }
