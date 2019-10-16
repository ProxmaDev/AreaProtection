package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;

public class FirstPosCommand extends SubCommand {

    public FirstPosCommand() {
        super("pos1");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            AreaProtection.playersInPosMode.put(player, 0);
            player.sendMessage(AreaProtection.Prefix + "Break or place a block to set 1st Position.");
        }
    }
}
