/*
    PluginName:

    Copyright (C) 2019 SchdowNVIDIA
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

    Thanks to ZAP-Hosting.com and JetBrains!

    ZAP-Hosting.com gave me a Server for testing all plugins.
    If you're interested in a cheap VPS or strong Rootserver follow the links below:
    VPS: https://zap-hosting.com/schdowvserver
    Rootserver: https://zap-hosting.com/schdowroot
    Code (10% Discount Lifetime): schdow-10
 */

package net.llamagames.AreaProtection.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.math.Vector3;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.AreaManager;

public class AreaProtectionCommand extends Command {

    private AreaProtection plugin;

    public AreaProtectionCommand(AreaProtection plugin) {
        super("ap", "Manage Areas", "/ap", new String[]{"areaprotection"});
        this.plugin = plugin;
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("todo", false, new String[]{"bypass", "pos1", "pos2", "list", "reload"})
        });
        this.commandParameters.put("areaname", new CommandParameter[]{
                new CommandParameter("todo", false, new String[]{"info", "goto", "create", "delete"}),
                new CommandParameter("area", CommandParamType.STRING, false)
        });
        this.commandParameters.put("flag", new CommandParameter[]{
                new CommandParameter("todo", false, new String[]{"flag"}),
                new CommandParameter("area", CommandParamType.STRING, false),
                new CommandParameter("flag", false, new String[]{"break", "place", "pvp", "interact", "god", "mob-spawn"}),
                new CommandParameter("bool", false, new String[]{"true", "false"})
        });
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!sender.hasPermission("areaprotection") && !(sender.isOp())) {
            sender.sendMessage("§cNo permission");
            return false;
        }
        if (args.length < 1) {
            plugin.sendSenderUsage(sender);
            return false;
        }
        APSubCommandHandler.runSubCommand(args[0], sender, args);
        return false;
    }

    @Deprecated
    public void sendUsage(Player player) {
        player.sendMessage(AreaProtection.Prefix + "/ap list");
        player.sendMessage(AreaProtection.Prefix + "/ap info <area_name>");
        player.sendMessage(AreaProtection.Prefix + "/ap goto <area_name>");
        player.sendMessage(AreaProtection.Prefix + "/ap bypass");
        player.sendMessage(AreaProtection.Prefix + "/ap pos1");
        player.sendMessage(AreaProtection.Prefix + "/ap pos2");
        player.sendMessage(AreaProtection.Prefix + "/ap reload");
        player.sendMessage(AreaProtection.Prefix + "/ap create <name>");
        player.sendMessage(AreaProtection.Prefix + "/ap delete <area_name>");
        player.sendMessage(AreaProtection.Prefix + "/ap flag <area_name> <flag> <true/false>");
    }
}
