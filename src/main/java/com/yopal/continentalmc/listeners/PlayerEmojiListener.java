package com.yopal.continentalmc.listeners;

import com.yopal.continentalmc.instances.PlayerWB;
import com.yopal.continentalmc.managers.WBManager;
import com.yopal.continentalmc.managers.YML.ConfigManager;
import com.yopal.continentalmc.managers.YML.EmojiManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class PlayerEmojiListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        HashMap<String, String> emojis = EmojiManager.getEmojis();

        for (String string : emojis.keySet()) {
            if (e.getMessage().contains(string)) {
                e.setMessage(e.getMessage().replace(string, emojis.get(string)));
            }
        }
    }
}
