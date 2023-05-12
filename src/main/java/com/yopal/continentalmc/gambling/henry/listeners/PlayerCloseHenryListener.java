package com.yopal.continentalmc.gambling.henry.listeners;

import com.yopal.continentalmc.gambling.henry.instances.HenryGUI;
import com.yopal.continentalmc.gambling.henry.managers.HenryGUIManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class PlayerCloseHenryListener implements Listener {
    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        HenryGUI gui = HenryGUIManager.getGUI((Player) e.getPlayer());

        if (gui == null) {
            return;
        }

        HenryGUIManager.removeGUI(gui);

    }
}
