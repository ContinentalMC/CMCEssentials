package com.yopal.continentalmc.gambling.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.yopal.continentalmc.CMCEssentials;
import com.yopal.continentalmc.gambling.enums.MachineTypes;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PageUtil {

    /**
     * Create a framing for the GUI - only the top and the bottom row
     *
     * @param inv
     * @param frameItem
     * @param topLeftSlot
     * @param bottomRightSlot
     */
    public static void createFrames(Inventory inv, ItemStack frameItem, int topLeftSlot, int bottomRightSlot) {
        // making sure item doesn't have display name
        ItemMeta itemMeta = frameItem.getItemMeta();
        itemMeta.setDisplayName(" ");
        frameItem.setItemMeta(itemMeta);

        // top row
        for (int i = topLeftSlot; i < topLeftSlot + 9; i++) {
            inv.setItem(i, frameItem);
        }

        // bottom row
        for (int i = bottomRightSlot; i > bottomRightSlot - 9; i--) {
            inv.setItem(i, frameItem);
        }

    }


    /**
     * Cylcle a list of items in a certain slot
     * @param cmc
     * @param inv
     * @param slot
     * @param materialList
     */
    public static void cycleItemsInSlot(CMCEssentials cmc, Inventory inv, int slot, List<Material> materialList, Player player, int ticksLast) {

        Random random = new Random();

        BukkitTask cycleTask = Bukkit.getScheduler().runTaskTimer(cmc, ()->{
            // reordering frames
            inv.setItem(slot, new ItemStack(materialList.get(random.nextInt(materialList.size()))));
        }, 0, 2);

        Bukkit.getScheduler().runTaskLater(cmc, ()->{
            cycleTask.cancel();
        }, ticksLast);

    }


    /**
     * Spin the frames in rainbow form for the GUI - only the top and the bottom row
     *
     * @param inv
     * @param topLeftSlot
     * @param bottomRightSlot
     */
    public static void createRainbowFrames(CMCEssentials cmc, Inventory inv, int topLeftSlot, int bottomRightSlot) {
        List<Material> rainbowFrames = Arrays.asList(
                Material.RED_STAINED_GLASS_PANE,
                Material.ORANGE_STAINED_GLASS_PANE,
                Material.YELLOW_STAINED_GLASS_PANE,
                Material.BLUE_STAINED_GLASS_PANE,
                Material.PURPLE_STAINED_GLASS_PANE,
                Material.PINK_STAINED_GLASS_PANE,
                Material.RED_STAINED_GLASS_PANE,
                Material.ORANGE_STAINED_GLASS_PANE,
                Material.YELLOW_STAINED_GLASS_PANE
        );

        BukkitTask rainbowTask = Bukkit.getScheduler().runTaskTimer(cmc, ()->{
            // reordering frames
            Collections.rotate(rainbowFrames, 1);

            // setting new rainbow frames
            for (int frameNum = 0; frameNum < rainbowFrames.size(); frameNum++) {
                // making the item have no display name
                ItemStack frame = new ItemStack(rainbowFrames.get(frameNum));
                ItemMeta frameMeta = frame.getItemMeta();
                frameMeta.setDisplayName(" ");
                frame.setItemMeta(frameMeta);

                // top row
                inv.setItem(topLeftSlot + frameNum, frame);

                // bottom row
                inv.setItem(bottomRightSlot - frameNum, frame);

            }

        }, 0, 3);

        Bukkit.getScheduler().runTaskLater(cmc, ()->{
            rainbowTask.cancel();
        }, 130);

    }



    /**
     * Set a specific ItemStack at a slot in the GUI.
     *
     * @param inv
     * @param itemName
     * @param material
     * @param itemLore
     * @param slot
     */
    public static void setItem(Inventory inv, String itemName, Material material, List<String> itemLore, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(itemName);
        itemMeta.setLore(itemLore);

        item.setItemMeta(itemMeta);

        inv.setItem(slot, item);
    }

    /**
     * Update the lore of an item in a GUI
     *
     * @param inv
     * @param rawSlot
     * @param newLore
     */
    public static void updateLore(Inventory inv, int rawSlot, List<String> newLore) {
        ItemStack itemStack = inv.getItem(rawSlot);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(newLore);
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Update the display name of an item in a GUI
     *
     * @param inv
     * @param rawSlot
     * @param name
     */
    public static void updateDisplayName(Inventory inv, int rawSlot, String name) {
        ItemStack itemStack = inv.getItem(rawSlot);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }


    /**
     * Set the receiver's skull in the GUI
     *
     * @param inv
     * @param player
     * @param slot
     * @param lore
     */
    public static ItemStack setPlayerSkull(Inventory inv, OfflinePlayer player, int slot, List<String> lore) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(ChatColor.YELLOW + player.getName());
        skullMeta.setLore(lore);

        itemStack.setItemMeta(skullMeta);

        inv.setItem(slot, itemStack);
        return itemStack;
    }

    /**
     * Update the online or offline status of the player skull in the main GUI
     *
     * @param inv
     * @param player
     * @param slot
     */
    public static void updateStatus(CMCEssentials CMCEssentials, Inventory inv, OfflinePlayer player, int slot) {
        ItemStack itemStack = inv.getItem(slot);
        ItemMeta itemMeta = itemStack.getItemMeta();

        Bukkit.getScheduler().runTaskLater(CMCEssentials, () -> {
            if (player.isOnline()) {
                itemMeta.setLore(Arrays.asList(
                        ChatColor.GRAY + "The Receiver of the Gift!",
                        ChatColor.GRAY + "Current Status: ONLINE"
                ));
            } else {
                itemMeta.setLore(Arrays.asList(
                        ChatColor.GRAY + "The Receiver of the Gift!",
                        ChatColor.GRAY + "Current Status: OFFLINE"
                ));
            }

            itemStack.setItemMeta(itemMeta);
        }, 20);

    }

    /**
     * Set a custom skull in the GUI
     *
     * @param inv
     * @param textureString
     * @param slot
     */
    public static void setCustomSkull(Inventory inv, String textureString, int slot) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", textureString));
        Field field;
        try {
            field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        itemStack.setItemMeta(skullMeta);

        inv.setItem(slot, itemStack);
    }


}
