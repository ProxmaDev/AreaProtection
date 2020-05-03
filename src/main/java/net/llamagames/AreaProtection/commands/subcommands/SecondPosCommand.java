package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Language;

public class SecondPosCommand extends SubCommand {

    public SecondPosCommand() {
        super("pos2");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            AreaProtection.playersInPosMode.put(player, 1);
            player.sendMessage(Language.get("set-pos2"));
        }
    }
}
