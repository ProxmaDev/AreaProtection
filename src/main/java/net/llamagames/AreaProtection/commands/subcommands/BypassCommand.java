package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Language;

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
                player.sendMessage(AreaProtection.Prefix + Language.getMessage("not-longer-bypassing"));
            } else {
                AreaProtection.bypassPlayers.add(player);
                player.sendMessage(AreaProtection.Prefix + Language.getMessage("bypassing-now"));
            }

        }
    }
}
