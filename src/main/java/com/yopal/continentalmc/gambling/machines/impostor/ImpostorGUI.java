package com.yopal.continentalmc.gambling.machines.impostor;

import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import com.yopal.continentalmc.gambling.managers.CelebrationManager;
import com.yopal.continentalmc.gambling.utils.PageUtil;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ImpostorGUI {
    private CMCEssentials cmc;
    private Player player;
    private int winPercentage;
    private Inventory inv;

    public ImpostorGUI(CMCEssentials cmc, Player player, int winPercentage) {
        this.cmc = cmc;
        this.player = player;
        this.winPercentage = winPercentage;

        openMainPage();
    }

    public void openMainPage() {
        inv = Bukkit.createInventory(player, 27, PlayerInteract.returnPrefix() + "Impostor Machine");
        ArrayList<String> headTextures = getRandomHeadTextures();
        ArrayList<String> names = getRandomNames();

        // CLOSE
        PageUtil.setItem(inv, ChatColor.RED + ChatColor.BOLD.toString() + "CLOSE", Material.BARRIER, Arrays.asList(
                ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To close!"
        ), 9);

        // RETRY
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc1ZDNkYjAzZGMyMWU1NjNiMDM0MTk3ZGE0MzViNzllY2ZlZjRiOGUyZWNkYjczMGUzNzBjMzE2NjI5ZDM2ZiJ9fX0=", 17);
        PageUtil.updateDisplayName(inv, 17, ChatColor.RED + ChatColor.BOLD.toString() + "RETRY");
        PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.RED + "Please select a villager first!"));

        // FRAMES
        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setDisplayName(" ");
        frame.setItemMeta(frameMeta);
        PageUtil.createFrames(inv, frame,0, 26);

        Random random = new Random();
        String seperator = ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
        HashMap<String, List<String>> personality = CasinoManager.getPersonality();

        for (int i = 0; i < headTextures.size(); i++) {
            PageUtil.setCustomSkull(inv, headTextures.get(i), i + 11);

            PageUtil.updateDisplayName(inv, i + 11, ChatColor.GOLD + ChatColor.BOLD.toString() + names.get(i));

            PageUtil.updateLore(inv, i + 11, Arrays.asList(
                    seperator,
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "Age: " + ChatColor.GRAY + random.nextInt(1, 101),
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "Job: " + ChatColor.GRAY + personality.get("job").get(random.nextInt(personality.get("job").size())),
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "Favorite Color: " + ChatColor.GRAY + personality.get("favColor").get(random.nextInt(personality.get("favColor").size())),
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "Favorite Animal: " + ChatColor.GRAY + personality.get("favAnimal").get(random.nextInt(personality.get("favAnimal").size())),
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "Favorite Hobby: " + ChatColor.GRAY + personality.get("favHobby").get(random.nextInt(personality.get("favHobby").size())),
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To confirm your suspicion!",
                    seperator
            ));
        }

        player.openInventory(inv);

    }

    public boolean checkWin() {
        Random random = new Random();
        int winNum = random.nextInt(1, 101);

        if (winNum <= winPercentage) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkGoldenWin() {
        Random random = new Random();
        int winNum = random.nextInt(1, 101);

        if (winNum <= (winPercentage/100)) {
            return true;
        } else {
            return false;
        }
    }

    public void setWinning(int invSlot) {
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdjMTRlYjg3ZGM2NDQ0YWU2MjVmMTIyY2YzYWU5NmZjOGEyODZhYmI2OWRjYzc4ZWU5NWNkNDQzMjMyYTA1YyJ9fX0=", invSlot);
        PageUtil.updateDisplayName(inv, invSlot, ChatColor.RED + ChatColor.BOLD.toString() + "IMPOSTOR");
        PlayerInteract.sendMessage(player, PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "Good job catching the impostor! You should try again!");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);

        CelebrationManager.summonFirework(player.getUniqueId(), Color.GRAY, cmc, MachineTypes.IMPOSTOR);

        Economy econ = CMCEssentials.getEconomy();
        econ.depositPlayer(player, CasinoManager.getImpostorWinning("REGULAR"));
        PlayerInteract.sendMessage(player, ChatColor.GREEN + "$" + CasinoManager.getImpostorWinning("REGULAR") + " has been added to your balance!");

        // changing retry head
        if (checkHasToken()) {
            PageUtil.updateDisplayName(inv, 17, ChatColor.GREEN + ChatColor.BOLD.toString() + "RETRY");
            PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To retry the impostor machine!"));
        } else {
            PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.RED + "You need a GOOD token to play again!"));
        }

    }

    public void setGoldenWinning(int invSlot) {
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFiZjg0ODc3OWNhODFlZDlmNGJjODhmOTM0NTEzZGQ4Y2Q2NzEyMzAxZGIzNjk1MDg4MDFhZGNhMDk4Y2QwMCJ9fX0=", invSlot);
        PageUtil.updateDisplayName(inv, invSlot, ChatColor.GOLD + ChatColor.BOLD.toString() + "IMPOSTOR");
        PlayerInteract.sendMessage(player, PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.GRAY + "Wow you caught a golden impostor! You should test your luck again!");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);

        CelebrationManager.summonFirework(player.getUniqueId(), Color.YELLOW, cmc, MachineTypes.IMPOSTOR);

        Economy econ = CMCEssentials.getEconomy();
        econ.depositPlayer(player, CasinoManager.getImpostorWinning("GOLDEN"));
        PlayerInteract.sendMessage(player, ChatColor.GREEN + "$" + CasinoManager.getImpostorWinning("GOLDEN") + " has been added to your balance!");

    }

    public void setLosing(int invSlot) {
        Random random = new Random();
        int randInvSlot = 11;

        while (randInvSlot == invSlot) {
            randInvSlot = random.nextInt(11, 16);
        }

        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdjMTRlYjg3ZGM2NDQ0YWU2MjVmMTIyY2YzYWU5NmZjOGEyODZhYmI2OWRjYzc4ZWU5NWNkNDQzMjMyYTA1YyJ9fX0=", randInvSlot);
        player.sendMessage(PlayerInteract.returnPrefix() + ChatColor.GOLD + ChatColor.BOLD + "Henry: " + ChatColor.RED + "You didn't win anything unfortunately. Better luck next time!");
        PageUtil.updateDisplayName(inv, randInvSlot, ChatColor.RED + ChatColor.BOLD.toString() + "IMPOSTOR");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);

        // changing retry head
        if (checkHasToken()) {
            PageUtil.updateDisplayName(inv, 17, ChatColor.GREEN + ChatColor.BOLD.toString() + "RETRY");
            PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To retry the impostor machine!"));
        } else {
            PageUtil.updateLore(inv, 17, Arrays.asList(ChatColor.RED + "You need a GOOD token to play again!"));
        }

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
    private boolean checkHasToken() {

        NamespacedKey tokenTypeKey = new NamespacedKey(cmc, "tokenType");

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null) {
                continue;
            }

            if (!itemStack.hasItemMeta()) {
                continue;
            }

            if (!itemStack.getItemMeta().getPersistentDataContainer().has(tokenTypeKey, PersistentDataType.STRING)) {
                continue;
            }

            if (!itemStack.getItemMeta().getPersistentDataContainer().get(tokenTypeKey, PersistentDataType.STRING).equalsIgnoreCase("good")) {
                continue;
            }

            return true;
        }

        return false;

    }


}
