package com.yopal.continentalmc.gambling.henry.managers;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.henry.listeners.PlayerInteractHenryListener;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public class HenryManager {

    public static void spawn(CMCEssentials cmc, Location loc) {
        Villager henry = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        henry.setVillagerLevel(5);
        henry.setVillagerType(Villager.Type.PLAINS);
        henry.setProfession(Villager.Profession.LIBRARIAN);
        henry.setInvulnerable(true);

        henry.setCustomName(ChatColor.GREEN + "[NPC] " +  ChatColor.GOLD + ChatColor.BOLD + "Henry");

        henry.getPersistentDataContainer().set(new NamespacedKey(cmc, "casinoHandler"), PersistentDataType.STRING, "henry");
    }

    public static void welcomePlayer(Player player) {
        // code
        Random random = new Random();

        player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + CasinoManager.getHenryWelcomes().get(random.nextInt(CasinoManager.getHenryWelcomes().size())));
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
    }

}
