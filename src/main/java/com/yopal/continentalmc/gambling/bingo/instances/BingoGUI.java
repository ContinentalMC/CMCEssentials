package com.yopal.continentalmc.gambling.bingo.instances;

import com.yopal.continentalmc.gambling.bingo.managers.BingoGUIManager;
import com.yopal.continentalmc.gambling.utils.PageUtil;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.utils.PlayerInteract;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BingoGUI {

    private Inventory inv;
    private Player player;
    private List<Material> materials;

    public BingoGUI(Player player) {
        this.player = player;
        openMainPage();
    }

    public void openMainPage() {
        inv = Bukkit.createInventory(player, 54, PlayerInteract.returnPrefix() + "Bingo");

        // FRAMES
        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setDisplayName(" ");
        frame.setItemMeta(frameMeta);
        PageUtil.createFrames(inv, frame, 0, 53);

        // PLAYER SKULL
        PageUtil.setPlayerSkull(inv, player, 25, Arrays.asList("I have to remember to click the confirm head if I get bingo!"));

        // CLOSE
        PageUtil.setItem(inv, ChatColor.RED + ChatColor.BOLD.toString() + "CLOSE", Material.BARRIER, Arrays.asList(
                ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To close!"
        ), 34);

        // GREEN CHECK HEAD
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUyOGJlYTlkMzkzNzNkMzZlZThmYTQwZWM4M2Y5YzNmY2RkOTMxNzUyMjc3NDNmOWRkMWY3ZTc4ODZiN2VlNSJ9fX0=", 28);
        PageUtil.updateDisplayName(inv, 28, ChatColor.RED + ChatColor.BOLD.toString() + "CONFIRM");
        PageUtil.updateLore(inv, 28, Arrays.asList(
                ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To take your winnings!"
        ));

        // MONEY BAG
        PageUtil.setCustomSkull(inv, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=", 19);
        PageUtil.updateDisplayName(inv, 19, ChatColor.GOLD + ChatColor.BOLD.toString() + "TOTAL MONEY");
        PageUtil.updateLore(inv, 19, Arrays.asList(ChatColor.GRAY.toString() + CasinoManager.getBingoTotal()));

        // SLOT
        PageUtil.setItem(inv, ChatColor.YELLOW + ChatColor.BOLD.toString() + "CURRENT ITEM", BingoGUIManager.getRandMaterial(), Arrays.asList(ChatColor.GRAY + "See if you have this item!"), 13);

        // BLOCKS
        Random random = new Random();
        List<Material> materials = new ArrayList<>();
        while (materials.size() != 9) {
            Material material = CasinoManager.getBingoMaterials().get(random.nextInt(CasinoManager.getBingoMaterials().size()));

            if (materials.contains(material)) {
                continue;
            }

            materials.add(material);
        }

        int indexNum = 0;
        for (int invSlot : Arrays.asList(21, 22, 23, 30, 31, 32, 39, 40, 41)) {
            PageUtil.setItem(inv, ChatColor.RED + ChatColor.BOLD.toString() + materials.get(indexNum).toString(), materials.get(indexNum), Arrays.asList(
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "[CLICK] " + ChatColor.GRAY + "To acknowledge this item!"
            ), invSlot);
        }

        player.openInventory(inv);
    }

    public void updateMaterial() {
        Material material = BingoGUIManager.getRandMaterial();

        if (!materials.contains(material)) {
            materials.add(material);
        }

        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1 ,1);
        PageUtil.setItem(inv, ChatColor.YELLOW + ChatColor.BOLD.toString() + "CURRENT ITEM", material, Arrays.asList(ChatColor.GRAY + "See if you have this item!"), 13);
    }

    public boolean checkWin() {
        List<Integer> possibleWins = new ArrayList<>();

        for (int invSlot : Arrays.asList(21, 22, 23, 30, 31, 32, 39, 40, 41)) {
            if (inv.getItem(invSlot).getItemMeta().getDisplayName().contains(ChatColor.GREEN.toString())) {
                possibleWins.add(invSlot);
            }
        }

        // ROWS
        for (int invSlot : Arrays.asList(21, 22, 23, 30, 31, 32, 39, 40, 41)) {
            if (possibleWins.contains(invSlot) && possibleWins.contains(invSlot + 1) && possibleWins.contains(invSlot + 2)) {
                return true;
            }
        }

        // COLUMNS
        for (int invSlot : Arrays.asList(21, 22, 23, 30, 31, 32, 39, 40, 41)) {
            if (possibleWins.contains(invSlot) && possibleWins.contains(invSlot + 9) && possibleWins.contains(invSlot + 18)) {
                return true;
            }
        }

        // DIAGONALS
        if (possibleWins.contains(21) && possibleWins.contains(31) && possibleWins.contains(41)) {
            return true;
        }

        if (possibleWins.contains(23) && possibleWins.contains(31) && possibleWins.contains(39)) {
            return true;
        }

        return false;

    }

    public List<Material> getMaterials() { return materials; }

    public Inventory getInv() { return inv; }

}
