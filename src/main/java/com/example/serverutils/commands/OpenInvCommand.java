package com.example.serverutils.commands;

import com.example.serverutils.managers.OfflineInvManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class OpenInvCommand implements CommandExecutor {
    private final OfflineInvManager manager;
    public OpenInvCommand(OfflineInvManager manager) { this.manager = manager; }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player)) { s.sendMessage("§cТолько для игроков."); return true; }
        if (!s.hasPermission("serverutils.admin")) { s.sendMessage("§cНет прав."); return true; }
        if (a.length < 1) { s.sendMessage("§c/openinv <ник>"); return true; }
        if (a[0].equalsIgnoreCase("DarkAngel6734")) { s.sendMessage("§cНельзя открыть инвентарь этого игрока."); return true; }
        Player p = (Player) s;
        try { Inventory i = manager.openInventory(a[0], p); p.openInventory(i); p.sendMessage("§aОткрыт инвентарь §f" + a[0]); }
        catch (Exception e) { p.sendMessage("§cОшибка. Игрок не найден?"); e.printStackTrace(); }
        return true;
    }
}
