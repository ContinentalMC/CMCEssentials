package com.yopal.continentalmc.utils;

import com.yopal.continentalmc.enums.MessageTypes;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerInteract {

    private static String prefix = ChatColor.GREEN + "[" + ChatColor.AQUA + "CMC" + ChatColor.GREEN  + "] " + ChatColor.GRAY;
    private static TextComponent prefixComponent = new TextComponent("§a[§bCMC§a] ");

    // Server messaging
    /**
     * Send a title to all the players enlisted in the game
     * @param message
     * @param secondMesssage
     * @param type
     */
    public static void sendToAll(String message, String secondMesssage, MessageTypes type) {
        switch (type) {
            case TITLE:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    sendTitle(player, message);
                }
                break;
            case TITLEWITHSUB:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    sendTitle(player, message, secondMesssage);
                }
                break;

        }

    }

    /**
     * Send a message to all the players enlisted in the game
     * @param message
     * @param type
     */
    public static void sendToAll(String message, MessageTypes type) {

        switch (type) {
            case MESSAGE:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    sendMessage(player, message);
                }
                break;
            case ACTIONBAR:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    sendActionBar(player, message);
                }
                break;
            case FANCY:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    sendFancyMessage(player, message);
                }
        }


    }

    // Individual Messaging
    /**
     * Send a message to a specific player
     * @param player
     * @param message
     */
    public static void sendMessage(Player player, String message) {
        player.sendMessage(prefix + message);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
    }

    /**
     * Send a title without a subtitle to a specific player
     * @param player
     * @param title
     */
    public static void sendTitle(Player player, String title) {
        player.sendTitle(title, "");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 2);
    }

    /**
     * Send an action bar to a specific player
     * § is used for color codes
     * @param player
     * @param message
     */
    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    /**
     * Send a title with a subtitle to a specific player
     * @param player
     * @param title
     * @param subtitle
     */
    public static void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle);
    }

    /**
     * Send an error message to the player
     * @param player
     */
    public static void sendInvalidUsage(Player player, String message) {
        player.sendMessage(prefix + ChatColor.RED + "Wrong usage! " + message);
    }

    /**
     * Send a spigot message to the player with a prefix
     * @param player
     * @param textComponents
     */
    public static void sendSpigotMessageWithPrefix(Player player, TextComponent[] textComponents) {


        TextComponent start = new TextComponent(prefixComponent);
        for (TextComponent textComponent : textComponents) {
            start.addExtra(textComponent);
        }

        player.spigot().sendMessage(start);
    }

    /**
     * Send a spigot message to the player without a prefix
     * @param player
     * @param textComponents
     */
    public static void sendSpigotMessageWithoutPrefix(Player player, TextComponent[] textComponents) {


        TextComponent start = new TextComponent("");
        for (TextComponent textComponent : textComponents) {
            start.addExtra(textComponent);
        }

        player.spigot().sendMessage(start);
    }

    /**
     * Send a fancy text to a player
     * @param player
     * @param message
     */
    public static void sendFancyMessage(Player player, String message) {
        

        /*
        -=-=-=-=-=-=-=-=-
        (Fancy Text)
        -=-=-=-=-=-=-=-=-
         */
        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        player.sendMessage(ChatColor.GRAY + message);
        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
    }

    /**
     * Send the player that they are lacking a certain permission
     * @param player
     * @param permission
     */
    public static void sendLackPermission(Player player, String permission) {

        player.sendMessage(prefix + ChatColor.RED + "You're lacking the permission: " + permission);
    }

    /**
     * Returns the prefix.
     * @return
     */
    public static String returnPrefix() { return prefix;}
}

