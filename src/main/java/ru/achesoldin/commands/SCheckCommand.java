package ru.achesoldin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.achesoldin.AcheSoldinPlugin;

public class SCheckCommand implements CommandExecutor {
    private final AcheSoldinPlugin plugin;

    public SCheckCommand(AcheSoldinPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /scheck <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        String worldName = plugin.getConfig().getString("check-location.world");
        double x = plugin.getConfig().getDouble("check-location.x");
        double y = plugin.getConfig().getDouble("check-location.y");
        double z = plugin.getConfig().getDouble("check-location.z");

        if (Bukkit.getWorld(worldName) == null) {
            sender.sendMessage("§cWorld " + worldName + " not found.");
            return true;
        }

        Location loc = new Location(Bukkit.getWorld(worldName), x, y, z);
        target.teleport(loc);

        String msg = plugin.getConfig().getString("messages.check-start")
                .replace("%moderator%", sender.getName());
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));

        return true;
    }
}
