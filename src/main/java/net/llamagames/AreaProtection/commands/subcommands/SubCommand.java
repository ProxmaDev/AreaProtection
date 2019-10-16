package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;

public class SubCommand {

    private String name;
    public AreaProtection ap;

    public SubCommand(String name) {
        this.name = name;
        this.ap = AreaProtection.getInstance();
    }

    public void execute(CommandSender sender, String[] args) {
        // NOTE: args[0] is the SubCommand Name.
        // Use args[1], args[2], ...
        // And yes: I was too lazy to change that :joy:
    }

    private String getName() {
        return name;
    }

}
