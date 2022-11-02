package me.approximations.parkourPlugin.dao.adapter;

import com.jaoow.sql.executor.adapter.SQLResultAdapter;
import me.approximations.parkourPlugin.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserAdapter implements SQLResultAdapter<User> {
    @Override
    public User adaptResult(ResultSet rs) {
        UUID uuid = null;
        long bestTime = -1;
        try {
            uuid = UUID.fromString(rs.getString("uuid"));
            bestTime = rs.getLong("bestTime");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new User(uuid, bestTime);
    }
}
