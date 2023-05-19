package com.yopal.continentalmc.gambling.machines.managers;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

import javax.naming.Name;

public class MachineCreationManager {

    public static void createMachine(CMCEssentials cmc, MachineTypes type, Block block, String name, int winningPercentage) {
        CasinoManager.setMachine(type, block, name, winningPercentage);

        // name
        String separator = ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + " >> ";
        String boldGold = ChatColor.GOLD + ChatColor.BOLD.toString();
        String[] lines = new String[] {
          boldGold + "Machine Name" + separator + ChatColor.YELLOW + ChatColor.BOLD + name,
          boldGold + "Machine Type" + separator + ChatColor.YELLOW + ChatColor.BOLD + type.toString(),
          ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + ChatColor.BOLD + block.getType().toString().replace("_", "").toUpperCase()
        };

        Location loc = block.getLocation();

        for (String line : lines) {
            ArmorStand armorStand = (ArmorStand) block.getWorld().spawnEntity(loc.subtract(0, 0.3, 0), EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setInvulnerable(true);

            // setting it so armor stand non interactable
            armorStand.getPersistentDataContainer().set(new NamespacedKey(cmc, "armorStandMachine"), PersistentDataType.STRING, type.toString());

            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(line);
        }

    }
}