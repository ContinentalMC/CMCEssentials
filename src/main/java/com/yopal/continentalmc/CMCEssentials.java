package com.yopal.continentalmc;

import com.yopal.continentalmc.commands.CMCCommand;
import com.yopal.continentalmc.commands.CMCTabCompleter;
import com.yopal.continentalmc.listeners.PlayerChatListener;
import com.yopal.continentalmc.listeners.PlayerEmojiListener;
import com.yopal.continentalmc.listeners.PlayerJoinListener;
import com.yopal.continentalmc.listeners.PlayerLeaveListener;
import com.yopal.continentalmc.managers.RecipeManager;
import com.yopal.continentalmc.managers.YML.ConfigManager;
import com.yopal.continentalmc.managers.YML.EmojiManager;
import com.yopal.continentalmc.managers.YML.ScoreManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CMCEssentials extends JavaPlugin {

    private static Economy econ = null;

    @Override
    public void onEnable() {
        // Plugin startup logic

        File folder = new File(getDataFolder(), "/");
        if (!folder.exists()) {
            folder.mkdir();
        }

        RecipeManager.modifyRecipes(this);

        // YML
        ConfigManager.setupConfig(this);
        EmojiManager.setupFile(this);
        ScoreManager.setupFile(this);

        // commands
        getCommand("cmc").setExecutor(new CMCCommand(this));
        getCommand("cmc").setTabCompleter(new CMCTabCompleter());

        // listeners
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEmojiListener(), this);

        if (!setupEconomy() ) {
            getLogger().severe(String.format("Disabled due to no Vault dependency found!"));
            getServer().getPluginManager().disablePlugin(this);
        } else {
            getLogger().info("Vault dependency found!");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
