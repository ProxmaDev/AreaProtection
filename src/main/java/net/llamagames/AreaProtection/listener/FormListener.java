package net.llamagames.AreaProtection.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.window.FormWindowCustom;
import net.llamagames.AreaProtection.AreaProtection;
import net.llamagames.AreaProtection.utils.Area;
import net.llamagames.AreaProtection.utils.AreaManager;

import java.util.HashMap;
import java.util.Map;

public class FormListener implements Listener {

    @EventHandler
    public void onFlagForm(PlayerFormRespondedEvent event) {
        if (event.getWindow() instanceof FormWindowCustom) {
            FormWindowCustom form = (FormWindowCustom) event.getWindow();
            Area area = AreaProtection.instance.getAreaByName(form.getTitle());

            if (area != null && form.getResponse() != null && AreaProtection.flagPending.containsKey(event.getPlayer().getName())) {
                HashMap<Integer, String> pending = AreaProtection.flagPending.get(event.getPlayer().getName());

                for (Map.Entry<Integer, String> entry : pending.entrySet()) {
                    area.setAllowed(entry.getValue(), form.getResponse().getToggleResponse(entry.getKey()));
                }

                AreaProtection.flagPending.remove(event.getPlayer().getName());

                AreaManager.saveAreaAsync(area);
            }
        }
    }

}
