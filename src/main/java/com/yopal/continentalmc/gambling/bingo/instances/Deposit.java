package com.yopal.continentalmc.gambling.bingo.instances;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.bingo.managers.BingoGUIManager;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.Cuboid;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deposit extends BukkitRunnable {

    private int money;
    private CMCEssentials cmc;
    private ArmorStand armorStand;

    public Deposit(CMCEssentials cmc, int moneyInvolved) {

        this.cmc = cmc;
        money = moneyInvolved;

        if (CasinoManager.getBingoTotal() >= CasinoManager.getBingoMaxOut()) {
            return;
        }

        spawnStand();
    }

    public void spawnStand() {
        Cuboid cuboid = new Cuboid(CasinoManager.getLoc1(), CasinoManager.getLoc2());
        Random random = new Random();
        List<Location> spawnLocs = new ArrayList<>();

        for (Block block : cuboid.getBlocks()) {
            if (block.getType().equals(Material.AIR)) {
                spawnLocs.add(block.getLocation());
            }
        }

        Location randLoc = spawnLocs.get(random.nextInt(spawnLocs.size()));

        // ARMORSTAND
        armorStand = (ArmorStand) cuboid.getWorld().spawnEntity(randLoc, EntityType.ARMOR_STAND);
        armorStand.setCustomName(ChatColor.GREEN + ChatColor.BOLD.toString() + "$" + money);
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);

        // PARTICLES
        cuboid.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, randLoc, 15, 1, 1, 1);

        // SOUND
        cuboid.getWorld().playSound(randLoc, Sound.ENTITY_PLAYER_LEVELUP, 1, 2);

        runTaskLater(cmc, 40);
    }

    @Override
    public void run() {
        armorStand.remove();

        CasinoManager.addOnBingoTotal(cmc, money);

        if (CasinoManager.getBingoTotal() >= CasinoManager.getBingoMaxOut()) {
            BingoGUIManager.startBingo(cmc);
        }

        ArmorStand armorStand = (ArmorStand) Bukkit.getEntity(CasinoManager.getBingoStand());
        if (armorStand == null) {
            return;
        }

        armorStand.setCustomName(ChatColor.GREEN + ChatColor.BOLD.toString() + "TOTAL POOL: " + CasinoManager.getBingoTotal());

        for (BingoGUI gui : BingoGUIManager.getGUIS()) {
            gui.updatePool();
        }
    }
}
