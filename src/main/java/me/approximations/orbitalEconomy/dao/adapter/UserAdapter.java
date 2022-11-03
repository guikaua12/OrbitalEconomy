package me.approximations.orbitalEconomy.dao.adapter;

import com.jaoow.sql.executor.adapter.SQLResultAdapter;
import me.approximations.orbitalEconomy.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserAdapter implements SQLResultAdapter<User> {
    @Override
    public User adaptResult(ResultSet rs) {
        try {
            String nick = rs.getString("nick");
            double balance = rs.getDouble("balance");
            return new User(nick, balance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
