package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;

public class ListCommand extends SubCommand {

    public ListCommand() {
        super("list");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (AreaProtection.areas.size() == 0) {
            sender.sendMessage(AreaProtection.Prefix + "There are no areas to list.");
        } else {
            StringBuilder list = new StringBuilder();
            for (Area area: AreaProtection.areas) {
                list.append(area.getName()).append(", ");
            }
            sender.sendMessage(AreaProtection.Prefix + "A list of all areas: " + list.toString());
        }
    }
}
