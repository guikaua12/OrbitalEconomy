package me.approximations.parkourPlugin.dao.repository;

import com.jaoow.sql.executor.SQLExecutor;
import me.approximations.parkourPlugin.Main;
import me.approximations.parkourPlugin.model.User;

import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class UserRepository{
    private final Main plugin;
    private final SQLExecutor sqlExecutor;
    private final String TABLE;

    public UserRepository(Main plugin, SQLExecutor sqlExecutor) {
        this.plugin = plugin;
        this.sqlExecutor = sqlExecutor;
        this.TABLE = plugin.getConfig().getString("database.table");
    }



    public void createTable() {
        sqlExecutor.execute("CREATE TABLE IF NOT EXISTS "+TABLE+"(uuid VARCHAR(72), bestTime BIGINT);");
    }

    public void insertOrUpdate(User user) {
        if(contains(user.getUuid())) {
            update(user);
        }else {
            insert(user);
        }
    }

    public void insert(User user) {
        sqlExecutor.execute("INSERT INTO "+TABLE+" VALUES(?, ?);", c -> {
            try {
                c.setString(1, user.getUuid().toString());
                c.setLong(2, user.getBestTime());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void update(User user) {
        sqlExecutor.execute("UPDATE "+TABLE+" SET bestTime = ? WHERE uuid = ?;", c -> {
            try {
                c.setLong(1, user.getBestTime());
                c.setString(2, user.getUuid().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public User get(UUID uuid) {
        return sqlExecutor.query("SELECT * FROM "+TABLE+" WHERE uuid = ?;", c -> {
            try {
                c.setString(1, uuid.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, User.class).orElse(null);
    }

    public boolean contains(UUID uuid) {
        return get(uuid) != null;
    }

    public void delete(UUID uuid) {
        sqlExecutor.execute("DELETE FROM "+TABLE+" WHERE uuid = ?;", c -> {
            try {
                c.setString(1, uuid.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public Set<User> getAll() {
        return sqlExecutor.queryMany("SELECT * FROM "+TABLE+";", User.class);
    }

    public Set<User> getTop5() {
        return sqlExecutor.queryMany("SELECT * FROM "+TABLE+" ORDER BY bestTime ASC LIMIT 5;", User.class);
    }
}
