package ru.achesoldin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.achesoldin.Achesoldin;

public class SReleaseCommand implements CommandExecutor {

    private final Achesoldin plugin;

    public SReleaseCommand(Achesoldin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.usage-srelease")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            String m = plugin.getConfig().getString("messages.player-not-found").replace("%player%", args[0]);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
            return true;
        }

        if (!plugin.getCheckManager().isUnderCheck(target)) {
            String m = plugin.getConfig().getString("messages.not-under-check").replace("%player%", target.getName());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
            return true;
        }

        plugin.getCheckManager().stopCheck(target, true);

        target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.released-player")));

        String mm = plugin.getConfig().getString("messages.released-moderator").replace("%player%", target.getName());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', mm));
        return true;
    }
}
