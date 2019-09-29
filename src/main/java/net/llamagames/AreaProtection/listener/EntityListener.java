package net.llamagames.AreaProtection.listener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntitySpawnEvent;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;

public class EntityListener implements Listener {

    private AreaProtection plugin;

    public EntityListener(AreaProtection plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobSpawn(EntitySpawnEvent event) {
        Area area = plugin.getAreaByPos(event.getPosition());
        if (area != null) {
            if (event.getEntity() instanceof Player) {
                return;
            }
            if (!area.isMobSpawnAllowed()) {
                event.setCancelled(true);
            }
        }
    }

}
