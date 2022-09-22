package st169656.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Mail
  {
    private int id;
    private StringProperty sender;
    private StringProperty destination;
    private StringProperty subject;
    private StringProperty content;
    private StringProperty date;
    private String forwardTo;
    private int replyTo;

    public Mail (int id, String sender, String destination, String subject, String content, String date)
      {
        String dst = destination.toLowerCase ()
            .replace (", ", ",")
            .replace (" ", "");
        this.id = id;
        this.sender = new SimpleStringProperty (sender);
        this.destination = new SimpleStringProperty (dst);
        this.subject = new SimpleStringProperty (subject);
        this.content = new SimpleStringProperty (content);
        this.date = new SimpleStringProperty (date);
      }

    public Mail (SimpleMail sm)
      {
        this.id = sm.getId ();
        this.sender = new SimpleStringProperty (sm.getSender ());
        this.destination = new SimpleStringProperty (sm.getDestination ());
        this.subject = new SimpleStringProperty (sm.getSubject ());
        this.content = new SimpleStringProperty (sm.getContent ());
        this.date = new SimpleStringProperty (sm.getDate ());
        this.forwardTo = sm.getForwardTo ();
        this.replyTo = sm.getReplyTo ();
      }

    // Returns a new ID for this account, should be sequential.
    private synchronized static int solveId (String dst)
      {
        return lastId (dst) + 1;
      }

    private static int lastId (String dst)
      {
        File accData = new File ("./data/" + dst + "/");
        if (! accData.exists ()) return 0;
        String[] all = accData.list ();
        ArrayList <Integer> sorting = new ArrayList <> ();
        if (all != null && all.length > 0)
          {
            for (String elem : all)
              {
                try
                  {
                    sorting.add (Integer.parseInt (elem));
                  }
                catch (NumberFormatException nfe) { }
              }

            Collections.sort (sorting);

            if (sorting.size () > 0)
              return sorting.get (sorting.size () - 1);
            else return 0;
          }
        else return 0;
      }

    private synchronized static boolean exists (String dst, int id)
      {
        File data = new File ("./data/" + dst + "/");
        boolean exist = false;
        for (String el : data.list ())
          {
            try
              {
                if (Integer.parseInt (el) == id)
                  {
                    exist = true;
                    break;
                  }
              }
            catch (NumberFormatException nfe) {}
          }
        return exist;
      }

    public synchronized static void saveMail (SimpleMail mail) throws IOException
      {
        String[] dests = mail.getDestination ().split (",");
        FileWriter fw;

        for (String dst : dests)
          {
            if (mail.getId () == 0 || exists (dst, mail.getId ()))
              mail.setId (solveId (dst));

            fw = new FileWriter (new File ("./data/" + dst + "/" + mail.getId ()));
            BufferedWriter bw = new BufferedWriter (fw);
            bw.write (mail.toString ());
            bw.close ();
            fw.close ();
          }
      }

    public synchronized static boolean delete (Account account, SimpleMail obj)
      {
        File toDel = new File ("./data/" + account.getAddress () + "/" + obj.getId ());
        return toDel.delete ();
      }

    public synchronized static void forward (SimpleMail mail) throws IOException, IllegalArgumentException
      {
        if (mail == null) throw new IllegalArgumentException ("forward error: null mail passed as parameter");
        for (String acc : mail.getForwardTo ().split (","))
          {
            mail.setId (solveId (acc));
            FileWriter fw = new FileWriter (new File ("./data/" + acc + "/" + solveId (acc)));
            BufferedWriter bw = new BufferedWriter (fw);
            bw.write (mail.toString ());
            bw.close ();
            fw.close ();
          }
      }

    public static Mail parse (String mail)
      {
        if (mail == null) throw new IllegalArgumentException ("Null mail may not be passed as parameter");
        String[] data = mail.split ("\n");
        Mail ret = new Mail (
            Integer.parseInt (data[0]),
            data[1],
            data[2],
            data[3],
            data[4],
            data[5]
        );
        ret.setReplyTo (Integer.parseInt (data[6]));
        ret.setForwardTo (data[7]);
        return ret;
      }

    public static Mail parseMail (File mail) throws IOException, IllegalArgumentException
      {
        if (mail == null || ! mail.exists ()) throw new IllegalArgumentException ("Error: can't find such mail");
        ArrayList <String> mailFileContents = new ArrayList <> ();
        String line;
        FileReader reader = new FileReader (mail);
        BufferedReader br = new BufferedReader (reader);

        while ((line = br.readLine ()) != null)
          {
            mailFileContents.add (line);
          }

        Mail ret = new Mail (
            Integer.parseInt (mailFileContents.get (0)), // id
            mailFileContents.get (1),                   // sender
            mailFileContents.get (2),                   // destination(s)
            mailFileContents.get (3),                   // subject
            mailFileContents.get (4),                   // content
            mailFileContents.get (5)                    // date
        );

        ret.setReplyTo (Integer.parseInt (mailFileContents.get (6)));
        ret.setForwardTo (mailFileContents.get (7));

        reader.close ();
        br.close ();
        return ret;
      }

    public int getId ()
      {
        return id;
      }

    public String getSenderName ()
      {
        return sender.get ().substring (0, sender.get ().indexOf ('@'));
      }

    public String getSender ()
      {
        return sender.get ();
      }

    public String getDestination ()
      {
        return destination.get ();
      }

    public String getSubject ()
      {
        return subject.get ();
      }

    public String getContent ()
      {
        return content.get ();
      }

    public String getDate ()
      {
        return date.get ();
      }

    public StringProperty senderProperty ()
      {
        return sender;
      }

    public StringProperty destinationProperty ()
      {
        return destination;
      }

    public StringProperty subjectProperty ()
      {
        return subject;
      }

    public StringProperty contentProperty ()
      {
        return content;
      }

    public StringProperty dateProperty ()
      {
        return date;
      }

    public int getReplyTo () { return replyTo; }

    public void setReplyTo (int replyTo) { this.replyTo = replyTo; }

    public String getForwardTo () { return forwardTo; }

    public void setForwardTo (String forwardTo) { this.forwardTo = forwardTo; }

    @Override
    public boolean equals (Object o)
      {
        if (this == o) return true;
        if (! (o instanceof Mail)) return false;
        Mail mail = (Mail) o;
        return getId () == mail.getId () &&
            getReplyTo () == mail.getReplyTo () &&
            Objects.equals (getSender (), mail.getSender ()) &&
            Objects.equals (getDestination (), mail.getDestination ()) &&
            Objects.equals (getSubject (), mail.getSubject ()) &&
            Objects.equals (getContent (), mail.getContent ()) &&
            Objects.equals (getDate (), mail.getDate ()) &&
            Objects.equals (getForwardTo (), mail.getForwardTo ());
      }

    @Override
    public int hashCode ()
      {
        return Objects.hash (getId (), getSender (), getDestination (), getSubject (), getContent (), getDate (), getForwardTo (), getReplyTo ());
      }

    @Override
    public String toString ()
      {
        return
            getId () + "\n" +
                getSender () + "\n" +
                getDestination () + "\n" +
                getSubject () + "\n" +
                getContent () + "\n" +
                getDate () + "\n" +
                getReplyTo () + "\n" +
                getForwardTo ();
      }
  }
