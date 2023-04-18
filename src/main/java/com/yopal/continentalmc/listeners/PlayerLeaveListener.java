package com.yopal.continentalmc.listeners;

import com.yopal.continentalmc.managers.WBManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (!WBManager.checkPlayer(e.getPlayer())) {
            return;
        }
        WBManager.removePlayerThatJoined(e.getPlayer());
    }

}
