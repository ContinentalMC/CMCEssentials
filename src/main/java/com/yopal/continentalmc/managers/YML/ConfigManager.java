package com.yopal.continentalmc.managers.YML;

import com.yopal.continentalmc.CMCEssentials;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private static FileConfiguration config;

    public static void setupConfig(CMCEssentials cmc) {
        ConfigManager.config = cmc.getConfig();
        cmc.saveDefaultConfig();
    }

    public static void reloadConfig(CMCEssentials cmc) { cmc.reloadConfig(); config = cmc.getConfig();}

    public static List<String> getWelcomeMessages() { return config.getStringList("welcome-options"); }
    public static int getWelcomeAmount() { return config.getInt("welcome-amount");}

}
