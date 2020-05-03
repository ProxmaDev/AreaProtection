package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.AreaFlag;
import net.llamagames.AreaProtection.utils.Language;

import java.util.HashMap;

public class InfoCommand extends SubCommand {

    public InfoCommand() {
        super("name");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            ap.sendUsage(sender);
            return;
        }
        Area area = ap.getAreaByName(args[1]);
        if(area != null) {
            sender.sendMessage( Language.getAndReplace("info-about", args[1]));
            sender.sendMessage(Language.get("info-line"));
            sender.sendMessage(Language.getAndReplace("info-world", area.getWorld()));
            sender.sendMessage(Language.getAndReplace("info-pos1", area.getPos1().x, area.getPos1().y, area.getPos1().z));
            sender.sendMessage(Language.getAndReplace("info-pos2", area.getPos2().x, area.getPos2().y, area.getPos2().z));
            sender.sendMessage(Language.getAndReplace("info-flags", formatFlags(area.getFlags())));
        } else {
            sender.sendMessage(Language.getAndReplace("cant-find-area", args[1]));
        }
    }

    public String formatFlags(HashMap<String, AreaFlag> flags) {
        StringBuilder sb = new StringBuilder();

        flags.values().forEach((areaFlag) -> {
            if (areaFlag.allowed) {
                sb.append("§f").append(areaFlag.name).append(": §aenabled§r, ");
            } else {
                sb.append("§f").append(areaFlag.name).append(": §cdisabled§r, ");
            }
        });

        return sb.toString().substring(0, sb.toString().length() - 2);
    }
}
