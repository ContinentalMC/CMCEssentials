package com.yopal.continentalmc.listeners;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.instances.PlayerWB;
import com.yopal.continentalmc.managers.WBManager;
import com.yopal.continentalmc.managers.YML.ConfigManager;
import com.yopal.continentalmc.managers.YML.ScoreManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class PlayerChatListener implements Listener {

    private CMCEssentials cmc;

    public PlayerChatListener(CMCEssentials cmc) {
        this.cmc = cmc;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        boolean messageDetected = false;
        for (String string : ConfigManager.getWelcomeMessages()) {
            if (e.getMessage().toLowerCase().contains(string)) {
                messageDetected = true;
                break;
            }
        }

        if (!messageDetected) {
            return;
        }

        PlayerWB playerWB = WBManager.getPlayerWB(player);

        if (playerWB == null) {
            return;
        }

        WBManager.removePlayerWB(playerWB);

        // adding money
        PlayerInteract.sendMessage(player, "Thank you for contributing to a friendly community! Here's $" + ConfigManager.getWelcomeAmount() + " for your efforts!");
        Economy econ = cmc.getEconomy();

        // adding karma
        UUID playerUUID = player.getUniqueId();
        PlayerInteract.sendMessage(player, ChatColor.LIGHT_PURPLE + "+3 Karma!");
        ScoreManager.setScore(cmc, playerUUID, ScoreManager.getScore(playerUUID) + 3);

        econ.depositPlayer(player, ConfigManager.getWelcomeAmount());
    }


}
