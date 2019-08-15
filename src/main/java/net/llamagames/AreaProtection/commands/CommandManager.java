/*
    AreaProtection:

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

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandMap;
import cn.nukkit.command.PluginIdentifiableCommand;
import net.llamagames.AreaProtection.AreaProtection;

public abstract class CommandManager extends Command implements PluginIdentifiableCommand {
    private AreaProtection plugin;

    public CommandManager(AreaProtection plugin, String name, String desc, String usage) {
        super(name, desc, usage);

        this.plugin = plugin;
    }

    public CommandManager(AreaProtection plugin, String name, String desc, String usage, String[] aliases) {
        super(name, desc, usage, aliases);

        this.plugin = plugin;
    }

    public CommandManager(AreaProtection plugin, Boolean override, String name, String desc, String usage, String[] aliases) {
        super(name, desc, usage, aliases);

        this.plugin = plugin;

        CommandMap map = plugin.getServer().getCommandMap();
        Command command = map.getCommand(name);
        command.setLabel(name + "_disabled");
        command.unregister(map);
    }

    public AreaProtection getPlugin() {
        return plugin;
    }
}
