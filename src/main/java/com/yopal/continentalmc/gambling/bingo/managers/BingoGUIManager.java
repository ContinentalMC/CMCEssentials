package com.yopal.continentalmc.gambling.bingo.managers;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.bingo.instances.BingoGUI;
import com.yopal.continentalmc.gambling.machines.horsebet.HorseBetGUI;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class BingoGUIManager {

    private static BukkitTask task;

    private static Material currentMaterial;

    private static boolean isRolling = false;

    private static HashMap<UUID, BingoGUI> guiMap = new HashMap<>();

    public static void addGUI(UUID uuid, BingoGUI gui) { guiMap.put(uuid, gui); }

    public static void removeGUI(UUID uuid) { guiMap.remove(uuid); }

    public static BingoGUI getGUI(UUID uuid) { return guiMap.get(uuid); }

    public static void startBingo(CMCEssentials cmc) {
        if (isRolling) {
            return;
        }

        Random random = new Random();

        task = Bukkit.getScheduler().runTaskTimer(cmc, ()->{
            currentMaterial = CasinoManager.getBingoMaterials().get(random.nextInt(CasinoManager.getBingoMaterials().size()));

            for (BingoGUI bingoGUI : guiMap.values()) {
                bingoGUI.updateMaterial();
            }
        }, 60, 100);

        isRolling = true;

    }

    public static void stopBingo() {
        isRolling = false;
        task.cancel();
        currentMaterial = null;
    }
    public static Material getRandMaterial() { return currentMaterial; }

    public static Collection<BingoGUI> getGUIS() { return guiMap.values(); }

}
