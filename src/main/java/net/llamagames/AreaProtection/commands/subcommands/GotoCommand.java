package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.Language;

public class GotoCommand extends SubCommand {

    public GotoCommand() {
        super("goto");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            ap.sendSenderUsage(sender);
            return;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Area area = ap.getAreaByName(args[1]);
            if (area != null) {
                player.teleport(area.getPos1());
                player.sendMessage(AreaProtection.Prefix + Language.getAndReplace("area-teleported", args[1]));
            } else {
                player.sendMessage(AreaProtection.Prefix + Language.getAndReplace("cant-find-area", args[1]));
            }
        }
    }
}
