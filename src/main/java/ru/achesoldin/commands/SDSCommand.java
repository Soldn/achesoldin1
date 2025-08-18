package ru.achesoldin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.achesoldin.Achesoldin;

public class SDSCommand implements CommandExecutor {

    private final Achesoldin plugin;

    public SDSCommand(Achesoldin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.only-player"));
            return true;
        }
        Player p = (Player) sender;

        if (args.length < 1) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.provide-discord")));
            return true;
        }

        String discord = args[0];
        plugin.getCheckManager().setDiscord(p, discord);

        String msg = plugin.getConfig().getString("messages.discord-saved")
                .replace("%player%", p.getName())
                .replace("%discord%", discord);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        return true;
    }
}
