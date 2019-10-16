package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;

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
            sender.sendMessage(AreaProtection.Prefix + "Information about " + args[1] + ":");
            sender.sendMessage(AreaProtection.Prefix + "----------------------------------");
            sender.sendMessage(AreaProtection.Prefix + "World: " + area.getWorld().getName());
            sender.sendMessage(AreaProtection.Prefix + "1. Position: X: " + area.getPos1().x + ", Y: " + area.getPos1().y + ", Z: " + area.getPos1().z);
            sender.sendMessage(AreaProtection.Prefix + "2. Position: X: " + area.getPos2().x + ", Y: " + area.getPos2().y + ", Z: " + area.getPos2().z);
            sender.sendMessage(AreaProtection.Prefix + "Break: " + area.isBreakAllowed());
            sender.sendMessage(AreaProtection.Prefix + "Place: " + area.isPlaceAllowed());
            sender.sendMessage(AreaProtection.Prefix + "Interact: " + area.isInteractAllowed());
            sender.sendMessage(AreaProtection.Prefix + "PvP: " + area.isPvpAllowed());
            sender.sendMessage(AreaProtection.Prefix + "God: " + area.isGod());
            sender.sendMessage(AreaProtection.Prefix + "Mob Spawning: " + area.isMobSpawnAllowed());
        } else {
            sender.sendMessage(AreaProtection.Prefix + "§cCouldn't find area with name " + args[1]);
        }
    }
}
