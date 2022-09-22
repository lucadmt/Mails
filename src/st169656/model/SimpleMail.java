package st169656.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class SimpleMail implements Serializable
  {
    private int id; // If 0, server will assign new ID.
    private String sender;
    private String destination;
    private String subject;
    private String content;
    private String date;

    private String forwardTo;
    private int replyTo;

    public SimpleMail (Mail m)
      {
        this.id = m.getId ();
        this.sender = m.getSender ();
        this.destination = m.getDestination ();
        this.subject = m.getSubject ();
        this.content = m.getContent ();
        this.date = m.getDate ();
      }

    public SimpleMail (int id, String sender, String destination, String subject, String content, String date)
      {
        String dst = destination.toLowerCase ()
            .replace (", ", ",")
            .replace (" ", "");
        this.id = id;
        this.sender = sender;
        this.destination = dst;
        this.subject = subject;
        this.content = content;
        this.date = date;
      }

    public static SimpleMail parseMail (File mail) throws IOException, IllegalArgumentException
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

        SimpleMail ret = new SimpleMail (
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

    public void setId (int id)
      {
        this.id = id;
      }

    public String getSender ()
      {
        return sender;
      }

    public void setSender (String sender)
      {
        this.sender = sender;
      }

    public String getDestination ()
      {
        return destination;
      }

    public void setDestination (String destination)
      {
        this.destination = destination;
      }

    public String getSubject ()
      {
        return subject;
      }

    public void setSubject (String subject)
      {
        this.subject = subject;
      }

    public String getContent ()
      {
        return content;
      }

    public void setContent (String content)
      {
        this.content = content;
      }

    public String getDate ()
      {
        return date;
      }

    public void setDate (String date)
      {
        this.date = date;
      }

    public String getForwardTo ()
      {
        return forwardTo;
      }

    public void setForwardTo (String forwardTo)
      {
        this.forwardTo = forwardTo.toLowerCase ().replace (" ", "");
      }

    public int getReplyTo ()
      {
        return replyTo;
      }

    public void setReplyTo (int replyTo)
      {
        this.replyTo = replyTo;
      }

    @Override
    public boolean equals (Object o)
      {
        if (this == o) return true;
        if (! (o instanceof SimpleMail)) return false;
        SimpleMail that = (SimpleMail) o;
        return getId () == that.getId () &&
            getReplyTo () == that.getReplyTo () &&
            Objects.equals (getSender (), that.getSender ()) &&
            Objects.equals (getDestination (), that.getDestination ()) &&
            Objects.equals (getSubject (), that.getSubject ()) &&
            Objects.equals (getContent (), that.getContent ()) &&
            Objects.equals (getDate (), that.getDate ()) &&
            Objects.equals (getForwardTo (), that.getForwardTo ());
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
