package com.yopal.continentalmc.gambling.machines.slots.managers;

import com.yopal.continentalmc.gambling.machines.impostor.instances.ImpostorGUI;
import com.yopal.continentalmc.gambling.machines.slots.instances.SlotGUI;

import java.util.HashMap;
import java.util.UUID;

public class SlotGUIManager {

    private static HashMap<UUID, SlotGUI> guiMap = new HashMap<>();

    public static void addGUI(UUID uuid, SlotGUI gui) { guiMap.put(uuid, gui); }

    public static void removeGUI(UUID uuid) { guiMap.remove(uuid); }

    public static SlotGUI getGUI(UUID uuid) { return guiMap.get(uuid); }

}