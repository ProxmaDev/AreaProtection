package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;

public class BypassCommand extends SubCommand {

    public BypassCommand() {
        super("bypass");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (AreaProtection.bypassPlayers.contains(player)) {
                AreaProtection.bypassPlayers.remove(player);
                player.sendMessage(AreaProtection.Prefix + "You're no longer bypassing every restriction.");
            } else {
                AreaProtection.bypassPlayers.add(player);
                player.sendMessage(AreaProtection.Prefix + "You're bypassing every restriction now.");
            }

        }
    }
}
