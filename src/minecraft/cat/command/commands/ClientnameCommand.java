package cat.command.commands;

import cat.BlueZenith;
import cat.command.Command;
import cat.ui.notifications.NotificationManager;
import cat.ui.notifications.NotificationType;

@SuppressWarnings("all")
public class ClientnameCommand extends Command {

    public ClientnameCommand() {
        super("ClientName", "Rename your very first client.", "clientname <new name>", "cname", "clientname");
    }

    @Override
    public void execute(String[] args) {
        StringBuilder output = new StringBuilder();
        if(args.length < 2) {
            NotificationManager.publish("Must provide a new name!", NotificationType.ERROR, 2500);
            return;
        }

        for (String arg : args) {
            if(arg == args[0]) continue; //skip first arg

            if(args[args.length - 1] != arg) //check if there are other words after space
            arg += " ";
            output.append(arg);
        }
        BlueZenith.name = output.toString();
        NotificationManager.publish("Set client name to §7" + BlueZenith.name, NotificationType.INFO, 3000);
    }
}
