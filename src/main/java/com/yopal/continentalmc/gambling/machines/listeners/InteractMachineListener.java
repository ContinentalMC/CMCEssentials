package com.yopal.continentalmc.gambling.machines.listeners;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import com.yopal.continentalmc.gambling.machines.impostor.instances.ImpostorGUI;
import com.yopal.continentalmc.gambling.machines.impostor.managers.ImpostorGUIManager;
import com.yopal.continentalmc.gambling.machines.managers.MachineGUIManager;
import com.yopal.continentalmc.gambling.machines.slots.instances.SlotGUI;
import com.yopal.continentalmc.gambling.machines.slots.managers.SlotGUIManager;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

public class InteractMachineListener implements Listener {

    private CMCEssentials cmc;

    public InteractMachineListener(CMCEssentials cmc) {
        this.cmc = cmc;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }

        if (!CasinoManager.isMachine(e.getClickedBlock())) {
            return;
        }

        Location blockLoc = e.getClickedBlock().getLocation();

        if (MachineGUIManager.machineInUse(blockLoc)) {
            PlayerInteract.sendInvalidUsage(e.getPlayer(), "This machine is already being used by another player!");
            return;
        }

        MachineTypes machineType = CasinoManager.getMachineType(e.getClickedBlock());
        NamespacedKey tokenTypeKey = new NamespacedKey(cmc, "tokenType");

        if (e.getItem() == null) {
            PlayerInteract.sendInvalidUsage(e.getPlayer(), "This machine requires a token!");
            return;
        }

        if (!e.getItem().getItemMeta().getPersistentDataContainer().has(tokenTypeKey, PersistentDataType.STRING)) {
            PlayerInteract.sendInvalidUsage(e.getPlayer(), "This machine requires a token!");
            return;
        }

        String tokenType = e.getItem().getItemMeta().getPersistentDataContainer().get(tokenTypeKey, PersistentDataType.STRING);

        switch (machineType) {
            case SLOTS:
                if (!tokenType.equalsIgnoreCase("basic")) {
                    PlayerInteract.sendInvalidUsage(e.getPlayer(), "You must have a BASIC token.");
                    return;
                }

                SlotGUIManager.addGUI(e.getPlayer().getUniqueId(), new SlotGUI(cmc, e.getPlayer(), CasinoManager.getWinPercentage(e.getClickedBlock())));
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                MachineGUIManager.addMachineInUse(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
                break;
            case IMPOSTOR:
                if (!tokenType.equalsIgnoreCase("good")) {
                    PlayerInteract.sendInvalidUsage(e.getPlayer(), "You must have a GOOD token.");
                    return;
                }

                ImpostorGUIManager.addGUI(e.getPlayer().getUniqueId() , new ImpostorGUI(cmc, e.getPlayer(), CasinoManager.getWinPercentage(e.getClickedBlock())));
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                MachineGUIManager.addMachineInUse(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
                break;
        }

    }
}
