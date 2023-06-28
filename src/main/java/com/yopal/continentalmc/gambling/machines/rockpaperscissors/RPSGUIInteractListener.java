package com.yopal.continentalmc.gambling.machines.rockpaperscissors;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import com.yopal.continentalmc.gambling.machines.managers.MachineGUIManager;
import com.yopal.continentalmc.gambling.machines.slots.SlotGUI;
import com.yopal.continentalmc.gambling.machines.slots.SlotGUIManager;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class RPSGUIInteractListener implements Listener {


    private CMCEssentials cmc;

    public RPSGUIInteractListener(CMCEssentials cmc) {
        this.cmc = cmc;
    }

    @EventHandler
    public void onPlayerInteract(InventoryClickEvent e) {

        HashMap<UUID, Location> machinesInUse = MachineGUIManager.getMachinesInUse();
        UUID uuid = e.getWhoClicked().getUniqueId();

        if (!machinesInUse.containsKey(uuid)) {
            return;
        }

        Block block = machinesInUse.get(uuid).getBlock();

        if (!CasinoManager.getMachineType(block).equals(MachineTypes.ROCKPAPERSCISSORS)) {
            return;
        }

        e.setCancelled(true);

        RPSGUI rpsgui = RPSGUIManager.getGUI(e.getWhoClicked().getUniqueId());

        RPSGUIType rpsguiType = rpsgui.getGUIType();
        Player player = (Player) e.getWhoClicked();

        switch (e.getSlot()) {
            case 12:
                if (rpsguiType == RPSGUIType.MAIN) {
                    break;
                }

                rpsgui.setSelection(RPSSelection.ROCK);
                rpsgui.openMainPage();

                player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 1);
                break;
            case 13:
                if (rpsguiType == RPSGUIType.SELECTION) {
                    rpsgui.setSelection(RPSSelection.PAPER);
                    rpsgui.openMainPage();

                    player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 1);
                } else if (rpsguiType == RPSGUIType.MAIN && e.getInventory().getItem(13).getItemMeta().getDisplayName().contains(ChatColor.GREEN.toString())) {
                    rpsgui.advance();
                    rpsgui.startAction();
                }
                break;
            case 14:
                if (rpsguiType == RPSGUIType.MAIN) {
                    break;
                }

                rpsgui.setSelection(RPSSelection.SCISSORS);
                rpsgui.openMainPage();

                player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 1);
                break;
            case 20:
                if (rpsgui.getIsCommittingAction()) {
                    break;
                }

                if (e.getInventory().getItem(20) == null) {
                    break;
                }

                if (e.getInventory().getItem(20).getItemMeta().getDisplayName().contains(ChatColor.BOLD.toString())) {
                    break;
                }

                rpsgui.openSelectionPage();
                break;
            case 31:
                player.closeInventory();
                break;
        }

    }
}
