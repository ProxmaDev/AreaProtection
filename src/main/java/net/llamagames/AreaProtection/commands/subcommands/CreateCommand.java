package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.FormManager;
import net.llamagames.AreaProtection.utils.Language;

public class CreateCommand extends SubCommand {

    public CreateCommand() {
        super("create");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            ap.sendUsage(sender);
            return;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!AreaProtection.firstPoses.containsKey(player) || !AreaProtection.secondPoses.containsKey(player)) {
                ap.sendUsage(player);
                player.sendMessage(Language.get("positions-missing"));
                return;
            }
            Area area = ap.createArea(args[1], AreaProtection.firstPoses.get(player), AreaProtection.secondPoses.get(player), AreaProtection.firstPoses.get(player).getLevel());
            FormManager.sendFlagForm(player, area);
            player.sendMessage(Language.getAndReplace("area-created", args[1]));
        }
    }
}
