package st169656.model;

public class ConfigManager
  {
    private static ConfigManager instance = new ConfigManager ();
    private String serverAddress = "localhost";
    private int serverPort = 1337;

    private ConfigManager ()
      {
        // Other eventual config aspects should go there.
      }

    public static ConfigManager getInstance ()
      {
        return instance;
      }

    public String getServerAddress ()
      {
        return serverAddress;
      }

    public void setServerAddress (String serverAddress)
      {
        this.serverAddress = serverAddress;
      }

    public int getServerPort ()
      {
        return serverPort;
      }

    public void setServerPort (int serverPort)
      {
        this.serverPort = serverPort;
      }
  }
