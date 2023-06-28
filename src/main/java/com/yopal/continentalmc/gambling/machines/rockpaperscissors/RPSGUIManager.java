package com.yopal.continentalmc.gambling.machines.rockpaperscissors;

import com.yopal.continentalmc.gambling.machines.slots.SlotGUI;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

public class RPSGUIManager {

    private static HashMap<UUID, RPSGUI> guiMap = new HashMap<>();

    public static void addGUI(UUID uuid, RPSGUI gui) { guiMap.put(uuid, gui); }

    public static void removeGUI(UUID uuid) { guiMap.remove(uuid); }

    public static RPSGUI correlateGUI(Inventory inv) {
        for (RPSGUI gui : guiMap.values()) {
            if (gui.getInv() == inv) {
                return gui;
            }
        }

        return null;
    }

    public static RPSGUI getGUI(UUID uuid) { return guiMap.get(uuid); }

}
