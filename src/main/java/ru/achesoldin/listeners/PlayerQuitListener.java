package ru.achesoldin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.achesoldin.Achesoldin;

public class PlayerQuitListener implements Listener {

    private final Achesoldin plugin;

    public PlayerQuitListener(Achesoldin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        plugin.getCheckManager().handleQuit(e.getPlayer());
    }
}
