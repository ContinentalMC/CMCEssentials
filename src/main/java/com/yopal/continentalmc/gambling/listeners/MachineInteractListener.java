package com.yopal.continentalmc.gambling.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class MachineInteractListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) { return; }


    }

}
