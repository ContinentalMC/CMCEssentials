package com.yopal.continentalmc;

import com.yopal.continentalmc.commands.CMCCommand;
import com.yopal.continentalmc.commands.CMCTabCompleter;
import com.yopal.continentalmc.gambling.bingo.listeners.BingoGUIListener;
import com.yopal.continentalmc.gambling.bingo.managers.BingoGUIManager;
import com.yopal.continentalmc.gambling.henry.listeners.PlayerCloseHenryListener;
import com.yopal.continentalmc.gambling.henry.listeners.PlayerInteractHenryListener;
import com.yopal.continentalmc.gambling.henry.listeners.TokenBuyListener;
import com.yopal.continentalmc.gambling.listeners.PlayerDamageFireworkListener;
import com.yopal.continentalmc.gambling.machines.horsebet.HorseBetGUIInteractListener;
import com.yopal.continentalmc.gambling.machines.impostor.ImpostorGUIInteractListener;
import com.yopal.continentalmc.gambling.machines.listeners.InteractMachineListener;
import com.yopal.continentalmc.gambling.machines.listeners.MachineCloseListener;
import com.yopal.continentalmc.gambling.machines.platforms.PlatformsGUIInteractListener;
import com.yopal.continentalmc.gambling.machines.rockpaperscissors.RPSGUIInteractListener;
import com.yopal.continentalmc.gambling.machines.slots.SlotGUIInteractListener;
import com.yopal.continentalmc.listeners.PlayerChatListener;
import com.yopal.continentalmc.listeners.PlayerEmojiListener;
import com.yopal.continentalmc.listeners.PlayerJoinListener;
import com.yopal.continentalmc.listeners.PlayerLeaveListener;
import com.yopal.continentalmc.managers.RecipeManager;
import com.yopal.continentalmc.managers.YML.CasinoManager;
import com.yopal.continentalmc.managers.YML.ConfigManager;
import com.yopal.continentalmc.managers.YML.EmojiManager;
import com.yopal.continentalmc.managers.YML.ScoreManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
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
        CasinoManager.setupFile(this);

        // commands
        getCommand("cmc").setExecutor(new CMCCommand(this));
        getCommand("cmc").setTabCompleter(new CMCTabCompleter());

        // listeners
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEmojiListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageFireworkListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractHenryListener(this), this);
        Bukkit.getPluginManager().registerEvents(new TokenBuyListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCloseHenryListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractMachineListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MachineCloseListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SlotGUIInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ImpostorGUIInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new HorseBetGUIInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlatformsGUIInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RPSGUIInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BingoGUIListener(this), this);

        if (!setupEconomy() ) {
            getLogger().severe(String.format("Disabled due to no Vault dependency found!"));
            getServer().getPluginManager().disablePlugin(this);
        } else {
            getLogger().info("Vault dependency found!");
        }

        if (CasinoManager.getBingoTotal() >= CasinoManager.getBingoMaxOut()) {
            BingoGUIManager.startBingo(this);
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
