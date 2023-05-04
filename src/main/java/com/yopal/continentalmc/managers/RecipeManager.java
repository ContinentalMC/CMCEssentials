package com.yopal.continentalmc.managers;

import com.yopal.continentalmc.CMCEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.*;

import java.util.List;

public class RecipeManager {
    public static void modifyRecipes(CMCEssentials cmc) {
        // removing bone recipes

        for (Recipe recipe : Bukkit.getRecipesFor(new ItemStack(Material.BONE))) {
            if (recipe instanceof Keyed) {
                Bukkit.removeRecipe(((Keyed) recipe).getKey());
            }
        }

        for (Recipe recipe : Bukkit.getRecipesFor(new ItemStack(Material.BONE_BLOCK))) {
            if (recipe instanceof Keyed) {
                Bukkit.removeRecipe(((Keyed) recipe).getKey());
            }
        }

        // adding our bone to bone block recipe
        NamespacedKey boneBlockKey = new NamespacedKey(cmc, "bone_block");
        ShapelessRecipe boneBlockRecipe = new ShapelessRecipe(boneBlockKey, new ItemStack(Material.BONE_BLOCK));

        for (int i = 0; i < 9; i++) {
            boneBlockRecipe.addIngredient(Material.BONE);
        }

        Bukkit.addRecipe(boneBlockRecipe);

        // adding bone block to bone recipe
        NamespacedKey boneKey = new NamespacedKey(cmc, "bone");
        ShapelessRecipe boneRecipe = new ShapelessRecipe(boneKey, new ItemStack(Material.BONE, 9));

        boneRecipe.addIngredient(Material.BONE_BLOCK);

        Bukkit.addRecipe(boneRecipe);

    }
}
