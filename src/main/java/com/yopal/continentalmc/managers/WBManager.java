package com.yopal.continentalmc.managers;

import com.yopal.continentalmc.instances.PlayerWB;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;

public class WBManager {

    private static List<PlayerWB> playerWBList = new ArrayList<>();

    public static void addPlayerWB(PlayerWB playerWB) {
        playerWBList.add(playerWB);
    }

    public static boolean checkPlayer(Player player) {
        if (playerWBList.isEmpty()) {
            return false;
        }

        for (PlayerWB playerWB : playerWBList) {
            if (playerWB.getPlayerThatJoined() == player) {
                return true;
            }
        }

        return false;

    }

    public static PlayerWB getPlayerWB(Player playerToChat) {
        if (playerWBList.isEmpty()) { return null; }

        for (PlayerWB playerWB : playerWBList) {
            if (playerWB.getPlayerToChat() == playerToChat) {
                return playerWB;
            }
        }

        return null;
    }

    public static void removePlayerThatJoined(Player player) {
        if (playerWBList.isEmpty()) { return; }

        // error is right here
        Collection<PlayerWB> playerWBSToRemove = new ArrayList<>();
        for (PlayerWB playerWB : playerWBList) {
            if (playerWB.getPlayerThatJoined() == player) {
                playerWBSToRemove.add(playerWB);
            }
        }
        playerWBList.removeAll(playerWBSToRemove);


    }

    public static void removePlayerWB(PlayerWB playerWB) {
        playerWBList.remove(playerWB);
    }

}
