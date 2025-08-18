package ru.achesoldin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.achesoldin.Achesoldin;

public class SCheckCommand implements CommandExecutor {

    private final Achesoldin plugin;

    public SCheckCommand(Achesoldin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.only-player"));
            return true;
        }
        Player mod = (Player) sender;

        if (args.length < 1) {
            mod.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.usage-scheck")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            String m = plugin.getConfig().getString("messages.player-not-found").replace("%player%", args[0]);
            mod.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
            return true;
        }

        if (plugin.getCheckManager().isUnderCheck(target)) {
            String m = plugin.getConfig().getString("messages.already-under-check").replace("%player%", target.getName());
            mod.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
            return true;
        }

        plugin.getCheckManager().startCheck(mod, target);

        String pm = plugin.getConfig().getString("messages.check-start-player").replace("%moderator%", mod.getName());
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', pm));

        String mm = plugin.getConfig().getString("messages.check-start-moderator").replace("%player%", target.getName());
        mod.sendMessage(ChatColor.translateAlternateColorCodes('&', mm));

        // Prompt for discord
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.provide-discord")));
        return true;
    }
}
