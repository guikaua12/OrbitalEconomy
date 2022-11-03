package me.approximations.orbitalEconomy.manager;

import lombok.RequiredArgsConstructor;
import me.approximations.orbitalEconomy.dao.UserDao;
import me.approximations.orbitalEconomy.dao.repository.UserRepository;
import me.approximations.orbitalEconomy.model.TransactionRequest;
import me.approximations.orbitalEconomy.model.TransactionResult;
import me.approximations.orbitalEconomy.model.User;
import me.approximations.orbitalEconomy.util.NumberUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class TransactionManager {
    private final UserDao userDao;
    private final UserRepository userRepository;

    public TransactionRequest getBalance(OfflinePlayer target) {
        if(target == null) return new TransactionRequest(TransactionResult.INVALID_TARGET, 0);
        if(target.isOnline()) {
            User user = userDao.getUsers().get(target.getName());
            if(user == null) new TransactionRequest(TransactionResult.INVALID_TARGET, 0);
            return new TransactionRequest(TransactionResult.SUCCESS, user.getBalance());
        }
        else {
            User user = userRepository.get(target.getName());
            if(user == null) return new TransactionRequest(TransactionResult.INVALID_TARGET, 0);
            return new TransactionRequest(TransactionResult.SUCCESS, user.getBalance());
        }
    }

    public TransactionRequest withdraw(OfflinePlayer target, double amount) {
        if(target == null) return new TransactionRequest(TransactionResult.INVALID_TARGET, 0);
        if(NumberUtils.isInvalid(amount)) return new TransactionRequest(TransactionResult.INVALID_AMOUNT, 0);
        if(target.isOnline()) {
            User user = userDao.getUsers().get(target.getName());
            if(user == null) return new TransactionRequest(TransactionResult.INVALID_TARGET, 0);

            if(!user.has(amount)) return new TransactionRequest(TransactionResult.NOT_ENOUGH_BALANCE, 0);
            user.setBalance(Math.max(0, user.getBalance()-amount));
            return new TransactionRequest(TransactionResult.SUCCESS, amount);
        }else {
            User user = userRepository.get(target.getName());
            if(user == null) return new TransactionRequest(TransactionResult.INVALID_TARGET, 0);

            if(!user.has(amount)) return new TransactionRequest(TransactionResult.NOT_ENOUGH_BALANCE, 0);
            user.setBalance(Math.max(0, user.getBalance()-amount));
            userRepository.insertOrUpdate(user);
            return new TransactionRequest(TransactionResult.SUCCESS, amount);
        }
    }

    public TransactionRequest deposit(OfflinePlayer target, double amount) {
        if(target == null) return new TransactionRequest(TransactionResult.INVALID_TARGET, 0);
        if(NumberUtils.isInvalid(amount)) return new TransactionRequest(TransactionResult.INVALID_AMOUNT, 0);
        if(target.isOnline()) {
            User user = userDao.getUsers().get(target.getName());
            if(user == null) return new TransactionRequest(TransactionResult.INVALID_TARGET, 0);

            user.setBalance(user.getBalance()+amount);
            return new TransactionRequest(TransactionResult.SUCCESS, amount);
        }else {
            User user = userRepository.get(target.getName());
            if(user == null) return new TransactionRequest(TransactionResult.INVALID_TARGET, 0);

            user.setBalance(user.getBalance()+amount);
            userRepository.insertOrUpdate(user);
            return new TransactionRequest(TransactionResult.SUCCESS, amount);
        }
    }

    public TransactionRequest set(OfflinePlayer target, double amount) {
        if(target == null) return new TransactionRequest(TransactionResult.INVALID_TARGET, 0);
        if(NumberUtils.isInvalid(amount)) return new TransactionRequest(TransactionResult.INVALID_AMOUNT, 0);
        if(target.isOnline()) {
            User user = userDao.getUsers().get(target.getName());
            if(user == null) return new TransactionRequest(TransactionResult.INVALID_TARGET, 0);

            user.setBalance(amount);
            userRepository.insertOrUpdate(user);
            return new TransactionRequest(TransactionResult.SUCCESS, amount);
        }else {
            User user = userRepository.get(target.getName());
            if(user == null) return new TransactionRequest(TransactionResult.INVALID_TARGET, 0);

            user.setBalance(amount);
            userRepository.insertOrUpdate(user);
            return new TransactionRequest(TransactionResult.SUCCESS, amount);
        }
    }
}
