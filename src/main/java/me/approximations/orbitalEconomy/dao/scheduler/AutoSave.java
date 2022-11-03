package me.approximations.orbitalEconomy.dao.scheduler;

import me.approximations.orbitalEconomy.Main;
import me.approximations.orbitalEconomy.dao.UserDao;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSave extends BukkitRunnable {
    private final Main plugin;
    private final UserDao userDao = Main.getUserDao();
    public AutoSave(Main plugin) {
        this.plugin = plugin;
        runTaskTimerAsynchronously(plugin, 600*20, 600*20);
    }

    @Override
    public void run() {
        long before = System.currentTimeMillis();
        userDao.saveAll();
        long now = System.currentTimeMillis();
        long total = now - before;
        plugin.getLogger().info("Auto save completed in "+total+" ms.");
    }
}
