package st169656.model;

import java.io.Serializable;
import java.util.Objects;

public class MailOperation implements Serializable
  {
    private Account account;
    private SimpleMail mail;
    private Operation operation;

    public MailOperation (Account account, SimpleMail obj, Operation op)
      {
        this.account = account;
        mail = obj;
        operation = op;
      }

    public Account getAccount ()
      {
        return account;
      }

    public SimpleMail getMail ()
      {
        return mail;
      }

    public Operation getOperation ()
      {
        return operation;
      }

    @Override
    public boolean equals (Object o)
      {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        MailOperation that = (MailOperation) o;
        return Objects.equals (getAccount (), that.getAccount ()) &&
            Objects.equals (getMail (), that.getMail ()) &&
            getOperation () == that.getOperation ();
      }

    @Override
    public int hashCode ()
      {
        return Objects.hash (getAccount (), getMail (), getOperation ());
      }

    @Override
    public String toString ()
      {
        return "MailOperation\n\t{" +
            "\n\t\taccount=" + account +
            ",\n\t\tmail=" + mail +
            ",\n\t\toperation=" + operation +
            "\n\t}";
      }
  }
