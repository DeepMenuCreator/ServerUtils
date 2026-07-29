package com.example.serverutils;

import com.example.serverutils.commands.OpenEcCommand;
import com.example.serverutils.commands.OpenInvCommand;
import com.example.serverutils.listeners.BreakYourBlockListener;
import com.example.serverutils.listeners.OfflineInvListener;
import com.example.serverutils.listeners.ShulkerListener;
import com.example.serverutils.managers.OfflineInvManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerUtils extends JavaPlugin {
    private static ServerUtils instance;
    private OfflineInvManager offlineInvManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        offlineInvManager = new OfflineInvManager();
        getServer().getPluginManager().registerEvents(new ShulkerListener(), this);
        getServer().getPluginManager().registerEvents(new OfflineInvListener(offlineInvManager), this);
        getServer().getPluginManager().registerEvents(new BreakYourBlockListener(), this);
        getCommand("openinv").setExecutor(new OpenInvCommand(offlineInvManager));
        getCommand("openec").setExecutor(new OpenEcCommand(offlineInvManager));
        getLogger().info("ServerUtils включен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ServerUtils выключен!");
    }

    public static ServerUtils getInstance() { return instance; }
    public OfflineInvManager getOfflineInvManager() { return offlineInvManager; }
}
