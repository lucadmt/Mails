package st169656.server.model;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.time.LocalDateTime;

public class Log
  {
    private static String LOGFILE = "./data/Log.log";
    private StringProperty log = new SimpleStringProperty ();
    private String temp_server_log = "";
    private File main_log;
    private int save_attempt = 0;

    public Log ()
      {
        main_log = new File (LOGFILE);
        if (! main_log.exists ())
          {
            try
              {
                updateLog ("Creating logfile...");
                updateLog ("logfile generated with return code: " + main_log.createNewFile ());
              }
            catch (IOException ioe)
              {
                System.out.println ("CRITICAL I/O ERROR WHILE SAVING FILE, I CAN'T RECOVER");
              }
          }
      }

    public synchronized void saveLog ()
      {
        save_attempt += 1;
        PrintWriter printWriter = null;
        try
          {
            FileWriter fw = new FileWriter (LOGFILE, true);
            BufferedWriter bw = new BufferedWriter (fw);
            printWriter = new PrintWriter (bw);
            printWriter.append (temp_server_log);
          }
        catch (FileNotFoundException fnfe)
          {
            File f = new File (LOGFILE);
            try
              {
                updateLog ("LogFile not found, attempting recover...");
                f.createNewFile ();
                saveLog ();
              }
            catch (IOException e)
              {
                if (save_attempt < 3)
                  {
                    updateLog ("I/O ERROR WHILE SAVING LOG, I CAN'T RECOVER");
                    updateLog ("A BACKUP LOG WITH THIS CONTENTS WILL BE SAVED IN /data/backup.log");
                    System.out.println ("I/O ERROR WHILE SAVING LOG, I CAN'T RECOVER");
                    System.out.println ("A BACKUP LOG WITH THIS CONTENTS WILL BE SAVED IN /data/backup.log");
                    LOGFILE = "./data/backup.log";
                    saveLog ();
                    LOGFILE = "./data/Log.log";
                  }
                else
                  {
                    updateLog ("CRITICAL I/O ERROR, CAN'T SAVE LOG");
                    System.out.println ("CRITICAL I/O ERROR, CAN'T SAVE LOG");
                  }
              }
          }
        catch (IOException e)
          {
            System.out.println ("CRITICAL I/O ERROR WHILE SAVING FILE, I CAN'T RECOVER");
          }
        finally
          {
            if (printWriter != null) printWriter.close ();
          }
        save_attempt = 0;
      }

    public void clearLog ()
      {
        saveLog ();
        temp_server_log = "";
        this.log.set ("");
      }

    private void updateLog (String s)
      {
        temp_server_log += getTime () + " " + s + "\n";
      }

    public StringProperty logProperty ()
      {
        return log;
      }

    public String getLog ()
      {
        return log.get ();
      }

    public synchronized void setLog (String log)
      {
        Platform.runLater (() ->
          {
            updateLog (log);
            this.log.set (temp_server_log);
          });
      }

    private String getTime ()
      {
        LocalDateTime now = LocalDateTime.now ();
        return now.getHour () + ":" + now.getMinute () + ":" + now.getSecond ();
      }
  }
