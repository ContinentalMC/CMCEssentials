package com.yopal.continentalmc.gambling.machines.rockpaperscissors;

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
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.Random;

public class RPSGUI {
    private CMCEssentials cmc;
    private Player player;
    private int winPercentage;
    private Inventory inv;
    private RPSGUIType guiType;
    private RPSSelection selection;
    private int totalPool;
    private BukkitTask task;
    private boolean committingAction;
    private RPSComputerAction rpsComputerAction;
    private RPSSelection previousSelection;
    private int round;

    public RPSGUI(CMCEssentials cmc, Player player, int winPercentage, int totalPool, int round) {
        this.cmc = cmc;
        this.player = player;
        this.winPercentage = winPercentage;
        this.totalPool = totalPool;
        this.round = round;

        openMainPage();
    }

    public void openMainPage() {
        guiType = RPSGUIType.MAIN;
        inv = Bukkit.createInventory(player, 45, PlayerInteract.returnPrefix() + "RPS Machine");

        // FRAMES
        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setDisplayName(" ");
        frame.setItemMeta(frameMeta);
        PageUtil.createFrames(inv, frame,0, 44);

        // RETRY
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc1ZDNkYjAzZGMyMWU1NjNiMDM0MTk3ZGE0MzViNzllY2ZlZjRiOGUyZWNkYjczMGUzNzBjMzE2NjI5ZDM2ZiJ9fX0=", 13);
        PageUtil.updateDisplayName(inv, 13, ChatColor.RED + ChatColor.BOLD.toString() + "RETRY");
        PageUtil.updateLore(inv, 13, Arrays.asList(ChatColor.RED + "Please choose rock, paper, or scissors first!"));

        // CLOSE
        PageUtil.setItem(inv, ChatColor.RED + ChatColor.BOLD.toString() + "CLOSE", Material.BARRIER, Arrays.asList(
                ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To take your winnings!"
        ),  31);

        // MONEY BAG
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=", 22);
        PageUtil.updateDisplayName(inv, 22, ChatColor.GOLD + ChatColor.BOLD.toString() + "TOTAL MONEY");

        if (round == 1 && selection == null) {
            PageUtil.updateLore(inv, 22, Arrays.asList(ChatColor.GRAY.toString() + 0));
        } else {
            PageUtil.updateLore(inv, 22, Arrays.asList(ChatColor.GRAY.toString() + totalPool));
        }


        // PLAYER HEAD
        PageUtil.setPlayerSkull(inv, player, 19, Arrays.asList());

        // COMPUTER
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODYxNGUwYTcwZDFhZGNmOWVmODVmOWExM2MwODE3YzAyYjJjZjA3NzI5MWI3NTQxNGFlNTJkNDlkYTlmYWE5NSJ9fX0=", 25);
        PageUtil.updateDisplayName(inv, 25, ChatColor.YELLOW + "Computer");

        // CHOICES - Player
        if (selection == null) {
            PageUtil.setItem(inv, ChatColor.YELLOW + "Your Choice", Material.GRAY_WOOL, Arrays.asList(
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To select rock, paper, or scissors!"
            ), 20);
        } else {
            PageUtil.setItem(inv, ChatColor.YELLOW + ChatColor.BOLD.toString() + selection, getMaterial(selection), Arrays.asList(
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To select me!"
            ),  20);
        }

        // CHOICES - Computer
        if (selection == null) {
            PageUtil.setItem(inv, ChatColor.YELLOW + "Computer's Choice", Material.GRAY_WOOL, Arrays.asList(), 24);
        } else {
            PageUtil.setItem(inv, ChatColor.YELLOW + "Computer's Choice", getMaterial(makeComputerChoice()), Arrays.asList(), 24);
        }

        player.openInventory(inv);

    }

    private void setWinning() {
        CelebrationManager.summonFirework(player.getUniqueId(), Color.YELLOW, cmc, MachineTypes.ROCKPAPERSCISSORS);

        if (round != 5) {
            PageUtil.updateDisplayName(inv, 13, ChatColor.GREEN + ChatColor.BOLD.toString() + "RETRY");
            PageUtil.updateLore(inv, 13, Arrays.asList(
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "Click to play for " + ChatColor.GOLD + ChatColor.BOLD + "DOUBLE OR NOTHING" + ChatColor.GRAY + "."
            ));
        }

    }

    public void openSelectionPage() {
        guiType = RPSGUIType.SELECTION;

        inv = Bukkit.createInventory(player, 27, PlayerInteract.returnPrefix() + "RPS Machine");

        // FRAMES
        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setDisplayName(" ");
        frame.setItemMeta(frameMeta);
        PageUtil.createFrames(inv, frame,0, 26);

        // RPS
        int rpsInvSlot = 12;
        for (RPSSelection selection : Arrays.asList(RPSSelection.ROCK, RPSSelection.PAPER, RPSSelection.SCISSORS)) {

            PageUtil.setItem(inv, ChatColor.YELLOW + ChatColor.BOLD.toString() + selection, getMaterial(selection), Arrays.asList(
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To select me!"
            ),  rpsInvSlot);

            rpsInvSlot++;

        }
        player.openInventory(inv);
    }


    private void applyAction(String headTexture) {
        player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1.5f);
        PageUtil.setCustomSkull(inv, headTexture, 25);
        PageUtil.updateDisplayName(inv, 25, ChatColor.YELLOW + "Computer");

        task = Bukkit.getScheduler().runTaskLater(cmc, ()->{
            player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1.5f);
            PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODYxNGUwYTcwZDFhZGNmOWVmODVmOWExM2MwODE3YzAyYjJjZjA3NzI5MWI3NTQxNGFlNTJkNDlkYTlmYWE5NSJ9fX0=", 25);
            PageUtil.updateDisplayName(inv, 25, ChatColor.YELLOW + "Computer");

            committingAction = false;
        }, 20);
    }

    private Material getMaterial(RPSSelection sel) {
        switch (sel) {
            case ROCK:
                return Material.COBBLESTONE;
            case PAPER:
                return Material.PAPER;
            case SCISSORS:
                return Material.SHEARS;
        }
        return null;
    }

    private RPSSelection getStrength(RPSSelection sel) {
        switch (sel) {
            case ROCK:
                return RPSSelection.PAPER;
            case PAPER:
                return RPSSelection.SCISSORS;
            case SCISSORS:
                return RPSSelection.ROCK;
        }
        return null;
    }

    private RPSSelection getWeakness(RPSSelection sel) {
        switch (sel) {
            case ROCK:
                return RPSSelection.SCISSORS;
            case PAPER:
                return RPSSelection.ROCK;
            case SCISSORS:
                return RPSSelection.PAPER;
        }
        return null;
    }

    private boolean checkCorrectSelection() {
        Random random = new Random();
        int randNum = random.nextInt(1, 101);

        // EXCEPTION FOR ROUND ONE
        if (round == 1 && randNum <= winPercentage) {
            return true;
        } else if (round == 1) {
            return false;
        }

        /*
           CHANGEGRAY: select the same move you played last previous
           CHANGECYAN: select the weakness of what you played in the previous round
           CHANGERED: play the strength of what you chose in the previous round
         */
        switch (rpsComputerAction) {
            case CHANGEGRAY:
                if (previousSelection == selection && randNum <= winPercentage) {
                } else {
                    return false;
                }
            case CHANGECYAN:
                if (getWeakness(previousSelection) == selection && randNum <= winPercentage) {
                    return true;
                } else {
                    return false;
                }
            case CHANGERED:
                if (getStrength(previousSelection) == selection && randNum <= winPercentage) {
                    return true;
                } else {
                    return false;
                }
        }

        if (randNum <= winPercentage - CasinoManager.getRPSWinPercentageIncorrectDowngrade()) {
            return true;
        } else {
            return false;
        }

    }


    private RPSSelection makeComputerChoice() {
        if (checkCorrectSelection()) {
            setWinning();
            return getWeakness(selection);
        } else {
            PageUtil.updateLore(inv, 22, Arrays.asList(ChatColor.GRAY.toString() + 0));
            totalPool = 0;
            return getStrength(selection);
        }
    }

    public void startAction() {
        Random random = new Random();
        int randNum = random.nextInt(3);

        committingAction = true;

        switch (randNum) {
            case 0:
                applyAction("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjY3OGMzNTA5ZjhjYjE0YTc0MDhkMWVlYzRhMzg4NzU3YTEwMjMwNjMwOGM0YTgzNmE1NWU1Yjk4OGQ3NjllZSJ9fX0=");
                rpsComputerAction = RPSComputerAction.CHANGEGRAY;
                break;
            case 1:
                applyAction("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQxNjUzYWI1NDdmMjU4ODZhYjMwY2UyODE5MGRjZjE2Nzc2Yzg4ZDdmMjk2YWQ4YWE2MWEzNzlkY2JlZDNkOSJ9fX0=");
                rpsComputerAction = RPSComputerAction.CHANGECYAN;
                break;
            case 2:
                applyAction("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE2NzFiNjM3NGUzZjJlZTZhZDJlZDY0ZmQxOGI2N2RkNjQ3ZDc4M2UxNDYwZDkwYjQxMTliYWM0MzQ0Yzk2ZCJ9fX0=");
                rpsComputerAction = RPSComputerAction.CHANGERED;
                break;
        }

    }

    public void cancelAllTasks() {
        task.cancel();
    }

    public void advance() {
        totalPool = totalPool * 2;
        winPercentage = winPercentage - CasinoManager.getRPSWinPercentageDowngrade();
        round++;
        previousSelection = selection;
        selection = null;

        // RETRY
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc1ZDNkYjAzZGMyMWU1NjNiMDM0MTk3ZGE0MzViNzllY2ZlZjRiOGUyZWNkYjczMGUzNzBjMzE2NjI5ZDM2ZiJ9fX0=", 13);
        PageUtil.updateDisplayName(inv, 13, ChatColor.RED + ChatColor.BOLD.toString() + "RETRY");
        PageUtil.updateLore(inv, 13, Arrays.asList(ChatColor.RED + "Please choose rock, paper, or scissors first!"));

        // MONEY BAG
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=", 22);
        PageUtil.updateDisplayName(inv, 22, ChatColor.GOLD + ChatColor.BOLD.toString() + "TOTAL MONEY");
        PageUtil.updateLore(inv, 22, Arrays.asList(ChatColor.GRAY.toString() + totalPool));

        // CHOICES - Player
        PageUtil.setItem(inv, ChatColor.YELLOW + "Your Choice", Material.GRAY_WOOL, Arrays.asList(
                ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To select rock, paper, or scissors!"
        ), 20);

        // CHOICES - Computer
        PageUtil.setItem(inv, ChatColor.YELLOW + "Computer's Choice", Material.GRAY_WOOL, Arrays.asList(), 24);

    }

    public void giveTotalMoney() {
        if (round == 1 && selection == null) {
            totalPool = 0;
        }

        Economy econ = CMCEssentials.getEconomy();
        econ.depositPlayer(player, totalPool);
        PlayerInteract.sendMessage(player, ChatColor.GREEN + "$" + totalPool + " has been added to your balance!");

        if (totalPool == 0) {
            player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.RED + "Oof, you lost huh. Better luck next time!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        } else if (round == 5) {
            PlayerInteract.sendMessage(player, PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "AMAZING! You got the max pool of " + totalPool + ". Great job! Wanna try again?");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        } else {
            PlayerInteract.sendMessage(player, PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "WOW! You took a total pool of " + totalPool + ". Great job! Wanna try again?");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
        }


    }

    // setters
    public void setSelection(RPSSelection selection) { this.selection = selection; }


    // getters
    public boolean getIsCommittingAction() { return committingAction; }
    public RPSGUIType getGUIType() { return guiType; }
    public Inventory getInv() { return inv; }



}
