package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.AreaManager;
import net.llamagames.AreaProtection.utils.FormManager;
import net.llamagames.AreaProtection.utils.Language;

public class FlagCommand extends SubCommand {

    public FlagCommand() {
        super("flag");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.isPlayer()) {
            if (args.length >= 1) {
                Area area = ap.getAreaByName(args[1]);
                if (area != null) {
                    FormManager.sendFlagForm((Player) sender, area);
                } else {
                    sender.sendMessage(Language.getAndReplace("cant-find-area", args[1]));
                }
            } else {
                ap.sendUsage(sender);
            }
        }
    }
}
