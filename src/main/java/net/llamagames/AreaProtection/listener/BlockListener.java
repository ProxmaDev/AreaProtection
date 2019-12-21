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
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.ItemID;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.Language;

public class BlockListener implements Listener {

    private AreaProtection plugin;

    public BlockListener(AreaProtection plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event) {
        if(AreaProtection.playersInPosMode.containsKey(event.getPlayer())) {
            if(AreaProtection.playersInPosMode.get(event.getPlayer()) == 0) {
                AreaProtection.firstPoses.put(event.getPlayer(), event.getBlock().getLocation());
                event.getPlayer().sendMessage(AreaProtection.Prefix + Language.getMessage("pos1-set"));
                event.setCancelled(true);
            } else if(AreaProtection.playersInPosMode.get(event.getPlayer()) == 1) {
                AreaProtection.secondPoses.put(event.getPlayer(), event.getBlock().getLocation());
                event.getPlayer().sendMessage(AreaProtection.Prefix + Language.getMessage("pos2-set"));
                event.setCancelled(true);
            }
            AreaProtection.playersInPosMode.remove(event.getPlayer());
            return;
        }
        Area area = plugin.getAreaByPos(event.getBlock().getLocation());
        if(area != null) {
            if(!area.isBreakAllowed()) {
                if(!hasBypassPerms(event.getPlayer())) {
                    event.setCancelled();
                    notify(event.getPlayer(), Language.getMessage("no-break"));
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent event) {
        if(AreaProtection.playersInPosMode.containsKey(event.getPlayer())) {
            if(AreaProtection.playersInPosMode.get(event.getPlayer()) == 0) {
                AreaProtection.firstPoses.put(event.getPlayer(), event.getBlock().getLocation());
                event.getPlayer().sendMessage(AreaProtection.Prefix + Language.getMessage("pos1-set"));
                event.setCancelled(true);
            } else if(AreaProtection.playersInPosMode.get(event.getPlayer()) == 1) {
                AreaProtection.secondPoses.put(event.getPlayer(), event.getBlock().getLocation());
                event.getPlayer().sendMessage(AreaProtection.Prefix + Language.getMessage("pos2-set"));
                event.setCancelled(true);
            }
            AreaProtection.playersInPosMode.remove(event.getPlayer());
            return;
        }

        Area area = plugin.getAreaByPos(event.getBlock().getLocation());
        if(area != null) {
            if(!area.isPlaceAllowed()) {
                if(!hasBypassPerms(event.getPlayer())) {
                    event.setCancelled(true);
                    notify(event.getPlayer(), Language.getMessage("no-place"));
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
                    notify(event.getPlayer(), Language.getMessage("no-interact"));
                }
            }
            if (!area.isPlaceAllowed()) {
                if (!hasBypassPerms(event.getPlayer())) {
                    if (event.getItem().getId() == ItemID.BUCKET) {
                        event.setCancelled(true);
                        notify(event.getPlayer(), Language.getMessage("no-place"));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
                Area playerArea = plugin.getAreaByPos(event.getEntity().getPosition());
                if(playerArea != null) {
                    if(playerArea.isGod()) {
                        event.setCancelled();
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
                        notify(damager, Language.getMessage("no-pvp"));
                    }
                }
            } else {

                Area damagerArea = plugin.getAreaByPos(damager.getPosition());

                if(damagerArea != null) {
                    if(!damagerArea.isPvpAllowed()) {
                        if(!hasBypassPerms(damager)) {
                            event.setCancelled();
                            notify(damager, Language.getMessage("no-pvp"));
                        }
                    }
                }

            }
        }
    }

    public boolean hasBypassPerms(Player player) {
        if(AreaProtection.bypassPlayers.contains(player)) {
            return true;
        } else {
            return false;
        }
    }

    public void notify(Player player, String message) {
        if (AreaProtection.messageCooldowns.containsKey(player)) {
            long cooldown = AreaProtection.messageCooldowns.get(player);
            if (cooldown > System.currentTimeMillis()) {
                return;
            } else {
                AreaProtection.messageCooldowns.remove(player);
                AreaProtection.messageCooldowns.put(player, System.currentTimeMillis() + 2500);
                player.sendMessage(AreaProtection.Prefix + message);
            }
        } else {
            AreaProtection.messageCooldowns.put(player, System.currentTimeMillis() + 2500);
            player.sendMessage(AreaProtection.Prefix + message);
        }
    }

}



