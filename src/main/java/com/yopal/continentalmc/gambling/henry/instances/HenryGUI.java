package com.yopal.continentalmc.gambling.henry.instances;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.utils.PageUtil;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

public class HenryGUI {
    private CMCEssentials cmc;
    private Player player;
    public HenryGUI(CMCEssentials cmc, Player player) {
        this.cmc = cmc;
        this.player = player;

        openMainPage();
    }

    public void openMainPage() {
        Inventory inv = Bukkit.createInventory(player, 27, PlayerInteract.returnPrefix() + "Slot Machine");
        PageUtil.createFrames(inv, new ItemStack(Material.GREEN_STAINED_GLASS_PANE), 0, 26);

        String clickToBuyOne = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Click" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "To Purchase One Token!";

        HashMap<String, Integer> coinPrices = CasinoManager.getCoinPrices();

        // coins
        PageUtil.setItem(inv, ChatColor.GOLD + ChatColor.BOLD.toString() + "Basic CMCasino Token", Material.GRAY_DYE, Arrays.asList(
            clickToBuyOne,
            ChatColor.GRAY + "Cost: $" + coinPrices.get("basic-coin"),
            ChatColor.GRAY + "Machines Allowed:",
            ChatColor.GRAY + " ■ Slots"
        ), 10);

        PageUtil.setItem(inv, ChatColor.GOLD + ChatColor.BOLD.toString() + "Good CMCasino Token", Material.PINK_DYE, Arrays.asList(
                clickToBuyOne,
                ChatColor.GRAY + "Cost: $" + coinPrices.get("good-coin"),
                ChatColor.GRAY + "Machines Allowed:",
                ChatColor.GRAY + " ■ Impostor"
        ), 12);

        PageUtil.setItem(inv, ChatColor.GOLD + ChatColor.BOLD.toString() + "Special CMCasino Token", Material.MAGENTA_DYE, Arrays.asList(
                clickToBuyOne,
                ChatColor.GRAY + "Cost: $" + coinPrices.get("special-coin"),
                ChatColor.GRAY + "Machines Allowed:",
                ChatColor.GRAY + " ■ Horse Bet",
                ChatColor.GRAY + " ■ Platforms"
        ), 14);

        PageUtil.setItem(inv, ChatColor.GOLD + ChatColor.BOLD.toString() + "Insane CMCCasino Token", Material.PURPLE_DYE, Arrays.asList(
                clickToBuyOne,
                ChatColor.GRAY + "Cost: $" + coinPrices.get("insane-coin"),
                ChatColor.GRAY + "Machines Allowed:",
                ChatColor.GRAY + " ■ Rock Paper Scissors"
        ), 16);

        player.openInventory(inv);
    }

    // getters
    public Player getPlayer() { return player; }

}
