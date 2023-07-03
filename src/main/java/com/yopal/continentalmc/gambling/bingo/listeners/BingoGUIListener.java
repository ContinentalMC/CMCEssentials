package com.yopal.continentalmc.gambling.bingo.listeners;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.bingo.instances.BingoGUI;
import com.yopal.continentalmc.gambling.bingo.managers.BingoGUIManager;
import com.yopal.continentalmc.gambling.managers.CelebrationManager;
import com.yopal.continentalmc.gambling.utils.PageUtil;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.Cuboid;
import com.yopal.continentalmc.utils.PlayerInteract;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BingoGUIListener implements Listener {

    private CMCEssentials cmc;

    public BingoGUIListener(CMCEssentials cmc) { this.cmc = cmc; }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        BingoGUI bingoGUI = BingoGUIManager.getGUI(e.getPlayer().getUniqueId());

        if (bingoGUI == null) {
            return;
        }

        BingoGUIManager.removeGUI(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        BingoGUI bingoGUI = BingoGUIManager.getGUI(e.getWhoClicked().getUniqueId());

        if (bingoGUI == null) {
            return;
        }

        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();

        switch (e.getSlot()) {
            case 34:
                player.closeInventory();
                break;
            case 21:
            case 22:
            case 23:
            case 30:
            case 31:
            case 32:
            case 39:
            case 40:
            case 41:
                if (!bingoGUI.getMaterials().contains(e.getCurrentItem().getType())) {
                    break;
                }

                if (e.getInventory().getItem(e.getSlot()).getItemMeta().getDisplayName().contains(ChatColor.GREEN.toString())) {
                    break;
                }

                PageUtil.updateDisplayName(bingoGUI.getInv(), e.getSlot(), ChatColor.GREEN + ChatColor.BOLD.toString() + e.getCurrentItem().getType().toString().replace("_", " "));
                PageUtil.updateLore(bingoGUI.getInv(), e.getSlot(), Arrays.asList(
                        ChatColor.GRAY + "This item has been acknowledged!"
                ));

                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1, 2);
                break;
            case 28:

                if (!bingoGUI.checkWin()) {
                    break;
                }

                for (BingoGUI gui : BingoGUIManager.getGUIS()) {
                    PlayerInteract.sendMessage(player, "Congratulations to " + ChatColor.GOLD + ChatColor.BOLD + e.getWhoClicked().getName() + ChatColor.GRAY + " for winning the bingo!");
                    gui.getPlayer().closeInventory();
                }

                BingoGUIManager.stopBingo();

                Economy econ = CMCEssentials.getEconomy();
                econ.depositPlayer(player, CasinoManager.getBingoTotal());
                PlayerInteract.sendMessage(player, ChatColor.GREEN + "$" + CasinoManager.getBingoTotal() + " has been added to your balance!");

                CasinoManager.resetBingoTotal(cmc);

                ArmorStand armorStand = (ArmorStand) Bukkit.getEntity(CasinoManager.getBingoStand());
                if (armorStand == null) {
                    return;
                }

                armorStand.setCustomName(ChatColor.GREEN + ChatColor.BOLD.toString() + "TOTAL POOL: " + CasinoManager.getBingoTotal());

                break;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (CasinoManager.getLoc1() == null || CasinoManager.getLoc2() == null) {
            return;
        }

        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }


        Cuboid cuboid = new Cuboid(CasinoManager.getLoc1(), CasinoManager.getLoc2());
        List<Block> blockList = new ArrayList<>();

        for (Block block : cuboid.getBlocks()) {
            if (block.getType().equals(Material.AIR)) {
                continue;
            }

            blockList.add(block);
        }

        if (!blockList.contains(e.getClickedBlock())) {
            return;
        }

        if (CasinoManager.getBingoTotal() < CasinoManager.getBingoMaxOut()) {
            PlayerInteract.sendInvalidUsage(e.getPlayer(), "It's not time for bingo yet! Total pool needs to be at least: " + CasinoManager.getBingoMaxOut());
            return;
        }

        BingoGUIManager.addGUI(e.getPlayer().getUniqueId(), new BingoGUI(e.getPlayer()));
    }


}
