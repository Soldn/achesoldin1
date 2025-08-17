package ru.achesoldin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.achesoldin.commands.SCheckCommand;
import ru.achesoldin.commands.SdsCommand;
import ru.achesoldin.listeners.PlayerQuitListener;

public class Achesoldin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getCommand("scheck").setExecutor(new SCheckCommand(this));
        getCommand("sds").setExecutor(new SdsCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        getLogger().info("Achesoldin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Achesoldin disabled!");
    }
}
