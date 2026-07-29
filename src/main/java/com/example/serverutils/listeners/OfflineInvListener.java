package com.example.serverutils.listeners;

import com.example.serverutils.managers.OfflineInvManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class OfflineInvListener implements Listener {
    private final OfflineInvManager manager;
    public OfflineInvListener(OfflineInvManager manager) { this.manager = manager; }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        if (manager.isManaging(p)) manager.save(p);
    }
}
