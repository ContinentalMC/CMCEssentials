package com.yopal.continentalmc.commands;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import com.yopal.continentalmc.gambling.henry.managers.HenryManager;
import com.yopal.continentalmc.gambling.machines.managers.MachineCreationManager;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.managers.YML.ConfigManager;
import com.yopal.continentalmc.managers.YML.EmojiManager;
import com.yopal.continentalmc.managers.YML.ScoreManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CMCCommand implements CommandExecutor {

    private CMCEssentials cmc;
    public CMCCommand(CMCEssentials cmc) {
        this.cmc = cmc;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (args.length == 0) {
            return false;
        }

        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("reload") && player.hasPermission("cmc.admin.reload")) {
            ConfigManager.reloadConfig(cmc);
            ScoreManager.reloadFile(cmc);
            EmojiManager.reloadFile(cmc);
            CasinoManager.reloadFile(cmc);
            PlayerInteract.sendMessage(player, ChatColor.GREEN + "Files successfully reloaded!");
        } else if (args[0].equalsIgnoreCase("reload") && !player.hasPermission("cmc.admin.reload")) {
            PlayerInteract.sendLackPermission(player, "cmc.admin.reload");
        }

        if (args[0].equalsIgnoreCase("spawnHenry") && player.hasPermission("cmc.admin.spawnHenry")) {
            HenryManager.spawn(cmc, player.getLocation());
        } else if (args[0].equalsIgnoreCase("spawnHenry") && !player.hasPermission("cmc.admin.spawnHenry")) {
            PlayerInteract.sendLackPermission(player, "cmc.admin.spawnHenry");
        }

        if (args[0].equalsIgnoreCase("createMachine") && player.hasPermission("cmc.admin.createMachine")) {
            if (args.length == 1) {
                PlayerInteract.sendInvalidUsage(player, "Please input a name for the machine!");
                return false;
            }

            if (args.length == 2) {
                PlayerInteract.sendInvalidUsage(player, "Please input the machine type!");
                return false;
            }

            if (args.length == 3) {
                PlayerInteract.sendInvalidUsage(player, "Please input the winning percentage!");
                return false;
            }

            if (CasinoManager.isMachine(args[1])) {
                PlayerInteract.sendInvalidUsage(player, "Please choose a different name!");
                return false;
            }

            MachineTypes type;

            try {
                type = MachineTypes.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                PlayerInteract.sendInvalidUsage(player, "That's not a machine type!");
                return false;
            }

            Block block = player.getTargetBlock(null, 5);
            if (block == null) {
                return false;
            }

            int winPercentage;
            try {
                winPercentage = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                PlayerInteract.sendInvalidUsage(player, "Please input a real number!");
                return false;
            }

            MachineCreationManager.createMachine(cmc, type, block, args[1], winPercentage);

        } else if (args[0].equalsIgnoreCase("createMachine") && !player.hasPermission("cmc.admin.createmMachine")) {
            PlayerInteract.sendLackPermission(player, "cmc.admin.createMachine");
        }

        if (args[0].equalsIgnoreCase("removeMachine") && player.hasPermission("cmc.admin.removeMachine")) {
            if (args.length == 1) {
                PlayerInteract.sendInvalidUsage(player, "Please input the machine name to remove!");
                return false;
            }

            if (!CasinoManager.isMachine(args[1])) {
                PlayerInteract.sendInvalidUsage(player, "That's not a machine!");
                return false;
            }

            for (UUID uuid : CasinoManager.getArmorStandUUIDs(args[1])) {
                Bukkit.getEntity(uuid).remove();
            }

            CasinoManager.removeMachine(cmc, args[1]);
        }

        if (args[0].equalsIgnoreCase("emojis") && player.hasPermission("cmc.user.emojisList")) {
            PlayerInteract.sendMessage(player, ChatColor.GREEN + "Here are all the emojis!");
            for (String string : EmojiManager.getEmojis().keySet()) {
                player.sendMessage(ChatColor.GRAY + "  - " + string + " = " + EmojiManager.getEmojis().get(string));
            }
        } else if (args[0].equalsIgnoreCase("emojis") && !player.hasPermission("cmc.user.emojisList")) {
            PlayerInteract.sendLackPermission(player, "cmc.user.emojisList");
        }

        if (args[0].equalsIgnoreCase("getScore") && player.hasPermission("cmc.user.getScore")) {
            if (args.length == 1) {
                PlayerInteract.sendInvalidUsage(player, "Please input a player's name!");
                return false;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            if (!offlinePlayer.hasPlayedBefore()) {
                PlayerInteract.sendInvalidUsage(player, "Player has to have played before!");
                return false;
            }

            PlayerInteract.sendMessage(player, offlinePlayer.getName() +  "'s karma score is " + ScoreManager.getScore(offlinePlayer.getUniqueId()) + ". " + ScoreManager.getTopScorer() + " is a top scorer!");

        } else if (args[0].equalsIgnoreCase("getScore") && !player.hasPermission("cmc.user.getScore")) {
            PlayerInteract.sendLackPermission(player, "cmc.user.getScore");
        }

        if (args[0].equalsIgnoreCase("setCuboidPoint1") && player.hasPermission("cmc.admin.setCuboid")) {

            CasinoManager.setLoc(cmc, 1, player.getTargetBlock(null, 5).getLocation());

            PlayerInteract.sendMessage(player, ChatColor.GREEN + "Successful!");

        } else if (args[0].equalsIgnoreCase("getScore") && !player.hasPermission("cmc.admin.setCuboid")) {
            PlayerInteract.sendLackPermission(player, "cmc.admin.setCuboid");
        }

        if (args[0].equalsIgnoreCase("setCuboidPoint2") && player.hasPermission("cmc.admin.setCuboid")) {

            CasinoManager.setLoc(cmc, 2, player.getTargetBlock(null, 5).getLocation());

            PlayerInteract.sendMessage(player, ChatColor.GREEN + "Successful!");

        } else if (args[0].equalsIgnoreCase("getScore") && !player.hasPermission("cmc.admin.setCuboid")) {
            PlayerInteract.sendLackPermission(player, "cmc.admin.setCuboid");
        }

        if (args[0].equalsIgnoreCase("setBingoStand") && player.hasPermission("cmc.admin.setBingoStand")) {

            Block targetBlock = player.getTargetBlockExact(5);

            if (targetBlock == null) {
                PlayerInteract.sendInvalidUsage(player, "You need to look at a block!");
                return false;
            }

            ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(targetBlock.getLocation().add(0.5, 1, 0.5), EntityType.ARMOR_STAND);
            armorStand.setInvulnerable(true);
            armorStand.setSmall(true);
            armorStand.setVisible(false);
            armorStand.setCustomName(ChatColor.GREEN + ChatColor.BOLD.toString() + "TOTAL POOL: " + CasinoManager.getBingoTotal());
            armorStand.setCustomNameVisible(true);
            armorStand.setGravity(false);

            CasinoManager.setBingoStand(cmc, armorStand);

            PlayerInteract.sendMessage(player, "Bingo stand set successfully!");
        } else if (args[0].equalsIgnoreCase("setBingoStand") && !player.hasPermission("cmc.admin.setBingoStand")) {
            PlayerInteract.sendLackPermission(player, "cmc.admin.setBingoStand");
        }
        return false;
    }
}
