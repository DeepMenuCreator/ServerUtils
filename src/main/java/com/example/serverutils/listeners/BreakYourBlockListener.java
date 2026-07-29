package com.example.serverutils.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BreakYourBlockListener implements Listener {
    private final Map<Location, UUID> placed = new HashMap<>();

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        placed.put(e.getBlock().getLocation(), e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onBreak(BlockBreakEvent e) {
        UUID u = placed.get(e.getBlock().getLocation());
        if (u != null && u.equals(e.getPlayer().getUniqueId())) {
            e.setCancelled(false);
            placed.remove(e.getBlock().getLocation());
        }
    }
}
