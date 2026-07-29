package com.example.serverutils.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShulkerListener implements Listener {
    private final Map<UUID, Session> sessions = new HashMap<>();

    private static class Session {
        ItemStack item;
        int slot;
        Inventory source;
        boolean fromHand, mainHand;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player p = e.getPlayer();
        ItemStack m = p.getInventory().getItemInMainHand();
        ItemStack o = p.getInventory().getItemInOffHand();
        if (isShulker(m)) { e.setCancelled(true); open(p, m, true, true); }
        else if (isShulker(o)) { e.setCancelled(true); open(p, o, true, false); }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (e.getClick() != org.bukkit.event.inventory.ClickType.RIGHT) return;
        ItemStack cur = e.getCurrentItem();
        if (!isShulker(cur)) return;
        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true); p.updateInventory();
        Session s = new Session();
        s.item = cur.clone(); s.slot = e.getSlot(); s.source = e.getClickedInventory();
        s.fromHand = false; s.mainHand = false;
        open(p, s.item, false, false);
        sessions.put(p.getUniqueId(), s);
    }

    private void open(Player p, ItemStack item, boolean hand, boolean main) {
        BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
        ShulkerBox box = (ShulkerBox) meta.getBlockState();
        Inventory inv = Bukkit.createInventory(null, 27, net.kyori.adventure.text.Component.text("Shulker Box"));
        inv.setContents(box.getInventory().getContents());
        if (hand) {
            Session s = new Session();
            s.item = item.clone(); s.fromHand = true; s.mainHand = main;
            sessions.put(p.getUniqueId(), s);
        }
        p.openInventory(inv);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        Session s = sessions.remove(p.getUniqueId());
        if (s == null) return;
        BlockStateMeta meta = (BlockStateMeta) s.item.getItemMeta();
        ShulkerBox box = (ShulkerBox) meta.getBlockState();
        box.getInventory().setContents(e.getInventory().getContents());
        meta.setBlockState(box); s.item.setItemMeta(meta);
        if (s.fromHand) {
            if (s.mainHand) p.getInventory().setItemInMainHand(s.item);
            else p.getInventory().setItemInOffHand(s.item);
        } else if (s.source != null) {
            s.source.setItem(s.slot, s.item);
        }
    }

    private boolean isShulker(ItemStack i) {
        return i != null && i.getType().name().endsWith("SHULKER_BOX");
    }
}
