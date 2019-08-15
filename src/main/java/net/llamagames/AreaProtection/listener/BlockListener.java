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

package net.llamagames.AreaProtection.listener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;

public class BlockListener implements Listener {

    private AreaProtection plugin;

    public BlockListener(AreaProtection plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event) {
        Area area = plugin.getAreaByPos(event.getBlock().getLocation());
        if(area != null) {
            if(!area.isBreakAllowed()) {
                if(!hasBypassPerms(event.getPlayer())) {
                    event.setCancelled();
                    event.getPlayer().sendMessage("§cYou can't break that block.");
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent event) {
        Area area = plugin.getAreaByPos(event.getBlock().getLocation());
        if(area != null) {
            if(!area.isPlaceAllowed()) {
                if(!hasBypassPerms(event.getPlayer())) {
                    event.setCancelled();
                    event.getPlayer().sendMessage("§cYou can't place that block here.");
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Area area = plugin.getAreaByPos(event.getBlock().getLocation());
        if(area != null) {
            if(!area.isInteractAllowed()) {
                if(!hasBypassPerms(event.getPlayer())) {
                    event.setCancelled();
                    event.getPlayer().sendMessage("§cYou can't interact with that.");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPvp(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            Area playerArea = plugin.getAreaByPos(player.getPosition());

            if(playerArea != null) {
                if(!playerArea.isPvpAllowed()) {
                    if(!hasBypassPerms(damager)) {
                        event.setCancelled();
                        damager.sendMessage("§cYou can't pvp here.");
                    }
                }
            } else {

                Area damagerArea = plugin.getAreaByPos(damager.getPosition());

                if(damagerArea != null) {
                    if(!damagerArea.isPvpAllowed()) {
                        if(!hasBypassPerms(damager)) {
                            event.setCancelled();
                            damager.sendMessage("§cYou can't pvp here.");
                        }
                    }
                }

            }
        }
    }

    public boolean hasBypassPerms(Player player) {
        if(player.isOp()) {
            return true;
        } else if (player.hasPermission("areaprotection.bypass")) {
            return true;
        } else {
            return false;
        }
    }

}



