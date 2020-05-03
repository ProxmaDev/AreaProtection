package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.Language;

public class DeleteCommand extends SubCommand {

    public DeleteCommand() {
        super("delete");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            ap.sendUsage(sender);
            return;
        }
        Area area = ap.getAreaByName(args[1]);
        if (area == null) {
            sender.sendMessage(Language.getAndReplace("cant-find-area", args[1]));
            return;
        }
        ap.deleteArea(args[1]);
        sender.sendMessage(Language.getAndReplace("area-deleted", args[1]));
    }
}
