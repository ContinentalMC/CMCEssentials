package com.yopal.continentalmc.gambling.machines.platforms;

import java.util.HashMap;
import java.util.UUID;

public class PlatformsGUIManager {

    private static HashMap<UUID, PlatformsGUI> guiMap = new HashMap<>();

    public static void addGUI(UUID uuid, PlatformsGUI gui) { guiMap.put(uuid, gui); }

    public static void removeGUI(UUID uuid) { guiMap.remove(uuid); }

    public static PlatformsGUI getGUI(UUID uuid) { return guiMap.get(uuid); }
}
