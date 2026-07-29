package com.example.serverutils.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OfflineInvManager {
    private final Map<UUID, Object> nmsPlayerMap = new HashMap<>();
    private final Map<UUID, Object> playerListMap = new HashMap<>();

    public Inventory openInventory(String targetName, Player viewer) throws Exception {
        OfflinePlayer offline = Bukkit.getOfflinePlayer(targetName);
        UUID uuid = offline.getUniqueId();
        Object craftServer = Bukkit.getServer();
        Object nmsServer = craftServer.getClass().getMethod("getServer").invoke(craftServer);
        Class<?> mcServerClass = nmsServer.getClass();
        Object playerList = mcServerClass.getMethod("getPlayerList").invoke(nmsServer);
        Class<?> serverLevelClass = Class.forName("net.minecraft.server.level.ServerLevel");
        Object serverLevel = mcServerClass.getMethod("overworld").invoke(nmsServer);
        Class<?> gpClass = Class.forName("com.mojang.authlib.GameProfile");
        Object gp = gpClass.getConstructor(UUID.class, String.class).newInstance(uuid, targetName);
        Class<?> ciClass = Class.forName("net.minecraft.server.level.ClientInformation");
        Object ci = ciClass.getMethod("createDefault").invoke(null);
        Class<?> spClass = Class.forName("net.minecraft.server.level.ServerPlayer");
        Object sp = spClass.getConstructor(mcServerClass, serverLevelClass, gpClass, ciClass)
            .newInstance(nmsServer, serverLevel, gp, ci);
        findMethod(playerList.getClass(), "load", 1).invoke(playerList, sp);
        Object nmsInv = spClass.getMethod("getInventory").invoke(sp);
        Class<?> cipClass = Class.forName("org.bukkit.craftbukkit.inventory.CraftInventoryPlayer");
        Class<?> nmsInvClass = Class.forName("net.minecraft.world.entity.player.Inventory");
        Inventory inv = (Inventory) cipClass.getConstructor(nmsInvClass).newInstance(nmsInv);
        nmsPlayerMap.put(viewer.getUniqueId(), sp);
        playerListMap.put(viewer.getUniqueId(), playerList);
        return inv;
    }

    public Inventory openEnderChest(String targetName, Player viewer) throws Exception {
        OfflinePlayer offline = Bukkit.getOfflinePlayer(targetName);
        UUID uuid = offline.getUniqueId();
        Object craftServer = Bukkit.getServer();
        Object nmsServer = craftServer.getClass().getMethod("getServer").invoke(craftServer);
        Class<?> mcServerClass = nmsServer.getClass();
        Object playerList = mcServerClass.getMethod("getPlayerList").invoke(nmsServer);
        Class<?> serverLevelClass = Class.forName("net.minecraft.server.level.ServerLevel");
        Object serverLevel = mcServerClass.getMethod("overworld").invoke(nmsServer);
        Class<?> gpClass = Class.forName("com.mojang.authlib.GameProfile");
        Object gp = gpClass.getConstructor(UUID.class, String.class).newInstance(uuid, targetName);
        Class<?> ciClass = Class.forName("net.minecraft.server.level.ClientInformation");
        Object ci = ciClass.getMethod("createDefault").invoke(null);
        Class<?> spClass = Class.forName("net.minecraft.server.level.ServerPlayer");
        Object sp = spClass.getConstructor(mcServerClass, serverLevelClass, gpClass, ciClass)
            .newInstance(nmsServer, serverLevel, gp, ci);
        findMethod(playerList.getClass(), "load", 1).invoke(playerList, sp);
        Object nmsEnder = spClass.getMethod("getEnderChestInventory").invoke(sp);
        Class<?> ciClass2 = Class.forName("org.bukkit.craftbukkit.inventory.CraftInventory");
        Class<?> contClass = Class.forName("net.minecraft.world.Container");
        Inventory inv = (Inventory) ciClass2.getConstructor(contClass).newInstance(nmsEnder);
        nmsPlayerMap.put(viewer.getUniqueId(), sp);
        playerListMap.put(viewer.getUniqueId(), playerList);
        return inv;
    }

    public void save(Player viewer) {
        Object sp = nmsPlayerMap.remove(viewer.getUniqueId());
        Object pl = playerListMap.remove(viewer.getUniqueId());
        if (sp == null || pl == null) return;
        try { findMethod(pl.getClass(), "save", 1).invoke(pl, sp); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public boolean isManaging(Player viewer) { return nmsPlayerMap.containsKey(viewer.getUniqueId()); }

    private Method findMethod(Class<?> c, String n, int p) {
        for (Method m : c.getMethods()) if (m.getName().equals(n) && m.getParameterCount() == p) return m;
        throw new RuntimeException("Method " + n + " not found");
    }
}
