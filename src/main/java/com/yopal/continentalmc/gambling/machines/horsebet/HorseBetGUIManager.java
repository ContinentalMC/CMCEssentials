package com.yopal.continentalmc.gambling.machines.horsebet;

import java.util.HashMap;
import java.util.UUID;

public class HorseBetGUIManager {

    private static HashMap<UUID, HorseBetGUI> guiMap = new HashMap<>();

    public static void addGUI(UUID uuid, HorseBetGUI gui) { guiMap.put(uuid, gui); }

    public static void removeGUI(UUID uuid) { guiMap.remove(uuid); }

    public static HorseBetGUI getGUI(UUID uuid) { return guiMap.get(uuid); }
}
