package com.yopal.continentalmc.gambling.machines.horsebet;

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
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.Random;

public class HorseBetGUI {

    private Player player;
    private CMCEssentials cmc;
    private int winPercentage;
    private boolean isRunning = false;
    private BukkitTask task1;
    private BukkitTask task2;
    private BukkitTask task3;
    private BukkitTask task4;


    public HorseBetGUI(CMCEssentials cmc, Player player, int winPercentage) {
        this.cmc = cmc;
        this.player = player;
        this.winPercentage = winPercentage;

        openMainPage();
    }

    private void openMainPage() {
        Inventory inv = Bukkit.createInventory(player, 54, PlayerInteract.returnPrefix() + "Horse Bet Machine");

        // frames
        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setDisplayName(" ");
        frame.setItemMeta(frameMeta);
        PageUtil.createFrames(inv, frame, 0, 53);

        // finish
        for (int i = 16; i < 52; i = i + 9) {
            PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU0YzU0ZDJjY2QzMmFlM2IzNmY1YmQwZTE0MmExMzYzZWY4MjFkMTgyNTU1YjRlMmNiNzY4MzY5NThiMjMyYyJ9fX0=", i);
            PageUtil.updateDisplayName(inv, i, ChatColor.YELLOW + ChatColor.BOLD.toString() + "FINISH");
        }

        // horses
        int horseNum = 0;
        for (int i = 10; i < 46; i = i + 9) {
            horseNum++;
            PageUtil.setItem(inv, ChatColor.YELLOW + "Horse #" + horseNum, Material.LEATHER_HORSE_ARMOR, Arrays.asList(
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "Me to place your bet!"
            ), i);
        }

        // retry
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc1ZDNkYjAzZGMyMWU1NjNiMDM0MTk3ZGE0MzViNzllY2ZlZjRiOGUyZWNkYjczMGUzNzBjMzE2NjI5ZDM2ZiJ9fX0=", 26);
        PageUtil.updateDisplayName(inv, 26, ChatColor.RED + ChatColor.BOLD.toString() + "RETRY");
        PageUtil.updateLore(inv, 26, Arrays.asList(ChatColor.RED + "Please bet on one of the horses first!"));

        // close
        PageUtil.setItem(inv, ChatColor.RED + ChatColor.BOLD.toString() + "CLOSE", Material.BARRIER, Arrays.asList(
                ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To close!"
        ), 35);

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

    public void startRunning(Inventory inv, int horseSlot) {
        isRunning = true;

        Random random = new Random();

        // getting the speeds
        int[] speeds = new int[]{0, 0, 0, 0};

        boolean win = checkWin();

        for (int i = 0; i < 4; i++) {
            if (win && i == ((horseSlot - 10)/9)) {
                speeds[i] = random.nextInt(15, 20);
            } else if (!win && i == (((horseSlot - 10)/9))) {
                speeds[i] = random.nextInt(50, 60);
            } else {
                speeds[i] = random.nextInt(30, 40);
            }
        }

        ItemStack horse1 = inv.getItem(10);
        ItemStack horse2 = inv.getItem(19);
        ItemStack horse3 = inv.getItem(28);
        ItemStack horse4 = inv.getItem(37);

        // horses
        task1 = Bukkit.getScheduler().runTaskTimer(cmc, ()->{
            int slot = inv.first(horse1);

            if (slot + 1 == 16) {
                provideResponse(inv, slot, win);
            }

            inv.setItem(slot + 1, horse1);
            inv.setItem(slot, null);

            player.playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP, 1, 1);
        }, 0, speeds[0]);

        task2 = Bukkit.getScheduler().runTaskTimer(cmc, ()->{
            int slot = inv.first(horse2);

            if (slot + 1 == 25) {
                provideResponse(inv, slot, win);
            }

            inv.setItem(slot + 1, horse2);
            inv.setItem(slot, null);

            player.playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP, 1, 1);
        }, 0, speeds[1]);

        task3 = Bukkit.getScheduler().runTaskTimer(cmc, ()->{
            int slot = inv.first(horse3);

            if (slot + 1 == 34) {
                provideResponse(inv, slot, win);
            }

            inv.setItem(slot + 1, horse3);
            inv.setItem(slot, null);

            player.playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP, 1, 1);
        }, 0, speeds[2]);

        task4 = Bukkit.getScheduler().runTaskTimer(cmc, ()->{
            int slot = inv.first(horse4);

            if (slot + 1 == 43) {
                provideResponse(inv, slot, win);
            }

            inv.setItem(slot + 1, horse4);
            inv.setItem(slot, null);

            player.playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP, 1, 1);
        }, 0, speeds[3]);

    }

    private void provideResponse(Inventory inv, int horseSlot, boolean win) {
        cancelAllTasks();

        isRunning = false;
        player.playSound(player.getLocation(), Sound.ENTITY_HORSE_AMBIENT, 1, 1);

        // changing retry head
        if (checkHasToken()) {
            PageUtil.updateDisplayName(inv, 26, ChatColor.GREEN + ChatColor.BOLD.toString() + "RETRY");
            PageUtil.updateLore(inv, 26, Arrays.asList(ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To retry the horse bet machine!"));
        } else {
            PageUtil.updateLore(inv, 26, Arrays.asList(ChatColor.RED + "You need a SPECIAL token to play again!"));
        }

        if (player != null && win) {
            CelebrationManager.summonFirework(player.getUniqueId(), Color.YELLOW, cmc, MachineTypes.HORSEBET);
        }

        if (win) {
            Economy econ = CMCEssentials.getEconomy();
            econ.depositPlayer(player, CasinoManager.getHorseBetWinning());

            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
            player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "I see you got a good eye! Wanna play again?");
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "Aw that's some bad luck. Maybe you'll bet the right horse next time!");
        }

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


    public boolean getIsRunning() { return isRunning; }

    public void cancelAllTasks() {
        task1.cancel();
        task2.cancel();
        task3.cancel();
        task4.cancel();
    }



}
