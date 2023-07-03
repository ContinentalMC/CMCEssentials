package com.yopal.continentalmc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMCTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("reload", "emojis", "getScore", "createMachine", "removeMachine", "setCuboidPoint1", "setCuboidPoint2", "setBingoStand", "spawnHenry"), new ArrayList<>());
        }

        return null;
    }
}
