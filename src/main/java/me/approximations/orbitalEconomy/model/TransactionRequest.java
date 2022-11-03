package me.approximations.orbitalEconomy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TransactionRequest {
    private TransactionResult result;
    private double amount;
}
