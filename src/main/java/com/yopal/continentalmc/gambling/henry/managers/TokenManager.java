package com.yopal.continentalmc.gambling.henry.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

public class TokenManager {

    public static HashMap<Integer, ItemStack> giveBasicToken(Player player) {
        ItemStack itemStack = new ItemStack(Material.GRAY_DYE);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "CMCasino Token");
        itemMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Level >> BASIC",
                ChatColor.GRAY + "Machines Allowed:",
                ChatColor.GRAY + " ■ Slots"
        ));
        itemStack.setItemMeta(itemMeta);

        return player.getInventory().addItem(itemStack);
    }

    public static HashMap<Integer, ItemStack> giveGoodToken(Player player) {
        ItemStack itemStack = new ItemStack(Material.PINK_DYE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "CMCasino Token");

        itemMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Level >> GOOD",
                ChatColor.GRAY + " ■ Diamond Cutting Perfection",
                ChatColor.GRAY + " ■ Impostor"
        ));
        itemStack.setItemMeta(itemMeta);

        return player.getInventory().addItem(itemStack);
    }

    public static HashMap<Integer, ItemStack> giveSpecialToken(Player player) {
        ItemStack itemStack = new ItemStack(Material.MAGENTA_DYE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "CMCasino Token");

        itemMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Level >> SPECIAL",
                ChatColor.GRAY + "Machines Allowed:",
                ChatColor.GRAY + " ■ Minecart Bet",
                ChatColor.GRAY + " ■ Platforms"
        ));
        itemStack.setItemMeta(itemMeta);

        return player.getInventory().addItem(itemStack);
    }

    public static HashMap<Integer, ItemStack> giveInsaneToken(Player player) {
        ItemStack itemStack = new ItemStack(Material.PURPLE_DYE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "CMCasino Token");

        itemMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Level >> INSANE",
                ChatColor.GRAY + "Machines Allowed:",
                ChatColor.GRAY + " ■ Rock Paper Scissors"
        ));
        itemStack.setItemMeta(itemMeta);

        return player.getInventory().addItem(itemStack);
    }
}
