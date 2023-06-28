package com.yopal.continentalmc.managers.YML;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class CasinoManager {
    private static YamlConfiguration casinoYML;
    private static File file;

    public static void setupFile(CMCEssentials cmc) {
        file = new File(cmc.getDataFolder(), "casino.yml");

        if (!file.exists()) {
            cmc.saveResource("casino.yml", false);
        }

        CasinoManager.casinoYML = YamlConfiguration.loadConfiguration(file);

    }

    public static void reloadFile(CMCEssentials cmc) {
        File file = new File(cmc.getDataFolder(), "casino.yml");
        CasinoManager.casinoYML = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveFile(CMCEssentials cmc) {

        try {
            casinoYML.save(file);
        } catch (IOException e) {
            cmc.getLogger().log(Level.SEVERE, "casino.yml couldn't be saved");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    // getters
    public static HashMap<String, Integer> getCoinPrices() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (String string : casinoYML.getConfigurationSection("coins.").getKeys(false)) {
            hashMap.put(string, casinoYML.getInt("coins." + string));
        }

        return hashMap;
    }

    public static int getWinPercentage(Block block) {
        ConfigurationSection configSection = casinoYML.getConfigurationSection("machines.");

        for (String string : configSection.getKeys(false)) {
            if (casinoYML.getLocation("machines." + string + ".location").equals(block.getLocation())) {
                return casinoYML.getInt("machines." + string + ".winningPercentage") ;
            }
        }

        return 0;
    }

    public static MachineTypes getMachineType(Block block) {
        ConfigurationSection configSection = casinoYML.getConfigurationSection("machines.");

        for (String string : configSection.getKeys(false)) {
            if (casinoYML.getLocation("machines." + string + ".location").equals(block.getLocation())) {
                return MachineTypes.valueOf(casinoYML.getString("machines." + string + ".machineType"));
            }
        }

        return null;
    }

    public static List<String> getVillagerHeadTextures() { return casinoYML.getStringList("villagerTextures"); }

    public static List<String> getVillagerNames() { return casinoYML.getStringList("randomNames"); }

    public static HashMap<String, List<String>> getPersonality() {
        HashMap<String, List<String>> personality = new HashMap<>();
        personality.put("job", casinoYML.getStringList("personality.job"));
        personality.put("favAnimal", casinoYML.getStringList("personality.favAnimal"));
        personality.put("favColor", casinoYML.getStringList("personality.favColor"));
        personality.put("favHobby", casinoYML.getStringList("personality.favHobby"));

        return personality;
    }

    public static List<String> getHenryWelcomes() {
        return casinoYML.getStringList("henryWelcomes");
    }

    public static boolean isMachine(String name) {
        return casinoYML.contains("machines." + name);
    }

    public static boolean isMachine(Block block) {
        ConfigurationSection configSection = casinoYML.getConfigurationSection("machines.");

        if (configSection == null) {
            return false;
        }

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

    public static void setArmorStandUUIDs(CMCEssentials cmc, String name, ArrayList<UUID> uuids) {
        String startPath = "machines." + name + ".armorStands.";

        casinoYML.set(startPath + "armorStand1", uuids.get(0).toString());
        casinoYML.set(startPath + "armorStand2", uuids.get(1).toString());
        casinoYML.set(startPath + "armorStand3", uuids.get(2).toString());
        casinoYML.set(startPath + "armorStand4", uuids.get(3).toString());


        saveFile(cmc);
    }

    public static void setLoc(CMCEssentials cmc, int pointNum, Location loc) {
        casinoYML.set("bingoCuboid.loc" + pointNum, loc);
        saveFile(cmc);
    }

    public static ArrayList<UUID> getArmorStandUUIDs(String name) {
        String startPath = "machines." + name + ".armorStands.";

        ArrayList<UUID> uuids = new ArrayList<>();
        uuids.add(UUID.fromString(casinoYML.getString(startPath + "armorStand1")));
        uuids.add(UUID.fromString(casinoYML.getString(startPath + "armorStand2")));
        uuids.add(UUID.fromString(casinoYML.getString(startPath + "armorStand3")));
        uuids.add(UUID.fromString(casinoYML.getString(startPath + "armorStand4")));

        return uuids;
    }

    public static void resetBingoTotal(CMCEssentials cmc) {
        casinoYML.set("bingoTotal", 0);
        saveFile(cmc);
    }

    public static void addOnBingoTotal(CMCEssentials cmc, int amount) {
        casinoYML.set("bingoTotal", casinoYML.getInt("bingoTotal") + amount);
        saveFile(cmc);
    }



    public static void removeMachine(CMCEssentials cmc, String string) {
        casinoYML.set("machines." + string, null);
        saveFile(cmc);
    }

    public static int getSlotWinning(Material material) {
        return casinoYML.getInt("slots." + material);
    }
    public static int getImpostorWinning(String winType) { return casinoYML.getInt("impostor." + winType); }
    public static int getHorseBetWinning() { return casinoYML.getInt("horseBet"); }
    public static int getPlatformsWinning() { return casinoYML.getInt("platforms"); }
    public static int getRPSWinPercentageDowngrade() { return casinoYML.getInt("rpsWinPercentageDowngrade"); }
    public static int getRPSWinPercentageIncorrectDowngrade() { return casinoYML.getInt("rpsWinPercentageDowngrade"); }
    public static int getPercentageCut() { return casinoYML.getInt("bingoCut"); }
    public static int getBingoMaxOut() { return casinoYML.getInt("bingoMaxOut"); }
    public static int getBingoTotal() { return casinoYML.getInt("bingoTotal"); }
    public static List<Material> getBingoMaterials() {
        List<Material> materials = new ArrayList<>();
        for (String string : casinoYML.getStringList("bingoMaterials")) {
            materials.add(Material.valueOf(string));
        }

        return materials;
    }

    public static Location getLoc1() { return casinoYML.getLocation("bingoCuboid.loc1"); }
    public static Location getLoc2() { return casinoYML.getLocation("bingoCuboid.loc2"); }


    // setters
    public static void setMachine(CMCEssentials cmc, MachineTypes type, Block block, String name, int winningPercentage) {
        String startPath = "machines." + name;

        casinoYML.set(startPath + ".winningPercentage", winningPercentage);
        casinoYML.set(startPath + ".machineType", type.toString());
        casinoYML.set(startPath + ".location", block.getLocation());

        saveFile(cmc);
    }
}
