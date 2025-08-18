package ru.achesoldin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.achesoldin.Achesoldin;

public class SBanCommand implements CommandExecutor {

    private final Achesoldin plugin;

    public SBanCommand(Achesoldin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.usage-sban")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            String m = plugin.getConfig().getString("messages.player-not-found").replace("%player%", args[0]);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
            return true;
        }

        String reason = plugin.getConfig().getString("ban-reason-default", "sldn");
        String cmdText = plugin.getConfig().getString("ban-command", "ban {player} {reason}")
                .replace("{player}", target.getName()).replace("{reason}", reason);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmdText);
        plugin.getCheckManager().markBanned(target, reason);
        plugin.getCheckManager().stopCheck(target, false);

        String mm = plugin.getConfig().getString("messages.banned-moderator").replace("%player%", target.getName());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', mm));
        return true;
    }
}
