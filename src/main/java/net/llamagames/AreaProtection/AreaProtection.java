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
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import net.llamagames.AreaProtection.commands.AreaProtectionCommand;
import net.llamagames.AreaProtection.commands.subcommands.*;
import net.llamagames.AreaProtection.commands.APSubCommandHandler;
import net.llamagames.AreaProtection.listener.BlockListener;
import net.llamagames.AreaProtection.listener.EntityListener;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.AreaManager;
import net.llamagames.AreaProtection.utils.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AreaProtection extends PluginBase {

    public static String Prefix;
    public static HashMap<Player, Position> firstPoses = new HashMap<>();
    public static HashMap<Player, Position> secondPoses = new HashMap<>();
    public static HashMap<Player, Integer> playersInPosMode = new HashMap<Player, Integer>();
    public static ArrayList<Area> areas = new ArrayList<>();
    public static ArrayList<Player> bypassPlayers = new ArrayList<>();

    public static ArrayList<String> flags = new ArrayList<String>(Arrays.asList("break", "place", "interact", "pvp", "god", "mob-spawn"));

    public static HashMap<Player, Long> messageCooldowns = new HashMap<>();

    public static AreaProtection instance;

    private int version = 1;

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
        if (!getConfig().exists("version")) {
            updateVersion();
        } else {
            if (getConfig().getInt("version") != version) {
                updateVersion();
            }
        }
        loadAreas();
        Language.init();
        Prefix = Language.getMessage("prefix");
        registerCommands();
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
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
            boolean mobspawn = (boolean) area.get("mob-spawn");

            areas.add(new Area(name, new Vector3(x, y, z), new Vector3(xx, yy, zz), getServer().getLevelByName(world), breakAllowed, place, interact, pvp, god, mobspawn));
        }
    }

    public static AreaProtection getInstance() {
        return instance;
    }

    public void sendUsage(Player player) {
        sendSenderUsage(player);
        /*player.sendMessage(AreaProtection.Prefix + "/ap list");
        player.sendMessage(AreaProtection.Prefix + "/ap info <area_name>");
        player.sendMessage(AreaProtection.Prefix + "/ap goto <area_name>");
        player.sendMessage(AreaProtection.Prefix + "/ap bypass");
        player.sendMessage(AreaProtection.Prefix + "/ap pos1");
        player.sendMessage(AreaProtection.Prefix + "/ap pos2");
        player.sendMessage(AreaProtection.Prefix + "/ap create <name>");
        player.sendMessage(AreaProtection.Prefix + "/ap delete <area_name>");
        player.sendMessage(AreaProtection.Prefix + "/ap flag <area_name> <flag> <true/false>");*/
    }

    public void sendSenderUsage(CommandSender sender) {
        sender.sendMessage(AreaProtection.Prefix + "/ap list");
        sender.sendMessage(AreaProtection.Prefix + "/ap info <area_name>");
        sender.sendMessage(AreaProtection.Prefix + "/ap goto <area_name>");
        sender.sendMessage(AreaProtection.Prefix + "/ap bypass");
        sender.sendMessage(AreaProtection.Prefix + "/ap pos1");
        sender.sendMessage(AreaProtection.Prefix + "/ap pos2");
        sender.sendMessage(AreaProtection.Prefix + "/ap create <name>");
        sender.sendMessage(AreaProtection.Prefix + "/ap delete <area_name>");
        sender.sendMessage(AreaProtection.Prefix + "/ap flag <area_name> <flag> <true/false>");
    }

    private void registerCommands() {
        CommandMap map = getServer().getCommandMap();

        map.register("ban", new AreaProtectionCommand(this));
        registerSubCommand("bypass", new BypassCommand());
        registerSubCommand("pos1", new FirstPosCommand());
        registerSubCommand("pos2", new SecondPosCommand());
        registerSubCommand("list", new ListCommand());
        registerSubCommand("info", new InfoCommand());
        registerSubCommand("goto", new GotoCommand());
        registerSubCommand("create", new CreateCommand());
        registerSubCommand("delete", new DeleteCommand());
        registerSubCommand("flag", new FlagCommand());
    }

    private void updateVersion() {
        getLogger().alert("Config is outdated!");
        getLogger().info("Updating config to version " + version + "...");
        if (!getConfig().exists("version")) {
            getConfig().set("version", version);
        }
        if (getConfig().exists("areas")) {
            getLogger().info("Existing areas found. updating...");
            for (Map.Entry<String, Object> map: getConfig().getSection("areas").getAllMap().entrySet()) {
                String name = map.getKey();
                boolean updated = false;

                if (!getConfig().exists("areas." + name + ".mob-spawn")) {
                    getConfig().set("areas." + name + ".mob-spawn", false);
                    updated = true;
                }
                if (updated) {
                    getLogger().info("Updated area " + name + ".");
                }
                getConfig().save();
                getConfig().reload();
            }
        }
    }

    // API Stuff (it's in the main because the api is not so big.)
    public Area getAreaByPos(Position position) {

        for(Area area: areas) {
            if (area.isInArea(position)) {
                return area;
            }
        }

        return null;
    }

    public Area getAreaByName(String name) {

        for (Area area: areas) {
            if(area.getName().equalsIgnoreCase(name)) {
                return area;
            }
        }

        return null;
    }

    public void createArea(String name, Vector3 pos1, Vector3 pos2, Level world) {
        AreaManager.createArea(name, pos1, pos2, world);
    }

    public void deleteArea(String name) {
        AreaManager.deleteAreaByName(name);
    }

    public void setFlagOfArea(Area area, String flag, boolean value) {
        if (flags.contains(flag)) {
            AreaManager.updateFlag(area, flag, value);
        } else {
            getLogger().info("API ERROR: invalid flag: " + "\"" + flag + "\"");
        }
    }

    public void registerSubCommand(String name, SubCommand subCommand) {
        APSubCommandHandler.registerSubCommand(name, subCommand);
    }

    @Deprecated
    public void reloadArea(Area area) {
        getLogger().info("§cWARNING: reloadArea() is deprecated and will be removed soon.");
        getLogger().info("§cWARNING: A plugin/addon is still using this method.");
        /*
        Config c = getConfig();
        String aP = "areas." + area.getName();
        area.setBreakAllowed(c.getBoolean(aP + ".break"));
        area.setPlace(c.getBoolean(aP + ".place"));
        area.setInteract(c.getBoolean(aP + ".interact"));
        area.setPvp(c.getBoolean(aP + ".pvp"));
        area.setGod(c.getBoolean(aP + ".god"));
        area.setMobSpawn(c.getBoolean(aP + ".mobspawn"))*/
    }

    public void registerMessage(String key, String message) {
        Language.messages.put(key, message);
    }
}



