package com.yopal.continentalmc.managers.YML;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
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

    // getters
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

    public static boolean isMachine(String name) {
        return casinoYML.contains("machines." + name);
    }

    public static boolean isMachine(Block block) {
        ConfigurationSection configSection = casinoYML.getConfigurationSection("machines.");

        if (configSection.getKeys(false).isEmpty()) {
            return false;
        }

        for (String string : configSection.getKeys(false)) {
            if (casinoYML.getLocation("machines." + string + ".location").equals(block.getLocation())) {
                return true;
            }
        }

        return false;

    }


    // setters
    public static void setMachine(MachineTypes type, Block block, String name, int winningPercentage) {
        String startPath = "machines." + name;

        casinoYML.set(startPath + "winningPercentage", winningPercentage);
        casinoYML.set(startPath + ".machineType", type);
        casinoYML.set(startPath + ".location", block.getLocation());


    }
}
