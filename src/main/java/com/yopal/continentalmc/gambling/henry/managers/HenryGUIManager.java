package com.yopal.continentalmc.gambling.henry.managers;

import com.yopal.continentalmc.gambling.henry.instances.HenryGUI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HenryGUIManager {

    private static List<HenryGUI> henryGUIList = new ArrayList<>();

    public static void addGUI(HenryGUI gui) {
        HenryGUIManager.henryGUIList.add(gui);
    }

    public static void removeGUI(HenryGUI gui) {
        HenryGUIManager.henryGUIList.remove(gui);
    }

    public static HenryGUI getGUI(Player player) {
        for (HenryGUI gui : HenryGUIManager.henryGUIList) {
            if (gui.getPlayer().equals(player)) {
                return gui;
            }
        }

        return null;
    }

}
