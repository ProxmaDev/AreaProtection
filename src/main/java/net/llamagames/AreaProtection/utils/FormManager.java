package net.llamagames.AreaProtection.utils;

import cn.nukkit.Player;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import net.llamagames.AreaProtection.AreaProtection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FormManager {

    public static void sendFlagForm(Player player, Area area) {
        List<Element> elements = new ArrayList<>();
        HashMap<Integer, String> pending = new HashMap<>();

        int id = 0;

        for (String s : AreaProtection.flags) {
            pending.put(id, s);
            elements.add(new ElementToggle(s, area.isAllowed(s)));
            id++;
        }

        FormWindowCustom form = new FormWindowCustom(area.getName(), elements);
        AreaProtection.flagPending.put(player.getName(), pending);
        player.showFormWindow(form);
    }

}
