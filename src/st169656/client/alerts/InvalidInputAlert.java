package st169656.client.alerts;

public class InvalidInputAlert extends ErrorAlert
  {
    public InvalidInputAlert (String details)
      {
        super ("Invalid Input", details + ". Check your input data and try again.");
      }
  }
