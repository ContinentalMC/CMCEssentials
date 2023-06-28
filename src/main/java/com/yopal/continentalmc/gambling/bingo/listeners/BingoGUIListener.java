package com.yopal.continentalmc.gambling.bingo.listeners;

import com.yopal.continentalmc.gambling.bingo.instances.BingoGUI;
import com.yopal.continentalmc.gambling.bingo.managers.BingoGUIManager;
import com.yopal.continentalmc.gambling.utils.PageUtil;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.Cuboid;
import com.yopal.continentalmc.utils.PlayerInteract;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BingoGUIListener implements Listener {

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

                PageUtil.updateDisplayName(bingoGUI.getInv(), e.getSlot(), ChatColor.GREEN + ChatColor.BOLD.toString() + e.getCurrentItem().getType());
                PageUtil.updateLore(bingoGUI.getInv(), e.getSlot(), Arrays.asList(
                        ChatColor.GRAY + "This item has been acknowledged!"
                ));

                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1, 2);
                break;
            case 28:

                break;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
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

        BingoGUIManager.addGUI(e.getPlayer().getUniqueId(), new BingoGUI(e.getPlayer()));
    }

}
