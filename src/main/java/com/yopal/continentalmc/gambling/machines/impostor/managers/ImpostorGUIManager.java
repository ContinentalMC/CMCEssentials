package com.yopal.continentalmc.gambling.machines.impostor.managers;

import com.yopal.continentalmc.gambling.machines.impostor.instances.ImpostorGUI;

import java.util.HashMap;
import java.util.UUID;

public class ImpostorGUIManager {

    private static HashMap<UUID, ImpostorGUI> guiMap = new HashMap<>();

    public static void addGUI(UUID uuid, ImpostorGUI gui) { guiMap.put(uuid, gui); }

    public static void removeGUI(UUID uuid) { guiMap.remove(uuid); }

    public static ImpostorGUI getGUI(UUID uuid) { return guiMap.get(uuid); }

}
