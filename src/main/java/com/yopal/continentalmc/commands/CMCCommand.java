package com.yopal.continentalmc.commands;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.managers.YML.ConfigManager;
import com.yopal.continentalmc.managers.YML.EmojiManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            EmojiManager.reloadFile(cmc);
            PlayerInteract.sendMessage(player, ChatColor.GREEN + "Files successfully reloaded!");
        } else {
            PlayerInteract.sendLackPermission(player, "cmc.admin.reload");
        }



        return false;
    }
}
