package me.approximations.parkourPlugin.dao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.approximations.parkourPlugin.dao.repository.UserRepository;
import me.approximations.parkourPlugin.model.User;
import org.bukkit.plugin.Plugin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class UserDao {
    @Getter
    private final UserRepository userRepository;
    private final Plugin plugin;
    @Getter
    private final Map<UUID, User> users = new LinkedHashMap<>();
    public void insertOrUpdate(User user) {
        if(contains(user.getUuid())) {
            update(user);
        }else {
            insert(user);
        }
    }

    public void insert(User user) {
        users.put(user.getUuid(), user);
    }

    public void update(User user) {
        users.replace(user.getUuid(), user);
    }

    public void remove(UUID uuid) {
        users.remove(uuid);
    }

    public boolean contains(UUID uuid) {
        return users.containsKey(uuid);
    }

    public void getAll() {
        userRepository.getAll().forEach(user -> {
            insertOrUpdate(user);
        });
    }

    public void saveAll() {
        users.forEach((nick, user) -> {
            userRepository.insertOrUpdate(user);
        });
    }

    public void saveOne(User user) {
        userRepository.insertOrUpdate(user);
    }
}