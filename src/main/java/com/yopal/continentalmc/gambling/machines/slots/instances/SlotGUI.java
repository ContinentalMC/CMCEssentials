package com.yopal.continentalmc.gambling.machines.slots.instances;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.managers.CelebrationManager;
import com.yopal.continentalmc.gambling.utils.PageUtil;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SlotGUI {

    private CMCEssentials cmc;
    private Player player;
    private int winPercentage;
    private List<Material> materialList = Arrays.asList(
            Material.COAL,
            Material.IRON_INGOT,
            Material.LAPIS_LAZULI,
            Material.EMERALD,
            Material.DIAMOND
    );

    public SlotGUI(CMCEssentials cmc, Player player, int winPercentage){
        this.cmc = cmc;
        this.player = player;
        this.winPercentage = winPercentage;

        openMainPage();

    }

    private List<Material> getWinningMaterials() {
        Random random = new Random();
        int winNum = random.nextInt(1, 201);

        if (winNum <= 10) {
            return Arrays.asList(Material.DIAMOND, Material.DIAMOND, Material.DIAMOND);
        } else if (winNum > 10 && winNum <= 30) {
            return Arrays.asList(Material.EMERALD, Material.EMERALD, Material.EMERALD);
        } else if (winNum > 30 && winNum <= 70) {
            return Arrays.asList(Material.LAPIS_LAZULI, Material.LAPIS_LAZULI, Material.LAPIS_LAZULI);
        } else if (winNum > 70 && winNum <= 130) {
            return Arrays.asList(Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT);
        } else if (winNum > 130 && winNum < 200) {
            return Arrays.asList(Material.COAL, Material.COAL, Material.COAL);
        }

        return null;
    }

    private List<Material> getLosingMaterials() {
        Random random = new Random();
        int matNum1 = random.nextInt(materialList.size());
        int matNum2;
        int matNum3;

        int chanceOfBeingSame = random.nextInt(3);

        // getting matNum2
        while (true) {
            matNum2 = random.nextInt(materialList.size());
            if (chanceOfBeingSame > 0) {
                matNum2 = matNum1;
                break;
            }

            if (matNum1 != matNum2) {
                break;
            }
        }

        // getting matNum3
        while (true) {
            matNum3 = random.nextInt(materialList.size());

            if (chanceOfBeingSame > 0 && matNum1 != matNum3) {
                break;
            } else if (matNum1 != matNum2 && matNum1 != matNum3) {
                break;
            }
        }

        return Arrays.asList(materialList.get(matNum1), materialList.get(matNum2), materialList.get(matNum3));

    }

    private boolean getWin() {
        Random random = new Random();
        int winNum = random.nextInt(101);

        if (winNum <= winPercentage) {
            return true;
        } else {
            return false;
        }
    }

    public void openMainPage() {
        Inventory inv = Bukkit.createInventory(player, 27, PlayerInteract.returnPrefix() + "Slot Machine");
        PageUtil.createRainbowFrames(cmc, inv, 0, 26);

        // CLOSE
        PageUtil.setItem(inv, ChatColor.RED + ChatColor.BOLD.toString() + "CLOSE", Material.BARRIER, Arrays.asList(
                ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To close!"
        ), 9);

        // REROLL
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc1ZDNkYjAzZGMyMWU1NjNiMDM0MTk3ZGE0MzViNzllY2ZlZjRiOGUyZWNkYjczMGUzNzBjMzE2NjI5ZDM2ZiJ9fX0=", 17);
        PageUtil.updateDisplayName(inv, 17, ChatColor.RED + ChatColor.BOLD.toString() + "RE-ROLL");
        PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.RED + "Please wait until this machine has stopped rolling!"));

        PageUtil.cycleItemsInSlot(cmc, inv, 12, materialList, player, 20);
        PageUtil.cycleItemsInSlot(cmc, inv, 13, materialList, player, 55);
        PageUtil.cycleItemsInSlot(cmc, inv, 14, materialList, player, 90);

        List<Material> materials;
        boolean win = getWin();
        if (win) {
            materials = getWinningMaterials();
        } else {
            materials = getLosingMaterials();
        }

        player.openInventory(inv);

        Bukkit.getScheduler().runTaskLaterAsynchronously(cmc, ()->{
            inv.setItem(12, new ItemStack(materials.get(0)));
            player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1.25f);
        }, 20);

        Bukkit.getScheduler().runTaskLaterAsynchronously(cmc, ()->{
            inv.setItem(13, new ItemStack(materials.get(1)));
            player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1.25f);
        }, 55);

        Bukkit.getScheduler().runTaskLaterAsynchronously(cmc, ()->{
            inv.setItem(14, new ItemStack(materials.get(2)));
            player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1.25f);
        }, 90);

        Bukkit.getScheduler().runTaskLater(cmc, ()-> {

            if (checkHasToken()) {
                PageUtil.updateDisplayName(inv, 17, ChatColor.GREEN + ChatColor.BOLD.toString() + "RE-ROLL");
                PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To re-roll the slot machine!"));
            } else {
                player.closeInventory();
            }

            provideResponse(materials, win, player);
        }, 130);

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

            if (!itemStack.getItemMeta().getPersistentDataContainer().get(tokenTypeKey, PersistentDataType.STRING).equalsIgnoreCase("basic")) {
                continue;
            }

            return true;
        }

        return false;

    }

    private void provideResponse(List<Material> materials, boolean win, Player player) {
        if (!win) {
            player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.RED + "You didn't win anything unfortunately. Better luck next time!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        if (player != null) {
            CelebrationManager.summonFireworkSlot(player.getUniqueId(), materials.get(0), cmc);
        }

        Economy econ = CMCEssentials.getEconomy();

        // work on giving configurable money to players for each prize
        switch (materials.get(0)) {
            case DIAMOND:
                player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "Wow aren't you lucky?! You got the highest prize! You should test out your luck again :)");
                break;
            case EMERALD:
                player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "Nice! Ready to play again?");
                break;
            case LAPIS_LAZULI:
                player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "Good job! Want to try again?");
                break;
            case IRON_INGOT:
                player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "Gooood. I think you can get a better prize by trying again.");
                break;
            case COAL:
                player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "Ehhh, not much but at least it's not nothing!");
                break;
        }

        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
        econ.depositPlayer(player, CasinoManager.getSlotWinning(materials.get(0)));
    }



}
