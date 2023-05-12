package com.yopal.continentalmc.managers.YML;

import com.yopal.continentalmc.CMCEssentials;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class CasinoManager {
    private static YamlConfiguration casinoYML;

    public static void setupFile(CMCEssentials cmc) {
        File file = new File(cmc.getDataFolder(), "casino.yml");

        if (!file.exists()) {
            cmc.saveResource("casino.yml", false);
        }

        CasinoManager.casinoYML = YamlConfiguration.loadConfiguration(file);

    }

    public static void reloadFile(CMCEssentials cmc) {
        File file = new File(cmc.getDataFolder(), "casino.yml");
        CasinoManager.casinoYML = YamlConfiguration.loadConfiguration(file);
    }

    public static HashMap<String, Integer> getCoinPrices() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (String string : casinoYML.getConfigurationSection("coins.").getKeys(false)) {
            hashMap.put(string, casinoYML.getInt("coins." + string));
        }

        return hashMap;
    }

    public static List<String> getHenryWelcomes() {
        return casinoYML.getStringList("henryWelcomes");
    }

}
