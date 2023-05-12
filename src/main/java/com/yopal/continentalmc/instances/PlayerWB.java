package com.yopal.continentalmc.instances;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerWB {

    private UUID playerThatJoinedUUID;

    private UUID playerToChatUUID;

    public PlayerWB(UUID playerThatJoinedUUID, UUID playerToChatUUID) {
        this.playerToChatUUID = playerToChatUUID;
        this.playerThatJoinedUUID = playerThatJoinedUUID;
    }

    // getters

    public Player getPlayerToChat() { return Bukkit.getPlayer(playerToChatUUID); }
    public Player getPlayerThatJoined() { return Bukkit.getPlayer(playerThatJoinedUUID); }



}
