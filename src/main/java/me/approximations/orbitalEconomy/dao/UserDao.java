package me.approximations.orbitalEconomy.dao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.approximations.orbitalEconomy.dao.repository.UserRepository;
import me.approximations.orbitalEconomy.model.User;
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
    private final Map<String, User> users = new LinkedHashMap<>();
    public void insertOrUpdate(User user) {
        if(contains(user.getNick())) {
            update(user);
        }else {
            insert(user);
        }
    }

    public void insert(User user) {
        users.put(user.getNick(), user);
    }

    public void update(User user) {
        users.replace(user.getNick(), user);
    }

    public void remove(String nick) {
        users.remove(nick);
    }

    public boolean contains(String nick) {
        return users.containsKey(nick);
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