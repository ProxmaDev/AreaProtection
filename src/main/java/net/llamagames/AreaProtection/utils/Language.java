package net.llamagames.AreaProtection.utils;

import cn.nukkit.utils.Config;
import net.llamagames.AreaProtection.AreaProtection;

import java.util.HashMap;
import java.util.Map;

public class Language {

    public static HashMap<String, String> messages = new HashMap<>();
    public static String prefix;

    public static void init() {
        messages.clear();
        AreaProtection.instance.saveResource("messages.yml");
        Config m = new Config(AreaProtection.instance.getDataFolder() + "/messages.yml");
        for (Map.Entry<String, Object> map : m.getAll().entrySet()) {
            String key = map.getKey();
            if (map.getValue() instanceof String) messages.put(key, (String) map.getValue());
        }
        prefix = m.getString("prefix");
    }

    public static String getAndReplace(String key, Object... replacements) {
        String message = get(key);
        int i = 0;
        for (Object replacement : replacements) {
            message = message.replace("[" + i + "]", String.valueOf(replacement));
            i++;
        }
        return message;
    }

    public static String getAndReplaceNoPrefix(String key, Object... replacements) {
        String message = getNoPrefix(key);
        int i = 0;
        for (Object replacement : replacements) {
            message = message.replace("[" + i + "]", String.valueOf(replacement));
            i++;
        }
        return message;
    }

    public static String get(String key) {
        return prefix.replace("&", "ยง") + messages.getOrDefault(key, "null").replace("&", "ยง");
    }

    public static String getNoPrefix(String key) {
        return messages.getOrDefault(key, "null").replace("&", "ยง");
    }

}
