package com.yopal.continentalmc.gambling.machines.impostor.instances;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.utils.PageUtil;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class ImpostorGUI {

    private Player player;
    private int winPercentage;

    public ImpostorGUI(CMCEssentials cmc, Player player, int winPercentage) {
        this.player = player;
        this.winPercentage = winPercentage;

        openMainPage();
    }

    public void openMainPage() {
        Inventory inv = Bukkit.createInventory(player, 27, PlayerInteract.returnPrefix() + "Impostor Machine");
        ArrayList<String> headTextures = getRandomHeadTextures();
        ArrayList<String> names = getRandomNames();

        Random random = new Random();
        String seperator = ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
        HashMap<String, List<String>> personality = CasinoManager.getPersonality();

        // TODO: implement this GUI into the machines!
        // TODO: add winning percentage and such

        for (int i = 0; i < headTextures.size(); i++) {
            PageUtil.setCustomSkull(inv, headTextures.get(i), i + 11);

            PageUtil.updateDisplayName(inv, i + 11, ChatColor.GOLD + ChatColor.BOLD.toString() + names.get(i));

            PageUtil.updateLore(inv, i + 11, Arrays.asList(
                    seperator,
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "Age: " + ChatColor.GRAY + random.nextInt(1, 101),
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "Job: " + personality.get("job").get(random.nextInt(personality.get("job").size())),
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "Favorite Color: " + personality.get("favColor").get(random.nextInt(personality.get("favColor").size())),
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "Favorite Animal: " + personality.get("favAnimal").get(random.nextInt(personality.get("favAnimal").size())),
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "Favorite Hobby: " + personality.get("favHobby").get(random.nextInt(personality.get("favHobby").size())),
                    seperator
            ));
        }

    }

    private boolean getWin() {
        Random random = new Random();
        int winNum = random.nextInt(101);

        if (winNum <= winPercentage) {
            return true;
        } else {
            return false;
        }
    }

    private void setLosing() {

    }

    private ArrayList<String> getRandomHeadTextures() {
        ArrayList<String> textures = new ArrayList();
        Random random = new Random();
        List<String> headYMLTextures = CasinoManager.getVillagerHeadTextures();

        while (textures.size() < 5) {
            String headTexture = headYMLTextures.get(random.nextInt(headYMLTextures.size()));

            if (textures.contains(headTexture)) {
                continue;
            }

            textures.add(headTexture);

        }

        return textures;
    }

    private ArrayList<String> getRandomNames() {
        ArrayList<String> names = new ArrayList<>();
        Random random = new Random();
        List<String> namesYML = CasinoManager.getVillagerNames();

        while (names.size() < 5) {
            String name = namesYML.get(random.nextInt(namesYML.size()));

            if (names.contains(name)) {
                continue;
            }

            names.add(name);

        }

        return names;
    }

}
