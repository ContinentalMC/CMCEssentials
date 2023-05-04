package com.yopal.continentalmc.managers.YML;

import com.yopal.continentalmc.CMCEssentials;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Level;

public class ScoreManager {
    private static YamlConfiguration scoreYML;

    public static void setupFile(CMCEssentials cmc) {
        File file = new File(cmc.getDataFolder(), "score.yml");

        if (!file.exists()) {
            cmc.saveResource("score.yml", false);
        }

        ScoreManager.scoreYML = YamlConfiguration.loadConfiguration(file);

    }

    private static void saveFile(CMCEssentials cmc) {
        File file = new File(cmc.getDataFolder(), "score.yml");

        try {
            scoreYML.save(file);
        } catch (IOException e) {
            cmc.getLogger().log(Level.SEVERE, "score.yml couldn't be saved");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public static void reloadFile(CMCEssentials cmc) {
        File file = new File(cmc.getDataFolder(), "score.yml");
        ScoreManager.scoreYML = YamlConfiguration.loadConfiguration(file);
    }

    public static int getScore(UUID playerUUID) {
        return scoreYML.getInt("scores." + playerUUID.toString());
    }

    public static void setScore(CMCEssentials cmc, UUID playerUUID, int amount) {
        scoreYML.set("scores." + playerUUID.toString(), amount);

        saveFile(cmc);
    }

    public static String getTopScorer() {
        // getting all scores
        ArrayList intList = new ArrayList<Integer>();

        for (String string : scoreYML.getConfigurationSection("scores.").getKeys(false)) {
            intList.add(scoreYML.getInt("scores." + string));
        }

        if (intList.isEmpty()) {
            return "No one";
        }

        int maxInt = (int) Collections.max(intList);

        for (String string : scoreYML.getConfigurationSection("scores.").getKeys(false)) {
            if (scoreYML.getInt("scores." + string) == maxInt) {
                return Bukkit.getOfflinePlayer(UUID.fromString(string)).getName();
            }
        }

        return null;

    }
    
}
