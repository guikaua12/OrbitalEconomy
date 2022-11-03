package me.approximations.orbitalEconomy;

import com.jaoow.sql.executor.SQLExecutor;
import lombok.Getter;
import me.approximations.orbitalEconomy.command.EconomyCommands;
import me.approximations.orbitalEconomy.dao.SQLProvider;
import me.approximations.orbitalEconomy.dao.UserDao;
import me.approximations.orbitalEconomy.dao.adapter.UserAdapter;
import me.approximations.orbitalEconomy.dao.repository.UserRepository;
import me.approximations.orbitalEconomy.dao.scheduler.AutoSave;
import me.approximations.orbitalEconomy.manager.TransactionManager;
import me.approximations.orbitalEconomy.model.User;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Getter
    private static Main instance;

    @Getter
    private static SQLProvider sqlProvider;

    @Getter
    private static UserRepository userRepository;

    @Getter
    private static UserDao userDao;

    @Getter
    private static TransactionManager transactionManager;

    private BukkitFrame bukkitFrame;

    @Override
    public void onEnable() {
        instance = this;

        setupDatabase();
        transactionManager = new TransactionManager(userDao, userRepository);
        setupCommand();
    }

    private void setupCommand() {
        bukkitFrame = new BukkitFrame(this);
        bukkitFrame.registerCommands(new EconomyCommands(transactionManager));
    }

    private void setupDatabase() {
        SQLProvider sqlProvider = new SQLProvider(this);
        SQLExecutor sqlExecutor = sqlProvider.setupDatabase();
        UserAdapter userAdapter = new UserAdapter();
        sqlExecutor.registerAdapter(User.class, userAdapter);
        userRepository = new UserRepository(this, sqlExecutor);
        userRepository.createTable();

        userDao = new UserDao(userRepository, this);

        sqlProvider.registerEvents();

        AutoSave autoSave = new AutoSave(this);
    }
}