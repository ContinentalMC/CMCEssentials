package com.yopal.continentalmc.gambling.henry.listeners;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.henry.instances.HenryGUI;
import com.yopal.continentalmc.gambling.henry.managers.HenryGUIManager;
import com.yopal.continentalmc.gambling.henry.managers.HenryManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerInteractHenryListener implements Listener {

    private CMCEssentials cmc;

    public PlayerInteractHenryListener(CMCEssentials cmc) {
        this.cmc = cmc;
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEntityEvent e) {
        if (!e.getRightClicked().getType().equals(EntityType.VILLAGER)) {
            return;
        }

        if (!e.getRightClicked().getPersistentDataContainer().has(new NamespacedKey(cmc, "casinoHandler"), PersistentDataType.STRING)) {
            return;
        }

        HenryGUIManager.addGUI(new HenryGUI(cmc, e.getPlayer()));
        HenryManager.welcomePlayer(e.getPlayer());
        e.setCancelled(true);
    }
}
