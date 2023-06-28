package com.yopal.continentalmc.gambling.machines.impostor;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import com.yopal.continentalmc.gambling.machines.managers.MachineGUIManager;
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
import java.util.List;
import java.util.UUID;

public class ImpostorGUIInteractListener implements Listener {
    private CMCEssentials cmc;

    public ImpostorGUIInteractListener(CMCEssentials cmc) {
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

        if (!CasinoManager.getMachineType(block).equals(MachineTypes.IMPOSTOR)) {
            return;
        }

        e.setCancelled(true);

        switch (e.getSlot()) {
            case 9:
                Bukkit.getPlayer(uuid).closeInventory();
                return;
            case 17:
                ItemStack head = e.getInventory().getItem(e.getSlot());
                List<String> headLore = head.getItemMeta().getLore();

                if (headLore.get(0).contains(ChatColor.RED.toString())) {
                    return;
                }

                ImpostorGUIManager.addGUI(e.getWhoClicked().getUniqueId() , new ImpostorGUI(cmc, (Player) e.getWhoClicked(), CasinoManager.getWinPercentage(block)));
                MachineGUIManager.addMachineInUse(e.getWhoClicked().getUniqueId(), block.getLocation());

                removeToken((Player) e.getWhoClicked());
                return;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:

                head = e.getInventory().getItem(17);
                headLore = head.getItemMeta().getLore();
                if (headLore.get(0).contains(ChatColor.GREEN.toString())) {
                    return;
                } else if (headLore.get(0).contains(ChatColor.RED + "You need a GOOD token to play again!")) {
                    return;
                }

                break;
            default:
                return;
        }

        ImpostorGUI impostorGUI = ImpostorGUIManager.getGUI(uuid);

        boolean regularWin = impostorGUI.checkWin();
        boolean goldenWin = false;

        if (regularWin) {
            goldenWin = impostorGUI.checkGoldenWin();
        }

        if (goldenWin) {
            impostorGUI.setGoldenWinning(e.getSlot());
        } else if (regularWin) {
            impostorGUI.setWinning(e.getSlot());
        } else {
            impostorGUI.setLosing(e.getSlot());
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
