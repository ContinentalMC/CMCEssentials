package com.yopal.continentalmc.gambling.machines.managers;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class MachineGUIManager {

    // NOTICE: Location is referencing the location of the machine, not the player
    private static HashMap<UUID, Location> machinesInUse = new HashMap<>();

    public static void addMachineInUse(UUID uuid, Location loc) {

    }
}
