package com.yopal.continentalmc.gambling.machines.horsebet;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import com.yopal.continentalmc.gambling.machines.managers.MachineGUIManager;
import com.yopal.continentalmc.gambling.machines.slots.SlotGUI;
import com.yopal.continentalmc.gambling.machines.slots.SlotGUIManager;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class HorseBetGUIInteractListener implements Listener {

    private CMCEssentials cmc;

    public HorseBetGUIInteractListener(CMCEssentials cmc) {
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

        if (!CasinoManager.getMachineType(block).equals(MachineTypes.HORSEBET)) {
            return;
        }

        e.setCancelled(true);

        HorseBetGUI horseBetGUI = HorseBetGUIManager.getGUI(uuid);

        switch (e.getSlot()) {
            case 26:
                ItemStack head = e.getInventory().getItem(e.getSlot());
                String headName = head.getItemMeta().getDisplayName();
                if (headName.contains(ChatColor.RED.toString())) {
                    break;
                }

                HorseBetGUIManager.addGUI(e.getWhoClicked().getUniqueId(), new HorseBetGUI(cmc, (Player) e.getWhoClicked(), CasinoManager.getWinPercentage(block)));
                MachineGUIManager.addMachineInUse(e.getWhoClicked().getUniqueId(), block.getLocation());

                removeToken((Player) e.getWhoClicked());
                break;
            case 35:
                Bukkit.getPlayer(uuid).closeInventory();
                break;
            case 10:
            case 19:
            case 28:
            case 37:
                horseBetGUI.startRunning(e.getInventory(), e.getSlot());
                break;
        }

    }

    private void removeToken(Player player) {
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

            itemStack.setAmount(itemStack.getAmount() - 1);

        }

    }
}