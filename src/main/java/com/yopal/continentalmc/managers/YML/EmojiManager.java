package com.yopal.continentalmc.managers.YML;

import com.yopal.continentalmc.CMCEssentials;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class EmojiManager {

    private static YamlConfiguration emojisYML;

    public static void setupFile(CMCEssentials cmc) {
        File file = new File(cmc.getDataFolder(), "emoji.yml");

        if (!file.exists()) {
            cmc.saveResource("emoji.yml", false);
        }

        EmojiManager.emojisYML = YamlConfiguration.loadConfiguration(file);

    }

    public static void reloadFile(CMCEssentials cmc) {
        File file = new File(cmc.getDataFolder(), "emoji.yml");
        EmojiManager.emojisYML = YamlConfiguration.loadConfiguration(file);
    }

    public static HashMap<String, String> getEmojis() {
        HashMap<String, String> emojis = new HashMap<>();

        for (String string : emojisYML.getKeys(false)) {
            emojis.put(":" + string + ":", emojisYML.getString(string));
        }

        return emojis;

    }
}
