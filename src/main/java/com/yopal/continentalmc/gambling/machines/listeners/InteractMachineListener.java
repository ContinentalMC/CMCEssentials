package com.yopal.continentalmc.gambling.machines.listeners;

import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractMachineListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }

        if (CasinoManager.isMachine(e.getClickedBlock())) {
            return;
        }


    }
}
