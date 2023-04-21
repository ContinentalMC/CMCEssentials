package com.yopal.continentalmc.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.instances.PlayerWB;
import com.yopal.continentalmc.managers.WBManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerJoinListener implements Listener {

    private Cache<UUID, Long> joinCooldown = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
    private CMCEssentials cmc;

    public PlayerJoinListener(CMCEssentials cmc) {
        this.cmc = cmc;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (WBManager.checkPlayer(e.getPlayer())) {
            return;
        }

        if (!checkCooldown(e.getPlayer())) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == e.getPlayer()) {
                continue;
            }
            WBManager.addPlayerWB(new PlayerWB(e.getPlayer().getUniqueId(), player.getUniqueId()));
        }

        Bukkit.getScheduler().runTaskLater(cmc, ()->{
            WBManager.removePlayerThatJoined(e.getPlayer());
        }, 200);

    }

    public boolean checkCooldown(Player player) {
        if (!joinCooldown.asMap().containsKey(player.getUniqueId())) {
            joinCooldown.put(player.getUniqueId(), System.currentTimeMillis() + 10000);
            return true;
        } else {
            return false;
        }
    }

}
