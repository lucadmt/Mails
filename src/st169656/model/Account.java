package st169656.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Account implements Serializable
  {
    private String address;
    private String dataDir = "./data/";
    private boolean isActive = false;

    public Account (String mail)
      {
        this.address = mail;
        this.dataDir += mail + "/";
      }

    public static ArrayList <SimpleMail> getMails (String address) throws NullPointerException, IOException
      {
        ArrayList <SimpleMail> inbox = new ArrayList <> ();
        for (File mail : (new File ("./data/" + address + "/")).listFiles ())
          {
            try
              {
                Integer.parseInt (mail.getName ());
                inbox.add (SimpleMail.parseMail (mail));
              }
            catch (NumberFormatException nfe)
              {
                // File name is not a number, that's okay, since we save mails as file numbers.
              }
          }
        return inbox;
      }

    public boolean exists()
      {
        return new File("./data/"+address+"/").exists ();
      }

    public String getAddress ()
      {
        return address;
      }

    public boolean isActive ()
      {
        return isActive;
      }

    public void setActive (boolean active)
      {
        isActive = active;
      }

    @Override
    public boolean equals (Object o)
      {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        Account account = (Account) o;
        return Objects.equals (getAddress (), account.getAddress ());
      }

    @Override
    public int hashCode ()
      {
        return Objects.hash (getAddress ());
      }

    @Override
    public String toString ()
      {
        return "Account\n\t{" +
            "\n\t\taddress='" + address + '\'' +
            ",\n\t\tdataDir='" + dataDir + '\'' +
            "\n\t}";
      }
  }
