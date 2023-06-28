package com.yopal.continentalmc.gambling.machines.listeners;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.henry.managers.TokenManager;
import com.yopal.continentalmc.gambling.machines.horsebet.HorseBetGUI;
import com.yopal.continentalmc.gambling.machines.horsebet.HorseBetGUIManager;
import com.yopal.continentalmc.gambling.machines.impostor.ImpostorGUI;
import com.yopal.continentalmc.gambling.machines.impostor.ImpostorGUIManager;
import com.yopal.continentalmc.gambling.machines.managers.MachineGUIManager;
import com.yopal.continentalmc.gambling.machines.platforms.PlatformsGUI;
import com.yopal.continentalmc.gambling.machines.platforms.PlatformsGUIManager;
import com.yopal.continentalmc.gambling.machines.rockpaperscissors.RPSGUI;
import com.yopal.continentalmc.gambling.machines.rockpaperscissors.RPSGUIManager;
import com.yopal.continentalmc.gambling.machines.rockpaperscissors.RPSGUIType;
import com.yopal.continentalmc.gambling.machines.slots.SlotGUI;
import com.yopal.continentalmc.gambling.machines.slots.SlotGUIManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.UUID;

public class MachineCloseListener implements Listener {

    private CMCEssentials cmc;

    public MachineCloseListener(CMCEssentials cmc) {
        this.cmc = cmc;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        // MACHINE LOCATIONS - REMOVAL IS IN IF STATEMENTS OF OTHER GUIs
        HashMap<UUID, Location> machinesInUse = MachineGUIManager.getMachinesInUse();

        if (!machinesInUse.containsKey(e.getPlayer().getUniqueId())) {
            return;
        }

        // SLOT GUI
        SlotGUI slotGUI = SlotGUIManager.getGUI(e.getPlayer().getUniqueId());

        if (slotGUI == null) {}
        else if (slotGUI.getIsRolling()) {
            slotGUI.cancelAllTasks();
        }

        if (slotGUI != null) {
            SlotGUIManager.removeGUI(e.getPlayer().getUniqueId());
            MachineGUIManager.removeMachineInUse(e.getPlayer().getUniqueId());
        }

        // IMPOSTOR GUI
        ImpostorGUI impostorGUI = ImpostorGUIManager.getGUI(e.getPlayer().getUniqueId());

        if (impostorGUI != null) {
            ImpostorGUIManager.removeGUI(e.getPlayer().getUniqueId());
            MachineGUIManager.removeMachineInUse(e.getPlayer().getUniqueId());
        }

        // HORSE BET GUI
        HorseBetGUI horseBetGUI = HorseBetGUIManager.getGUI(e.getPlayer().getUniqueId());

        if (horseBetGUI == null) {}
        else if (horseBetGUI.getIsRunning()) {
            horseBetGUI.cancelAllTasks();
            TokenManager.giveSpecialToken(cmc, (Player) e.getPlayer());
        }

        if (horseBetGUI != null) {
            HorseBetGUIManager.removeGUI(e.getPlayer().getUniqueId());
            MachineGUIManager.removeMachineInUse(e.getPlayer().getUniqueId());
        }

        // PLATFORMS GUI
        PlatformsGUI platformsGUI = PlatformsGUIManager.getGUI(e.getPlayer().getUniqueId());

        if (platformsGUI != null) {
            PlatformsGUIManager.removeGUI(e.getPlayer().getUniqueId());
            MachineGUIManager.removeMachineInUse(e.getPlayer().getUniqueId());
        }

        // ROCK PAPER SCISSORS GUI
        RPSGUI rpsGUI = RPSGUIManager.getGUI(e.getPlayer().getUniqueId());
        RPSGUI correlatedRPSGUI = RPSGUIManager.correlateGUI(e.getInventory());
        RPSGUIType correlatedType = null;
        
        if (correlatedRPSGUI != null) {
            correlatedType = correlatedRPSGUI.getGUIType();
        }

        if (rpsGUI != null && correlatedType == rpsGUI.getGUIType()) {
            RPSGUIManager.removeGUI(e.getPlayer().getUniqueId());
            MachineGUIManager.removeMachineInUse(e.getPlayer().getUniqueId());
            rpsGUI.cancelAllTasks();
            rpsGUI.giveTotalMoney();
        }



    }
}
