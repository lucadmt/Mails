package st169656.client.alerts;

public class CheckConnectionAlert extends ErrorAlert
  {
    public CheckConnectionAlert (String details)
      {
        super ("Connection error", details + ". Check connection/server availability, then try again.");
      }
  }
