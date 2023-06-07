package com.yopal.continentalmc.gambling.machines.slots.listeners;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import com.yopal.continentalmc.gambling.machines.managers.MachineGUIManager;
import com.yopal.continentalmc.gambling.machines.slots.instances.SlotGUI;
import com.yopal.continentalmc.gambling.machines.slots.managers.SlotGUIManager;
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

public class SlotGUIInteractListener implements Listener {

    private CMCEssentials cmc;

    public SlotGUIInteractListener(CMCEssentials cmc) {
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

        if (!CasinoManager.getMachineType(block).equals(MachineTypes.SLOTS)) {
            return;
        }

        e.setCancelled(true);

        switch (e.getSlot()) {
            case 9:
                Bukkit.getPlayer(uuid).closeInventory();
                break;
            case 17:
                ItemStack head = e.getInventory().getItem(e.getSlot());
                String headName = head.getItemMeta().getDisplayName();
                if (headName.contains(ChatColor.RED.toString())) {
                    break;
                }

                SlotGUIManager.addGUI(e.getWhoClicked().getUniqueId(), new SlotGUI(cmc, (Player) e.getWhoClicked(), CasinoManager.getWinPercentage(block)));
                MachineGUIManager.addMachineInUse(e.getWhoClicked().getUniqueId(), block.getLocation());

                removeToken((Player) e.getWhoClicked());
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

            if (!itemStack.getItemMeta().getPersistentDataContainer().get(tokenTypeKey, PersistentDataType.STRING).equalsIgnoreCase("good")) {
                continue;
            }

            itemStack.setAmount(itemStack.getAmount() - 1);

        }

    }

}
