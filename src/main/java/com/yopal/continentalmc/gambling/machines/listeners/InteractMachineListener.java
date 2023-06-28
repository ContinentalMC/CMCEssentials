package com.yopal.continentalmc.gambling.machines.listeners;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import com.yopal.continentalmc.gambling.machines.horsebet.HorseBetGUI;
import com.yopal.continentalmc.gambling.machines.horsebet.HorseBetGUIManager;
import com.yopal.continentalmc.gambling.machines.impostor.ImpostorGUI;
import com.yopal.continentalmc.gambling.machines.impostor.ImpostorGUIManager;
import com.yopal.continentalmc.gambling.machines.managers.MachineGUIManager;
import com.yopal.continentalmc.gambling.machines.platforms.PlatformsGUI;
import com.yopal.continentalmc.gambling.machines.platforms.PlatformsGUIManager;
import com.yopal.continentalmc.gambling.machines.rockpaperscissors.RPSGUI;
import com.yopal.continentalmc.gambling.machines.rockpaperscissors.RPSGUIManager;
import com.yopal.continentalmc.gambling.machines.slots.SlotGUI;
import com.yopal.continentalmc.gambling.machines.slots.SlotGUIManager;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import jdk.tools.jlink.internal.Platform;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
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

            case HORSEBET:
                if (!tokenType.equalsIgnoreCase("special")) {
                    PlayerInteract.sendInvalidUsage(e.getPlayer(), "You must have a SPECIAL token.");
                    return;
                }

                HorseBetGUIManager.addGUI(e.getPlayer().getUniqueId() , new HorseBetGUI(cmc, e.getPlayer(), CasinoManager.getWinPercentage(e.getClickedBlock())));
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                MachineGUIManager.addMachineInUse(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
                break;

            case PLATFORMS:
                if (!tokenType.equalsIgnoreCase("special")) {
                    PlayerInteract.sendInvalidUsage(e.getPlayer(), "You must have a SPECIAL token.");
                    return;
                }

                PlatformsGUIManager.addGUI(e.getPlayer().getUniqueId() , new PlatformsGUI(cmc, e.getPlayer(), CasinoManager.getWinPercentage(e.getClickedBlock())));
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                MachineGUIManager.addMachineInUse(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
                break;
            case ROCKPAPERSCISSORS:
                if (!tokenType.equalsIgnoreCase("insane")) {
                    PlayerInteract.sendInvalidUsage(e.getPlayer(), "You must have an INSANE token.");
                    return;
                }

                RPSGUI rpsGUI = new RPSGUI(cmc, e.getPlayer(), CasinoManager.getWinPercentage(e.getClickedBlock()), 25000, 1);

                RPSGUIManager.addGUI(e.getPlayer().getUniqueId() , rpsGUI);
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                MachineGUIManager.addMachineInUse(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());

                rpsGUI.startAction();
                break;
        }

    }
}
