package com.yopal.continentalmc.gambling.machines.listeners;

import com.yopal.continentalmc.gambling.machines.impostor.instances.ImpostorGUI;
import com.yopal.continentalmc.gambling.machines.impostor.managers.ImpostorGUIManager;
import com.yopal.continentalmc.gambling.machines.managers.MachineGUIManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.UUID;

public class MachineCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        HashMap<UUID, Location> machinesInUse = MachineGUIManager.getMachinesInUse();

        if (!machinesInUse.containsKey(e.getPlayer().getUniqueId())) {
            return;
        }

        MachineGUIManager.removeMachineInUse(e.getPlayer().getUniqueId());

        // IMPOSTOR GUI
        ImpostorGUI impostorGUI = ImpostorGUIManager.getGUI(e.getPlayer().getUniqueId());

        if (impostorGUI == null) {
            return;
        }

        ImpostorGUIManager.removeGUI(e.getPlayer().getUniqueId());

    }
}
