package net.llamagames.AreaProtection.commands;

import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.commands.subcommands.SubCommand;

import java.util.HashMap;

public class APSubCommandHandler {

    private static HashMap<String, SubCommand> subcommands = new HashMap<>();

    public static void runSubCommand(String name, CommandSender sender, String[] args) {
        if (subcommands.containsKey(name)) {
            subcommands.get(name).execute(sender, args);
        } else {
            AreaProtection.getInstance().sendSenderUsage(sender);
        }
    }

    public static void registerSubCommand(String name, SubCommand subCommand) {
        subcommands.put(name, subCommand);
    }

}
