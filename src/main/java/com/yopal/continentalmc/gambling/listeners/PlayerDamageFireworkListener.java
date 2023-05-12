package com.yopal.continentalmc.gambling.listeners;

import com.yopal.continentalmc.CMCEssentials;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerDamageFireworkListener implements Listener {

    private CMCEssentials cmc;
    public PlayerDamageFireworkListener(CMCEssentials cmc) {
        this.cmc = cmc;
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Firework)) {
            return;
        }

        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Firework firework = (Firework) e.getDamager();

        if (!firework.getPersistentDataContainer().has(new NamespacedKey(cmc, "fireworkMachine"), PersistentDataType.STRING)) {
            return;
        }

        e.setCancelled(true);
    }
}
