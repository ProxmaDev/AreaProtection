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

package net.llamagames.AreaProtection;

import cn.nukkit.Player;
import cn.nukkit.command.CommandMap;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import net.llamagames.AreaProtection.commands.AreaProtectionCommand;
import net.llamagames.AreaProtection.listener.BlockListener;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.AreaManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AreaProtection extends PluginBase {

    public static String Prefix = "§8» §3AreaProtection §8| §7";
    public static HashMap<Player, Position> firstPoses = new HashMap<>();
    public static HashMap<Player, Position> secondPoses = new HashMap<>();
    public static HashMap<Player, Integer> playersInPosMode = new HashMap<Player, Integer>();
    public static ArrayList<Area> areas = new ArrayList<>();
    public static ArrayList<Player> bypassPlayers = new ArrayList<>();

    public static AreaProtection instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        init();
    }

    private void init() {
        saveDefaultConfig();
        loadAreas();
        registerCommands();
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
    }

    public Area getAreaByPos(Position position) {
        Area area = null;

        for(Area areaa: areas) {
            if (areaa.isInArea(position)) {
                area = areaa;
                break;
            }
        }

        return area;
    }

    public Area getAreaByName(String name) {
        Area area = null;

        for (Area areaa: areas) {
            if(areaa.getName().equalsIgnoreCase(name)) {
                area = areaa;
            }
        }

        return area;
    }

    @SuppressWarnings("unchecked")
    public void loadAreas() {
        areas.clear();
        for (Map.Entry<String, Object> map: getConfig().getSection("areas").getAllMap().entrySet()) {
            String name = map.getKey();
            HashMap<String, Object> area = (HashMap<String, Object>) map.getValue();

            String world = area.get("world").toString();
            double x = (double) area.get("pos1x");
            double y = (double) area.get("pos1y");
            double z = (double) area.get("pos1z");
            double xx = (double) area.get("pos2x");
            double yy = (double) area.get("pos2y");
            double zz = (double) area.get("pos2z");
            boolean breakAllowed = (boolean) area.get("break");
            boolean place = (boolean) area.get("place");
            boolean interact = (boolean) area.get("interact");
            boolean pvp = (boolean) area.get("pvp");
            boolean god = (boolean) area.get("god");

            areas.add(new Area(name, new Vector3(x, y, z), new Vector3(xx, yy, zz), getServer().getLevelByName(world), breakAllowed, place, interact, pvp, god));
        }
    }

    public static AreaProtection getInstance() {
        return instance;
    }

    public void registerCommands() {
        CommandMap map = getServer().getCommandMap();

        map.register("ban", new AreaProtectionCommand(this));
    }

}



