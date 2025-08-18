package ru.achesoldin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CheckManager {

    private final Achesoldin plugin;

    private static class CheckData {
        UUID moderator;
        long endTimeMillis;
        BukkitTask task;
    }

    private final Map<UUID, CheckData> checks = new HashMap<>();

    private final File playersFile;
    private final YamlConfiguration playersCfg;

    public CheckManager(Achesoldin plugin) {
        this.plugin = plugin;

        playersFile = new File(plugin.getDataFolder(), "players.yml");
        if (!playersFile.exists()) {
            try { playersFile.createNewFile(); } catch (IOException ignored) {}
        }
        playersCfg = YamlConfiguration.loadConfiguration(playersFile);
    }

    public boolean isUnderCheck(Player p) {
        return checks.containsKey(p.getUniqueId());
    }

    public void startCheck(Player moderator, Player target) {
        if (isUnderCheck(target)) return;

        boolean tp = plugin.getConfig().getBoolean("teleport-to-check-location", true);
        if (tp) {
            String worldName = plugin.getConfig().getString("check-location.world", "world");
            double x = plugin.getConfig().getDouble("check-location.x");
            double y = plugin.getConfig().getDouble("check-location.y");
            double z = plugin.getConfig().getDouble("check-location.z");
            World w = Bukkit.getWorld(worldName);
            if (w != null) {
                target.teleport(new Location(w, x, y, z));
            }
        }

        // Effects
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128, false, false));

        // Titles
        String title = colorize(plugin.getConfig().getString("messages.title-main", "&cПроверка на читы"));
        String sub = colorize(plugin.getConfig().getString("messages.title-sub", "&7Модератор: &e%moderator%")
                .replace("%moderator%", moderator.getName()));
        target.sendTitle(stripColorCodes(title), stripColorCodes(sub), 10, 60, 10);

        // Timer
        int seconds = plugin.getConfig().getInt("check-duration-seconds", 60);
        long end = System.currentTimeMillis() + (seconds * 1000L);

        CheckData data = new CheckData();
        data.moderator = moderator.getUniqueId();
        data.endTimeMillis = end;

        data.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            long left = Math.max(0, data.endTimeMillis - System.currentTimeMillis());
            int sec = (int) Math.ceil(left / 1000.0);

            String action = colorize(plugin.getConfig().getString("messages.actionbar-timer", "&eДо конца проверки: &6%seconds%s")
                    .replace("%seconds%", String.valueOf(sec)));

            // Paper: sendActionBar available
            target.sendActionBar(Component.text(stripColorCodes(action)));

            if (left <= 0) {
                // stop timer automatically (no auto-ban here)
                stopCheck(target, false);
            }
        }, 0L, 20L);

        checks.put(target.getUniqueId(), data);

        // Players.yml log
        String base = "players." + target.getName();
        playersCfg.set(base + ".checkedBy", moderator.getName());
        playersCfg.set(base + ".status", "В процессе");
        savePlayers();
    }

    public void stopCheck(Player target, boolean clearEffects) {
        CheckData data = checks.remove(target.getUniqueId());
        if (data != null && data.task != null) data.task.cancel();

        if (clearEffects) {
            target.removePotionEffect(PotionEffectType.BLINDNESS);
            target.removePotionEffect(PotionEffectType.SLOW);
            target.removePotionEffect(PotionEffectType.JUMP);
        }

        // update file
        String base = "players." + target.getName();
        playersCfg.set(base + ".status", "Завершена");
        savePlayers();
    }

    public void setDiscord(Player player, String discord) {
        String base = "players." + player.getName();
        playersCfg.set(base + ".discord", discord);
        savePlayers();
    }

    public void markBanned(Player player, String reason) {
        String base = "players." + player.getName();
        playersCfg.set(base + ".status", "Забанен");
        playersCfg.set(base + ".reason", reason);
        savePlayers();
    }

    public UUID getModerator(Player target) {
        CheckData data = checks.get(target.getUniqueId());
        return data == null ? null : data.moderator;
    }

    public void handleQuit(Player p) {
        if (!isUnderCheck(p)) return;
        String cmd = plugin.getConfig().getString("ban-command", "ban {player} {reason}")
                .replace("{player}", p.getName())
                .replace("{reason}", plugin.getConfig().getString("ban-reason-default", "sldn"));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);

        markBanned(p, "quit");

        String msg = colorize(plugin.getConfig().getString("messages.auto-ban-quit", "&cИгрок %player% вышел во время проверки и был автоматически забанен ({reason}).")
                .replace("%player%", p.getName()));
        Bukkit.broadcastMessage(stripColorCodes(msg));

        stopCheck(p, false);
    }

    private void savePlayers() {
        try { playersCfg.save(playersFile); } catch (IOException ignored) {}
    }

    private String colorize(String s) {
        return s == null ? "" : s.replace("&", "§");
    }

    private String stripColorCodes(String s) {
        // ActionBar & Title need plain text with paper Component.text; simple strip (§ codes)
        return s == null ? "" : s.replace("§", "");
    }
}
