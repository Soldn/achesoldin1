package ru.achesoldin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.achesoldin.commands.SBanCommand;
import ru.achesoldin.commands.SCheckCommand;
import ru.achesoldin.commands.SDSCommand;
import ru.achesoldin.commands.SReleaseCommand;
import ru.achesoldin.listeners.PlayerQuitListener;

public class Achesoldin extends JavaPlugin {

    private CheckManager checkManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Banner
        FileConfiguration cfg = getConfig();
        String dev = cfg.getString("developer-name", "Unknown");
        getLogger().info("================================");
        getLogger().info(" Плагин Achesoldin загружен!");
        getLogger().info(" Разработчик: " + dev);
        getLogger().info(" Версия: " + getDescription().getVersion());
        getLogger().info("================================");

        this.checkManager = new CheckManager(this);

        getCommand("scheck").setExecutor(new SCheckCommand(this));
        getCommand("sds").setExecutor(new SDSCommand(this));
        getCommand("srelease").setExecutor(new SReleaseCommand(this));
        getCommand("sban").setExecutor(new SBanCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }

    public CheckManager getCheckManager() {
        return checkManager;
    }
}
