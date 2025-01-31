package me.xkyrell.kseconomy.economy.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.xkyrell.kseconomy.economy.*;
import net.milkbowl.vault.economy.EconomyResponse;
import java.text.DecimalFormat;

@Getter
@AllArgsConstructor
public class SimpleEconomy implements Economy {

    private final String name;
    private final String symbol;
    private final EconomyPluralizer pluralizer;
    @Getter(AccessLevel.NONE)
    private final DecimalFormat format;

    private double balance;
    private double maxBalance;

    @Override
    public EconomyResponse setBalance(double balance) {
        if (Double.isNaN(balance) || Double.isInfinite(balance) || balance < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Invalid amount format");
        }

        if (balance > maxBalance) {
            return new EconomyResponse(balance, getBalance(), EconomyResponse.ResponseType.FAILURE, "Exceeds max balance limit");
        }

        this.balance = balance;
        return new EconomyResponse(balance, getBalance(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdraw(double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Invalid amount format");
        }

        if (!hasEnoughBalance(amount)) {
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }

        balance -= amount;
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse deposit(double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Invalid amount format");
        }

        if (maxBalance < (balance + amount)) {
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, "Exceeds max balance limit");
        }

        balance += amount;
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public int getFractionalDigits() {
        return format.getMaximumFractionDigits();
    }

    @Override
    public String format(double value) {
        return format.format(value);
    }
}
