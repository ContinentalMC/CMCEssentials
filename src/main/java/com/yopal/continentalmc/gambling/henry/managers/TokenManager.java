package com.yopal.continentalmc.gambling.henry.managers;

import com.yopal.continentalmc.CMCEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;

public class TokenManager {
    

    public static HashMap<Integer, ItemStack> giveBasicToken(CMCEssentials cmc,  Player player) {
        ItemStack itemStack = new ItemStack(Material.GRAY_DYE);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "CMCasino Token");
        itemMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Level >> BASIC",
                ChatColor.GRAY + "Machines Allowed:",
                ChatColor.GRAY + " ■ Slots"
        ));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(cmc, "tokenType"), PersistentDataType.STRING, "basic");
        itemStack.setItemMeta(itemMeta);

        return player.getInventory().addItem(itemStack);
    }

    public static HashMap<Integer, ItemStack> giveGoodToken(CMCEssentials cmc, Player player) {
        ItemStack itemStack = new ItemStack(Material.PINK_DYE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "CMCasino Token");

        itemMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Level >> GOOD",
                ChatColor.GRAY + " ■ Impostor"
        ));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(cmc, "tokenType"), PersistentDataType.STRING, "good");
        itemStack.setItemMeta(itemMeta);

        return player.getInventory().addItem(itemStack);
    }

    public static HashMap<Integer, ItemStack> giveSpecialToken(CMCEssentials cmc, Player player) {
        ItemStack itemStack = new ItemStack(Material.MAGENTA_DYE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "CMCasino Token");

        itemMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Level >> SPECIAL",
                ChatColor.GRAY + "Machines Allowed:",
                ChatColor.GRAY + " ■ Horse Bet",
                ChatColor.GRAY + " ■ Platforms"
        ));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(cmc, "tokenType"), PersistentDataType.STRING, "special");
        itemStack.setItemMeta(itemMeta);

        return player.getInventory().addItem(itemStack);
    }

    public static HashMap<Integer, ItemStack> giveInsaneToken(CMCEssentials cmc, Player player) {
        ItemStack itemStack = new ItemStack(Material.PURPLE_DYE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "CMCasino Token");

        itemMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Level >> INSANE",
                ChatColor.GRAY + "Machines Allowed:",
                ChatColor.GRAY + " ■ Rock Paper Scissors"
        ));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(cmc, "tokenType"), PersistentDataType.STRING, "insane");
        itemStack.setItemMeta(itemMeta);

        return player.getInventory().addItem(itemStack);
    }
}
