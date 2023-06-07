package com.yopal.continentalmc.gambling.henry.listeners;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.henry.managers.HenryGUIManager;
import com.yopal.continentalmc.gambling.henry.managers.TokenManager;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.managers.YML.ConfigManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TokenBuyListener implements Listener {

    private CMCEssentials cmc;
    public TokenBuyListener(CMCEssentials cmc) {
        this.cmc = cmc;
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {

        HashMap<String, Integer> hashMap = CasinoManager.getCoinPrices();

        Economy econ = cmc.getEconomy();
        Player player = (Player) e.getWhoClicked();
        Double playerBal = econ.getBalance(player);

        if (HenryGUIManager.getGUI((Player) e.getWhoClicked()) == null) {
            return;
        }

        String insufficientFunds = PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.RED + "Insufficient funds!";
        String fullInventory = PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.RED + "Your inventory is too full to receive a token!";

        switch (e.getSlot()) {
            case 10:
                if (playerBal < hashMap.get("basic-coin")) {
                    player.sendMessage(insufficientFunds);
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    break;
                }

                HashMap<Integer, ItemStack> extraItems1 = TokenManager.giveBasicToken(cmc, player);

                if (!extraItems1.isEmpty()) {
                    player.sendMessage(fullInventory);
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    break;
                }

                econ.withdrawPlayer(player, hashMap.get("basic-coin"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 2);

                break;
            case 12:
                if (playerBal < hashMap.get("good-coin")) {
                    player.sendMessage(insufficientFunds);
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    break;
                }

                HashMap<Integer, ItemStack> extraItems2 = TokenManager.giveGoodToken(cmc, player);

                if (!extraItems2.isEmpty()) {
                    player.sendMessage(fullInventory);
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    break;
                }

                econ.withdrawPlayer(player, hashMap.get("good-coin"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 2);

                break;
            case 14:
                if (playerBal < hashMap.get("special-coin")) {
                    player.sendMessage(insufficientFunds);
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    break;
                }

                HashMap<Integer, ItemStack> extraItems3 = TokenManager.giveSpecialToken(cmc, player);

                if (!extraItems3.isEmpty()) {
                    player.sendMessage(fullInventory);
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    break;
                }

                econ.withdrawPlayer(player, hashMap.get("special-coin"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 2);

                break;
            case 16:
                if (playerBal < hashMap.get("insane-coin")) {
                    player.sendMessage(insufficientFunds);
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    break;
                }

                HashMap<Integer, ItemStack> extraItems4 = TokenManager.giveInsaneToken(cmc, player);

                if (!extraItems4.isEmpty()) {
                    player.sendMessage(fullInventory);
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    break;
                }

                econ.withdrawPlayer(player, hashMap.get("insane-coin"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 2);

                break;
        }

        e.setCancelled(true);
    }
}

