package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.AreaManager;
import net.llamagames.AreaProtection.utils.Language;

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
                        sender.sendMessage(AreaProtection.Prefix + Language.getMessage("flag-trueFalse"));
                        return;
                    }
                    if (flag.equalsIgnoreCase("break")) {
                        AreaManager.updateFlag(area, "break", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("set-flag", "break",args[3]));
                        area.setBreakAllowed(trueFalse);
                    } else if (flag.equalsIgnoreCase("place")) {
                        AreaManager.updateFlag(area, "place", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("set-flag", "place", args[3]));
                        area.setPlace(trueFalse);
                    } else if (flag.equalsIgnoreCase("pvp")) {
                        AreaManager.updateFlag(area, "pvp", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("set-flag", "pvp", args[3]));
                        area.setPvp(trueFalse);
                    } else if (flag.equalsIgnoreCase("interact")) {
                        AreaManager.updateFlag(area, "interact", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("set-flag", "interact", args[3]));
                        area.setInteract(trueFalse);
                    } else if (flag.equalsIgnoreCase("god")) {
                        AreaManager.updateFlag(area, "god", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("set-flag", "god", args[3]));
                        area.setGod(trueFalse);
                    } else if (flag.equalsIgnoreCase("mob-spawn")) {
                        AreaManager.updateFlag(area, "mob-spawn", trueFalse);
                        sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("set-flag", "mob-spawn", args[3]));
                        area.setMobSpawn(trueFalse);
                    } else {
                        sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("flag-not-found", flag));
                        sender.sendMessage(AreaProtection.Prefix + Language.getMessage("flag-list"));
                    }
                } else {
                    sender.sendMessage(AreaProtection.Prefix + Language.getAndReplace("cant-find-area", args[1]));
                }
            } else {
                ap.sendSenderUsage(sender);
            }
        }
    }
}
