package me.approximations.orbitalEconomy.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String nick;
    private double balance;

    public void update() {
        //
    }

    public boolean has(double balance) {
        return this.balance >= balance;
    }
}
