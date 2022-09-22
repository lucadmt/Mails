package st169656.model;

import java.io.Serializable;

public class ServerMessage implements Serializable
  {
    Operation result;
    String message;

    public ServerMessage (Operation result, String message)
      {
        this.result = result;
        this.message = message;
      }

    public Operation getResult ()
      {
        return result;
      }

    public String getMessage ()
      {
        return message;
      }
  }
