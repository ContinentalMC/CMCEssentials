package com.yopal.continentalmc.instances;

import org.bukkit.entity.Player;

public class PlayerWB {

    private Player playerThatJoined;

    private Player playerToChat;

    public PlayerWB(Player playerThatJoined, Player playerToChat) {
        this.playerThatJoined = playerThatJoined;
        this.playerToChat = playerToChat;
    }

    // getters

    public Player getPlayerToChat() { return playerToChat; }
    public Player getPlayerThatJoined() { return playerThatJoined; }



}
