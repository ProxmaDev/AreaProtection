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
import net.llamagames.AreaProtection.commands.AreaProtectionCommand;
import net.llamagames.AreaProtection.commands.subcommands.*;
import net.llamagames.AreaProtection.commands.APSubCommandHandler;
import net.llamagames.AreaProtection.listener.BlockListener;
import net.llamagames.AreaProtection.listener.EntityListener;
import net.llamagames.AreaProtection.listener.FormListener;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.AreaFlag;
import net.llamagames.AreaProtection.utils.AreaManager;
import net.llamagames.AreaProtection.utils.Language;

import java.util.*;

public class AreaProtection extends PluginBase {

    public static HashMap<Player, Position> firstPoses = new HashMap<>();
    public static HashMap<Player, Position> secondPoses = new HashMap<>();
    public static HashMap<Player, Integer> playersInPosMode = new HashMap<Player, Integer>();
    public static ArrayList<Area> areas = new ArrayList<>();
    public static ArrayList<Player> bypassPlayers = new ArrayList<>();

    public static HashMap<String, HashMap<Integer, String>> flagPending = new HashMap<>();

    public static Config areaDB;
    public static Config config;

    public static ArrayList<String> flags = new ArrayList<>();

    public static HashMap<Player, Long> messageCooldowns = new HashMap<>();

    public static AreaProtection instance;

    private int version = 2;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        init();
    }

    private void init() {
        areaDB = new Config(getDataFolder() + "/areas.yml", Config.YAML);
        saveDefaultConfig();
        config = getConfig();
        registerDefaultFlags();

        if (getConfig().exists("version")) {
            getLogger().info("§cIMPORTANT");
            getLogger().info("§cCan't load AreaProtection!");
            getLogger().info("§cPlease delete your entire AreaProtection plugin folder.");
            getLogger().info("§cThe way how AreaProtection works has changed a lot.");
            getLogger().info("§cIMPORTANT");
            getServer().getPluginManager().disablePlugin(instance);
            return;
        }

        loadAreas();
        Language.init();
        registerCommands();
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        getServer().getPluginManager().registerEvents(new FormListener(), this);
    }


    @SuppressWarnings("unchecked")
    public void loadAreas() {
        areas.clear();
        for (Map.Entry<String, Object> map : areaDB.getAll().entrySet()) {
            boolean needsUpdate = false;
            String name = map.getKey();
            HashMap<String, Object> area = (HashMap<String, Object>) map.getValue();

            String world = area.get("world").toString();
            double x = (double) area.get("pos1x");
            double y = (double) area.get("pos1y");
            double z = (double) area.get("pos1z");
            double xx = (double) area.get("pos2x");
            double yy = (double) area.get("pos2y");
            double zz = (double) area.get("pos2z");

            HashMap<String, AreaFlag> areaFlags = new HashMap<>();
            List<String> rawFlags = (List<String>) area.get("flags");

            for (String s : flags) {
                if (!rawFlags.contains(s + ":false") && !rawFlags.contains(s + ":true")) {
                    rawFlags.add(s + ":false");
                    needsUpdate = true;
                }
            }

            rawFlags.forEach((s -> {
                String[] i = s.split(":");
                boolean allowed = false;
                if (i[1].equalsIgnoreCase("true")) allowed = true;

                if (flags.contains(i[0])) areaFlags.put(i[0], new AreaFlag(i[0], allowed));
            }));

            Area newArea = new Area(name, new Vector3(x, y, z), new Vector3(xx, yy, zz), world, areaFlags);

            areas.add(newArea);

            if (needsUpdate) {
                AreaManager.saveAreaAsync(newArea);
            }
        }
    }

    public static AreaProtection getInstance() {
        return instance;
    }

    public void sendUsage(Player player) {
        this.sendUsage((CommandSender) player);
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Language.prefix + "/ap list");
        sender.sendMessage(Language.prefix + "/ap info <area_name>");
        sender.sendMessage(Language.prefix + "/ap goto <area_name>");
        sender.sendMessage(Language.prefix + "/ap bypass");
        sender.sendMessage(Language.prefix + "/ap pos1");
        sender.sendMessage(Language.prefix + "/ap pos2");
        sender.sendMessage(Language.prefix + "/ap reload");
        sender.sendMessage(Language.prefix + "/ap create <name>");
        sender.sendMessage(Language.prefix + "/ap delete <area_name>");
        sender.sendMessage(Language.prefix + "/ap flag <area_name>");
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
        registerSubCommand("reload", new ReloadCommand());
    }

    public void registerDefaultFlags() {
        flags.clear();
        if (config.getBoolean("flags.break")) flags.add("break");
        if (config.getBoolean("flags.place")) flags.add("place");
        if (config.getBoolean("flags.interact")) flags.add("interact");
        if (config.getBoolean("flags.pvp")) flags.add("pvp");
        if (config.getBoolean("flags.god")) flags.add("god");
        if (config.getBoolean("flags.mob-spawn")) flags.add("mob-spawn");
        if (config.getBoolean("flags.explosion")) flags.add("explosion");
        if (config.getBoolean("flags.drop-item")) flags.add("drop-item");
    }

    public boolean hasBypassPerms(Player player) {
        return bypassPlayers.contains(player);
    }

    // API stuff
    public Area getAreaByPos(Position position) {

        for (Area area : areas) {
            if (area.isInArea(position)) {
                return area;
            }
        }

        return null;
    }

    public Area getAreaByName(String name) {

        for (Area area : areas) {
            if (area.getName().equalsIgnoreCase(name)) {
                return area;
            }
        }

        return null;
    }

    public Area createArea(String name, Vector3 pos1, Vector3 pos2, Level world) {
        return AreaManager.createArea(name, pos1, pos2, world);
    }

    public void deleteArea(String name) {
        AreaManager.deleteArea(name);
    }

    public void setFlagOfArea(Area area, String flag, boolean value) {
        area.setAllowed(flag, value);
        AreaManager.saveAreaAsync(area);
    }

    public void registerSubCommand(String name, SubCommand subCommand) {
        APSubCommandHandler.registerSubCommand(name, subCommand);
    }

    public void registerMessage(String key, String message) {
        Language.messages.put(key, message);
    }
}



