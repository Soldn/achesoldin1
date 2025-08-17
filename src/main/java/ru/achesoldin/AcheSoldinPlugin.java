package ru.achesoldin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.achesoldin.commands.SCheckCommand;

public class AcheSoldinPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("scheck").setExecutor(new SCheckCommand(this));
        getLogger().info("AcheSoldin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AcheSoldin disabled!");
    }
}
