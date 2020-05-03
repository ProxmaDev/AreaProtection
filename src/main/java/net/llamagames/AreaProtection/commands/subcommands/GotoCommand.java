package net.llamagames.AreaProtection.commands.subcommands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.Language;

public class GotoCommand extends SubCommand {

    public GotoCommand() {
        super("goto");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            ap.sendUsage(sender);
            return;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Area area = ap.getAreaByName(args[1]);
            if (area != null) {
                if (!AreaProtection.instance.getServer().isLevelLoaded(area.getWorld())) AreaProtection.instance.getServer().loadLevel(area.getWorld());
                player.teleport(new Position(area.getPos1().getX(), area.getPos1().getY(), area.getPos1().getZ(), AreaProtection.instance.getServer().getLevelByName(area.getWorld())));
                player.sendMessage(Language.getAndReplace("area-teleported", args[1]));
            } else {
                player.sendMessage(Language.getAndReplace("cant-find-area", args[1]));
            }
        }
    }
}
