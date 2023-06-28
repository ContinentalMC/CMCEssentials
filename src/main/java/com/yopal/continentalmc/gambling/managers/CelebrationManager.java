package com.yopal.continentalmc.gambling.managers;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class CelebrationManager {

    public static void summonFireworkSlot(UUID playerUUID, Material material, CMCEssentials cmc) {
        Player player = Bukkit.getPlayer(playerUUID);
        Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        firework.getPersistentDataContainer().set(new NamespacedKey(cmc, "fireworkMachine"), PersistentDataType.STRING, "slot");

        FireworkEffect.Builder builder = FireworkEffect.builder().withFlicker().withFade().with(FireworkEffect.Type.STAR);

        switch (material) {
            case DIAMOND:
                fireworkMeta.addEffect(builder.withColor(Color.AQUA).build());
                break;
            case EMERALD:
                fireworkMeta.addEffect(builder.withColor(Color.GREEN).build());
                break;
            case LAPIS_LAZULI:
                fireworkMeta.addEffect(builder.withColor(Color.BLUE).build());
                break;
            case IRON_INGOT:
                fireworkMeta.addEffect(builder.withColor(Color.GRAY).build());
                break;
            case COAL:
                fireworkMeta.addEffect(builder.withColor(Color.BLACK).build());
                break;
        }
        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();
    }

    public static void summonFirework(UUID playerUUID, Color color, CMCEssentials cmc, MachineTypes type) {
        Player player = Bukkit.getPlayer(playerUUID);
        Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        firework.getPersistentDataContainer().set(new NamespacedKey(cmc, "fireworkMachine"), PersistentDataType.STRING, type.toString().toLowerCase());

        FireworkEffect.Builder builder = FireworkEffect.builder().withFlicker().withFade().with(FireworkEffect.Type.STAR);

        fireworkMeta.addEffect(builder.withColor(color).build());

        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();
    }

}
