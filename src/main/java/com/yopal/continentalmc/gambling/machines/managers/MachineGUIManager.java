package com.yopal.continentalmc.gambling.machines.managers;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class MachineGUIManager {

    // NOTICE: Location is referencing the location of the machine, not the player
    private static HashMap<UUID, Location> machinesInUse = new HashMap<>();

    public static void addMachineInUse(UUID uuid, Location loc) {
        machinesInUse.put(uuid, loc);
    }

    public static void removeMachineInUse(UUID uuid) {
        machinesInUse.remove(uuid);
    }

    public static HashMap<UUID, Location> getMachinesInUse() {
        return machinesInUse;
    }

    public static boolean machineInUse(Location loc) {
        if (machinesInUse.values().contains(loc)) {
            return true;
        } else {
            return false;
        }
    }

}
