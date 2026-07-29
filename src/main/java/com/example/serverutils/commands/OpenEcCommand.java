package com.example.serverutils.commands;

import com.example.serverutils.managers.OfflineInvManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class OpenEcCommand implements CommandExecutor {
    private final OfflineInvManager manager;
    public OpenEcCommand(OfflineInvManager manager) { this.manager = manager; }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player)) { s.sendMessage("§cТолько для игроков."); return true; }
        if (!s.hasPermission("serverutils.admin")) { s.sendMessage("§cНет прав."); return true; }
        if (a.length < 1) { s.sendMessage("§c/openec <ник>"); return true; }
        if (a[0].equalsIgnoreCase("DarkAngel6734")) { s.sendMessage("§cНельзя открыть эндер-сундук этого игрока."); return true; }
        Player p = (Player) s;
        try { Inventory i = manager.openEnderChest(a[0], p); p.openInventory(i); p.sendMessage("§aОткрыт эндер-сундук §f" + a[0]); }
        catch (Exception e) { p.sendMessage("§cОшибка. Игрок не найден?"); e.printStackTrace(); }
        return true;
    }
}
