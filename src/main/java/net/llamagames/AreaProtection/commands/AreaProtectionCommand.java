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
import cn.nukkit.command.CommandSender;
import cn.nukkit.math.Vector3;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.AreaManager;

public class AreaProtectionCommand extends CommandManager {

    private AreaProtection plugin;

    public AreaProtectionCommand(AreaProtection plugin) {
        super(plugin, "ap", "Manage Areas", "/ap");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!sender.hasPermission("areaprotection") && !(sender.isOp())) {
            sender.sendMessage("§cNo permission");
            return false;
        }
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("pos1")) {
                    //AreaProtection.firstPoses.put(player, player.getPosition());
                    AreaProtection.playersInPosMode.put(player, 0);
                    player.sendMessage(AreaProtection.Prefix + "Break or place a block to set 1. Position.");
                    return false;
                } else if (args[0].equalsIgnoreCase("pos2")) {
                    //AreaProtection.secondPoses.put(player, player.getPosition());
                    AreaProtection.playersInPosMode.put(player, 1);
                    player.sendMessage(AreaProtection.Prefix + "Break or place a block to set 2. Position.");
                    return false;

                } else if (args[0].equalsIgnoreCase("list")) {
                    if (AreaProtection.areas.size() == 0) {
                        player.sendMessage(AreaProtection.Prefix + "There are no areas to list.");
                        return false;
                    } else {
                        StringBuilder list = new StringBuilder();
                        for (Area area: AreaProtection.areas) {
                            list.append(area.getName()).append(", ");
                        }
                        player.sendMessage(AreaProtection.Prefix + "A list of all areas: " + list.toString());
                        return false;
                    }
                } else if(args[0].equalsIgnoreCase("bypass")) {
                    if(AreaProtection.bypassPlayers.contains(player)) {
                        AreaProtection.bypassPlayers.remove(player);
                        player.sendMessage(AreaProtection.Prefix + "You're no longer bypassing every restriction.");
                    } else {
                        AreaProtection.bypassPlayers.add(player);
                        player.sendMessage(AreaProtection.Prefix + "You're bypassing every restriction now.");
                    }
                    return false;
                } else {
                    sendUsage(player);
                    return false;
                }
            } else if (args.length == 2) {
                if(args[0].equalsIgnoreCase("create")) {
                    if(!AreaProtection.firstPoses.containsKey(player) || !AreaProtection.secondPoses.containsKey(player)) {
                        sendUsage(player);
                        player.sendMessage(AreaProtection.Prefix + "Please set the positions first.");
                        return false;
                    }
                    AreaManager.createArea(args[1], AreaProtection.firstPoses.get(player), AreaProtection.secondPoses.get(player), AreaProtection.firstPoses.get(player).getLevel());
                    player.sendMessage(AreaProtection.Prefix + "Area " + args[1] + " created.");
                    return false;
                } else if(args[0].equalsIgnoreCase("goto")) {
                    Area area = plugin.getAreaByName(args[1]);
                    if(area != null) {
                        player.teleport(area.getPos1());
                        player.sendMessage(AreaProtection.Prefix + "Teleported to area " + args[1]);
                        return false;
                    } else {
                        player.sendMessage(AreaProtection.Prefix + "§cCouldn't find area with name " + args[1]);
                    }
                } else if(args[0].equalsIgnoreCase("info")) {
                    Area area = plugin.getAreaByName(args[1]);
                    if(area != null) {
                        player.sendMessage(AreaProtection.Prefix + "Information about " + args[1] + ":");
                        player.sendMessage(AreaProtection.Prefix + "----------------------------------");
                        player.sendMessage(AreaProtection.Prefix + "World: " + area.getWorld().getName());
                        player.sendMessage(AreaProtection.Prefix + "1. Position: X: " + area.getPos1().x + ", Y: " + area.getPos1().y + ", Z: " + area.getPos1().z);
                        player.sendMessage(AreaProtection.Prefix + "2. Position: X: " + area.getPos2().x + ", Y: " + area.getPos2().y + ", Z: " + area.getPos2().z);
                        player.sendMessage(AreaProtection.Prefix + "Break: " + area.isBreakAllowed());
                        player.sendMessage(AreaProtection.Prefix + "Place: " + area.isPlaceAllowed());
                        player.sendMessage(AreaProtection.Prefix + "Interact: " + area.isInteractAllowed());
                        player.sendMessage(AreaProtection.Prefix + "PvP: " + area.isPvpAllowed());
                        player.sendMessage(AreaProtection.Prefix + "God: " + area.isGod());
                        return false;
                    } else {
                        player.sendMessage(AreaProtection.Prefix + "§cCouldn't find area with name " + args[1]);
                    }
                } else {
                    sendUsage(player);
                }
            }  else if(args.length == 4) {
                if(args[0].equalsIgnoreCase("flag")) {
                    Area area = plugin.getAreaByName(args[1]);
                    if(area != null) {
                        String flag = args[2];
                        String bool = args[3];
                        boolean trueFalse;
                        if(bool.equalsIgnoreCase("true")) {
                            trueFalse = true;
                        } else if(bool.equalsIgnoreCase("false")) {
                            trueFalse = false;
                        } else {
                            player.sendMessage(AreaProtection.Prefix + "Flag value needs to be true or false.");
                            return false;
                        }
                        if(flag.equalsIgnoreCase("break")) {
                            AreaManager.updateFlag(area, "break", trueFalse);
                            player.sendMessage(AreaProtection.Prefix + "Set break to " + args[3]);
                            area.setBreakAllowed(trueFalse);
                            return false;
                        } else if(flag.equalsIgnoreCase("place")) {
                            AreaManager.updateFlag(area, "place", trueFalse);
                            player.sendMessage(AreaProtection.Prefix + "Set place to " + args[3]);
                            area.setPlace(trueFalse);
                            return false;
                        } else if (flag.equalsIgnoreCase("pvp")) {
                            AreaManager.updateFlag(area, "pvp", trueFalse);
                            player.sendMessage(AreaProtection.Prefix + "Set pvp to " + args[3]);
                            area.setPvp(trueFalse);
                            return false;
                        } else if (flag.equalsIgnoreCase("interact")) {
                            AreaManager.updateFlag(area, "interact", trueFalse);
                            player.sendMessage(AreaProtection.Prefix + "Set interact to " + args[3]);
                            area.setInteract(trueFalse);
                            return false;
                        } else if(flag.equalsIgnoreCase("god")) {
                            AreaManager.updateFlag(area, "god", trueFalse);
                            player.sendMessage(AreaProtection.Prefix + "Set god to " + args[3]);
                            area.setGod(trueFalse);
                            return false;
                        } else {
                            player.sendMessage(AreaProtection.Prefix + "Flag " + flag + " not found.");
                            player.sendMessage(AreaProtection.Prefix + "Available flags: break, place, interact, pvp, god");
                        }
                    } else {
                        player.sendMessage(AreaProtection.Prefix + "§cCouldn't find a area with name " + args[1]);
                        return false;
                    }
                } else {
                    sendUsage(player);
                    return false;
                }
            } else {
                sendUsage(player);
                return false;
            }
            // Code here
        }
        return false;
    }

    public void sendUsage(Player player) {
        player.sendMessage(AreaProtection.Prefix + "/ap list");
        player.sendMessage(AreaProtection.Prefix + "/ap info <area_name>");
        player.sendMessage(AreaProtection.Prefix + "/ap goto <area_name>");
        player.sendMessage(AreaProtection.Prefix + "/ap bypass");
        player.sendMessage(AreaProtection.Prefix + "/ap pos1");
        player.sendMessage(AreaProtection.Prefix + "/ap pos2");
        player.sendMessage(AreaProtection.Prefix + "/ap create <name>");
        player.sendMessage(AreaProtection.Prefix + "/ap flag <area_name> <flag> <true/false>");
    }
}
