package me.approximations.parkourPlugin.dao.listener;

import me.approximations.parkourPlugin.Main;
import me.approximations.parkourPlugin.dao.UserDao;
import me.approximations.parkourPlugin.dao.repository.UserRepository;
import me.approximations.parkourPlugin.model.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveEvent implements Listener {
    private final Main plugin = Main.getInstance();
    private final UserDao userDao = plugin.getUserDao();
    private final UserRepository userRepository = plugin.getUserRepository();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            User user = userRepository.get(p.getUniqueId());
            if(user == null) {
                userDao.insert(new User(p.getUniqueId(), -1));
                return;
            }
            userDao.insert(user);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            User user = userDao.getUsers().get(p.getUniqueId());
            userRepository.insertOrUpdate(user);
            userDao.remove(p.getUniqueId());
        });
    }
}
