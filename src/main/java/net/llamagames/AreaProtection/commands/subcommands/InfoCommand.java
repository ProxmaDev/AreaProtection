package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.Language;

public class InfoCommand extends SubCommand {

    public InfoCommand() {
        super("name");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            ap.sendSenderUsage(sender);
            return;
        }
        Area area = ap.getAreaByName(args[1]);
        if(area != null) {
            sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("info-about", args[1]));
            sender.sendMessage(AreaProtection.Prefix + Language.getMessage("info-line"));
            sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("info-world", area.getWorld().getName()));
            sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("info-pos1", area.getPos1().x, area.getPos1().y, area.getPos1().z));
            sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("info-pos2", area.getPos2().x, area.getPos2().y, area.getPos2().z));
            sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("info-break", area.isBreakAllowed()));
            sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("info-place", area.isPlaceAllowed()));
            sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("info-interact", area.isInteractAllowed()));
            sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("info-pvp", area.isPvpAllowed()));
            sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("info-god", area.isGod()));
            sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("info-mobspawning", area.isMobSpawnAllowed()));
        } else {
            sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("cant-find-area", args[1]));
        }
    }
}
