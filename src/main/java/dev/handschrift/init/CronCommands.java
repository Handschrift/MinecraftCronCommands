package dev.handschrift.init;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CronCommands extends JavaPlugin {
    @Override
    public void onEnable() {
        final FileConfiguration config = this.getConfig();
        config.addDefault("command", "say hello");
        config.addDefault("time", "16:00");
        config.addDefault("checking delay", 20);
        config.options().copyDefaults(true);
        this.saveConfig();
        final DateFormat sdf = new SimpleDateFormat("HH:mm");
        final String command = config.getString("command");
        final String time = config.getString("time");
        final int delay = config.getInt("checking delay");
        this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            boolean run = false;

            @Override
            public void run() {
                try {
                    final LocalDateTime date = LocalDateTime.ofInstant(sdf.parse(time).toInstant(), ZoneId.systemDefault());
                    final LocalDateTime now = LocalDateTime.now();
                    if (now.getHour() == date.getHour() && now.getMinute() == date.getMinute()) {
                        if (!run) {
                            if (command == null)
                                return;
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                            run = true;
                        }
                    } else {
                        run = false;
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0L, delay);

    }
}
