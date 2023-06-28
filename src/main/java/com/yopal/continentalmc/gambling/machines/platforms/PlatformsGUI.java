package com.yopal.continentalmc.gambling.machines.platforms;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import com.yopal.continentalmc.gambling.managers.CelebrationManager;
import com.yopal.continentalmc.gambling.utils.PageUtil;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Random;

public class PlatformsGUI {
    private CMCEssentials cmc;
    private Player player;
    private int winPercentage;
    private Inventory inv;
    private boolean isCrossing = true;
    private int platformRow = 0;
    private ItemStack playerHead;
    public PlatformsGUI(CMCEssentials cmc, Player player, int winPercentage) {
        this.cmc = cmc;
        this.player = player;
        this.winPercentage = winPercentage;

        openMainPage();
    }

    private void openMainPage() {
        inv = Bukkit.createInventory(player, 36, PlayerInteract.returnPrefix() + "Platforms Machine");

        // FRAMES
        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setDisplayName(" ");
        frame.setItemMeta(frameMeta);
        PageUtil.createFrames(inv, frame,0, 35);

        // PLATFORMS
        for (int i = 11; i < 16; i++) {
            PageUtil.setItem(inv, ChatColor.YELLOW + ChatColor.BOLD.toString() + "Platform", Material.OAK_SLAB, Arrays.asList(
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To select this platform!"
            ), i);
        }

        for (int i = 20; i < 25; i++) {
            PageUtil.setItem(inv, ChatColor.YELLOW + ChatColor.BOLD.toString() + "Platform", Material.OAK_SLAB, Arrays.asList(
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To select this platform!"
            ), i);
        }

        // PLAYER HEAD
        playerHead = PageUtil.setPlayerSkull(inv, player, 10, Arrays.asList(ChatColor.GRAY + "I need to get to the other side!"));

        // RETRY
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc1ZDNkYjAzZGMyMWU1NjNiMDM0MTk3ZGE0MzViNzllY2ZlZjRiOGUyZWNkYjczMGUzNzBjMzE2NjI5ZDM2ZiJ9fX0=", 17);
        PageUtil.updateDisplayName(inv, 17, ChatColor.RED + ChatColor.BOLD.toString() + "RETRY");
        PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.RED + "Please click a platform first!"));

        // CLOSE
        PageUtil.setItem(inv, ChatColor.RED + ChatColor.BOLD.toString() + "CLOSE", Material.BARRIER, Arrays.asList(
                ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To close!"
        ),  26);

        player.openInventory(inv);
    }

    public boolean checkWin() {
        Random random = new Random();
        int winNum = random.nextInt(1, 101);

        if (winNum <= winPercentage) {
            return true;
        } else {
            return false;
        }
    }

    private int getCorrespondingPlatform(int platformNum) {
        if (platformNum >= 20) {
            return platformNum - 9;
        } else {
            return platformNum + 9;
        }
    }

    private void setLosing(int invSlot) {
        isCrossing = false;

        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_TRAPDOOR_CLOSE, 1, 1);
        PageUtil.setItem(inv, ChatColor.RED + ChatColor.BOLD.toString() + "YOU DEAD", Material.SKELETON_SKULL, Arrays.asList(ChatColor.GRAY + "Bummer, you seemed to have fallen into the trip"), invSlot);

        player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.RED + "Aw man you fell down the trap! You should try again anyways!");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);

        // changing retry head
        if (checkHasToken()) {
            PageUtil.updateDisplayName(inv, 17, ChatColor.GREEN + ChatColor.BOLD.toString() + "RETRY");
            PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To retry the platforms machine!"));
        } else {
            PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.RED + "You need a SPECIAL token to play again!"));
        }

    }

    private void setWinning() {
        isCrossing = false;

        CelebrationManager.summonFirework(player.getUniqueId(), Color.YELLOW.YELLOW, cmc, MachineTypes.PLATFORMS);

        PlayerInteract.sendMessage(player, PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "Wow you got across? You're very lucky! You should try again to test your luck.");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);

        Economy econ = CMCEssentials.getEconomy();
        econ.depositPlayer(player, CasinoManager.getPlatformsWinning());

        // changing retry head
        if (checkHasToken()) {
            PageUtil.updateDisplayName(inv, 17, ChatColor.GREEN + ChatColor.BOLD.toString() + "RETRY");
            PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To retry the platforms machine!"));
        } else {
            PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.RED + "You need a SPECIAL token to play again!"));
        }
    }

    public void advancePlayer(int invSlot) {

        boolean win = checkWin();

        if ((win && (invSlot + 1) != 16) && (win && (invSlot + 1) != 25)) {
            PageUtil.setItem(inv, ChatColor.YELLOW + ChatColor.BOLD.toString() + "Platform", Material.OAK_SLAB, Arrays.asList(), inv.first(playerHead));
            PageUtil.setPlayerSkull(inv, player, invSlot, Arrays.asList(ChatColor.GRAY + "I need to get to the other side!"));
            player.playSound(player.getLocation(), Sound.BLOCK_WOOD_STEP, 1, 1);

            PageUtil.setItem(inv, ChatColor.RED + ChatColor.BOLD.toString() + "TRAP", Material.BARRIER, Arrays.asList(ChatColor.GRAY + "This would've been your demise..."), getCorrespondingPlatform(invSlot));

        } else if ((win && (invSlot + 1) == 16) || (win && (invSlot + 1) == 25)) {
            PageUtil.setItem(inv, ChatColor.YELLOW + ChatColor.BOLD.toString() + "Platform", Material.OAK_SLAB, Arrays.asList(), inv.first(playerHead));
            PageUtil.setPlayerSkull(inv, player, invSlot, Arrays.asList(ChatColor.GRAY + "I need to get to the other side!"));
            player.playSound(player.getLocation(), Sound.BLOCK_WOOD_STEP, 1, 1);

            PageUtil.setItem(inv, ChatColor.RED + ChatColor.BOLD.toString() + "TRAP", Material.BARRIER, Arrays.asList(ChatColor.GRAY + "This would've been your demise..."), getCorrespondingPlatform(invSlot));

            setWinning();
        } else {
            setLosing(invSlot);
        }

        if (inv.getItem(10) != null) {
            inv.setItem(10, null);
        }

        platformRow++;

    }

    private boolean checkHasToken() {

        NamespacedKey tokenTypeKey = new NamespacedKey(cmc, "tokenType");

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null) {
                continue;
            }

            if (!itemStack.hasItemMeta()) {
                continue;
            }

            if (!itemStack.getItemMeta().getPersistentDataContainer().has(tokenTypeKey, PersistentDataType.STRING)) {
                continue;
            }

            if (!itemStack.getItemMeta().getPersistentDataContainer().get(tokenTypeKey, PersistentDataType.STRING).equalsIgnoreCase("special")) {
                continue;
            }

            return true;
        }

        return false;

    }


    public int getPlatformRow() { return platformRow; }
    public boolean getIsCrossing() { return isCrossing; }

}
