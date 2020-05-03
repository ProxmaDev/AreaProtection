package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Language;

public class FirstPosCommand extends SubCommand {

    public FirstPosCommand() {
        super("pos1");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            AreaProtection.playersInPosMode.put(player, 0);
            player.sendMessage(Language.get("set-pos1"));
        }
    }
}
