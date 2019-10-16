package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.AreaManager;

public class FlagCommand extends SubCommand {

    public FlagCommand() {
        super("flag");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("flag")) {
                Area area = ap.getAreaByName(args[1]);
                if (area != null) {
                    String flag = args[2];
                    String bool = args[3];
                    boolean trueFalse;
                    if (bool.equalsIgnoreCase("true")) {
                        trueFalse = true;
                    } else if (bool.equalsIgnoreCase("false")) {
                        trueFalse = false;
                    } else {
                        sender.sendMessage(AreaProtection.Prefix + "Flag value needs to be true or false.");
                        return;
                    }
                    if (flag.equalsIgnoreCase("break")) {
                        AreaManager.updateFlag(area, "break", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + "Set break to " + args[3]);
                        area.setBreakAllowed(trueFalse);
                    } else if (flag.equalsIgnoreCase("place")) {
                        AreaManager.updateFlag(area, "place", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + "Set place to " + args[3]);
                        area.setPlace(trueFalse);
                    } else if (flag.equalsIgnoreCase("pvp")) {
                        AreaManager.updateFlag(area, "pvp", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + "Set pvp to " + args[3]);
                        area.setPvp(trueFalse);
                    } else if (flag.equalsIgnoreCase("interact")) {
                        AreaManager.updateFlag(area, "interact", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + "Set interact to " + args[3]);
                        area.setInteract(trueFalse);
                    } else if (flag.equalsIgnoreCase("god")) {
                        AreaManager.updateFlag(area, "god", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + "Set god to " + args[3]);
                        area.setGod(trueFalse);
                    } else if (flag.equalsIgnoreCase("mob-spawn")) {
                        AreaManager.updateFlag(area, "mob-spawn", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + "Set mob-spawn to " + args[3]);
                        area.setMobSpawn(trueFalse);
                    } else {
                        sender.sendMessage(AreaProtection.Prefix + "Flag " + flag + " not found.");
                        sender.sendMessage(AreaProtection.Prefix + "Available flags: break, place, interact, pvp, god, mob-spawn");
                    }
                } else {
                    sender.sendMessage(AreaProtection.Prefix + "§cCouldn't find a area with name " + args[1]);
                }
            } else {
                ap.sendSenderUsage(sender);
            }
        }
    }
}
