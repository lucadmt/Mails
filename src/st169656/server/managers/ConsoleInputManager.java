package st169656.server.managers;

import st169656.server.Model;
import st169656.server.Server;

public class ConsoleInputManager
  {

    private static final String flood = "flooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\n" +
        "flooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\n" +
        "flooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\n" +
        "flooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\n" +
        "flooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\n" +
        "flooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\n" +
        "flooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\n" +
        "flooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\n" +
        "flooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\nflooding...\n";

    public ConsoleInputManager ()
      {}

    public void parse (String command)
      {
        String [] cmds = command.split ("/");
        int times = 0;
        if (cmds.length > 1)
          times = Integer.parseInt (cmds[1]);
        switch (cmds[0])
          {
            case "floodLog":
              for (int i = 0; i <= times; i++)
                Model.getInstance ().getLog ().setLog (flood);
              break;
          }
      }
  }
