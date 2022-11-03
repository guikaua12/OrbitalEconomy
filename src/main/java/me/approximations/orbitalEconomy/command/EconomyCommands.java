package me.approximations.orbitalEconomy.command;

import lombok.RequiredArgsConstructor;
import me.approximations.orbitalEconomy.manager.TransactionManager;
import me.approximations.orbitalEconomy.model.TransactionRequest;
import me.approximations.orbitalEconomy.model.TransactionResult;
import me.approximations.orbitalEconomy.util.CooldownUtils;
import me.approximations.orbitalEconomy.util.NumberUtils;
import me.approximations.orbitalEconomy.util.RandomUtils;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class EconomyCommands {
    private final TransactionManager transactionManager;

    @Command(name = "bal",
            usage = "/bal [username]"
    )
    public void balCommand(Context<Player> e, @Optional OfflinePlayer target) {
        Player p = e.getSender();
        if(e.argsCount() < 1) {
            TransactionRequest request = transactionManager.getBalance(e.getSender());
            p.sendMessage(ChatColor.GREEN+"Your balance is: "+ NumberUtils.format(request.getAmount()));
        }else {
            TransactionRequest request = transactionManager.getBalance(target);
            switch (request.getResult()) {
                case SUCCESS:
                    p.sendMessage(ChatColor.GREEN+target.getName()+"'s balance is: "+ NumberUtils.format(request.getAmount()));
                    break;
                case INVALID_TARGET:
                    p.sendMessage(ChatColor.RED+"Invalid target.");
                    break;
            }
        }
    }

    @Command(name = "give",
            usage = "/give <username> <amount>"
    )
    public void giveCommand(Context<Player> e, OfflinePlayer target, String amount) {
        Player p = e.getSender();

        double am = NumberUtils.parse(amount);
        TransactionRequest withdraw = transactionManager.withdraw(p, am);
        if (withdraw.getResult() == TransactionResult.NOT_ENOUGH_BALANCE) {
            p.sendMessage(ChatColor.RED + "You don't have enough balance.");
            return;
        }
        TransactionRequest deposit = transactionManager.deposit(target, am);
        switch (deposit.getResult()) {
            case SUCCESS:
                p.sendMessage(ChatColor.GREEN+"You deposited $"+target.getName()+" to "+amount+".");
                break;
            case INVALID_TARGET:
                p.sendMessage(ChatColor.RED+"Invalid target.");
                break;
            case INVALID_AMOUNT:
                p.sendMessage(ChatColor.RED+"Invalid amount.");
                break;
        }
    }

    @Command(name = "setbal",
            usage = "/setbal <username> <amount>"
    )
    public void setbalCommand(Context<Player> e, OfflinePlayer target, String amount) {
        Player p = e.getSender();
        if(!p.isOp()) {
            p.sendMessage(ChatColor.RED+"You don't have permission for this command.");
            return;
        }

        double am = NumberUtils.parse(amount);
        TransactionRequest deposit = transactionManager.set(target, am);
        switch (deposit.getResult()) {
            case SUCCESS:
                p.sendMessage(ChatColor.GREEN+"You set the "+target.getName()+"'s balance to "+amount+".");
                break;
            case INVALID_TARGET:
                p.sendMessage(ChatColor.RED+"Invalid target.");
                break;
            case INVALID_AMOUNT:
                p.sendMessage(ChatColor.RED+"Invalid amount.");
                break;
        }
    }

    @Command(name = "earn",
            usage = "/earn"
    )
    public void earnCommand(Context<Player> e) {
        Player p = e.getSender();
        if(!CooldownUtils.temDelay(p.getName())) {
            int earned = RandomUtils.random(1, 5);
            TransactionRequest request = transactionManager.deposit(p, earned);
            p.sendMessage(ChatColor.GREEN+"You earned "+earned+" coins.");
            CooldownUtils.addDelay(p.getName());
        }else {
            if(!CooldownUtils.acabouDelay(p.getName(), TimeUnit.MINUTES.toMillis(1))) {
                p.sendMessage(ChatColor.RED+"You have to wait more "+ CooldownUtils.getDelayString(p.getName(), TimeUnit.MINUTES.toMillis(1))+" to use this command.");
            }else {
                CooldownUtils.removeDelay(p.getName());
                int earned = RandomUtils.random(1, 5);
                TransactionRequest request = transactionManager.deposit(p, earned);
                p.sendMessage(ChatColor.GREEN+"You earned "+earned+" coins.");
            }
        }
    }
}
