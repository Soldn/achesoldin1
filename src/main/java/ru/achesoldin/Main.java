package ru.achesoldin;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getServer().getConsoleSender().sendMessage("===============================");
        getServer().getConsoleSender().sendMessage(" Плагин Achesoldin загружен! ");
        getServer().getConsoleSender().sendMessage(" Разработчик: Soldi_n .jar ");
        getServer().getConsoleSender().sendMessage(" Версия: 1.0.0 ");
        getServer().getConsoleSender().sendMessage("===============================");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("[Achesoldin] Плагин отключён.");
    }

    public static Main getInstance() {
        return instance;
    }
}
