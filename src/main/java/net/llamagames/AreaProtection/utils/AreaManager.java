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

package net.llamagames.AreaProtection.utils;

import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import net.llamagames.AreaProtection.AreaProtection;

public class AreaManager {

    public static void createArea(String name, Vector3 pos1, Vector3 pos2, Level world) {

        AreaProtection plugin = AreaProtection.getInstance();
        Config config = plugin.getConfig();
        config.set("areas." + name + ".world", world.getName());
        config.set("areas." + name + ".pos1x", pos1.x);
        config.set("areas." + name + ".pos1y", pos1.y);
        config.set("areas." + name + ".pos1z", pos1.z);
        config.set("areas." + name + ".pos2x", pos2.x);
        config.set("areas." + name + ".pos2y", pos2.y);
        config.set("areas." + name + ".pos2z", pos2.z);
        config.set("areas." + name + ".break", true);
        config.set("areas." + name + ".place", true);
        config.set("areas." + name + ".interact", true);
        config.set("areas." + name + ".pvp", true);
        config.set("areas." + name + ".god", false);
        config.set("areas." + name + ".mob-spawn", true);
        config.save();
        config.reload();
        plugin.loadAreas();

        plugin.getLogger().info(AreaProtection.Prefix + "New area " + name + " §rcreated.");
    }

    public static void updateFlag(Area area, String key, boolean value) {

        AreaProtection plugin = AreaProtection.getInstance();
        Config config = plugin.getConfig();
        config.set("areas." + area.getName() + "." + key, value);
        config.save();
        config.reload();
        //plugin.loadAreas();

        plugin.getLogger().info(AreaProtection.Prefix + "Set " + key + " for " + area.getName() + " to " + value);
    }

}
