package me.xkyrell.kseconomy.economy;

import net.milkbowl.vault.economy.EconomyResponse;

public interface Economy {

    String getName();

    String getSymbol();

    double getBalance();

    EconomyResponse withdraw(double amount);

    EconomyResponse deposit(double amount);

    EconomyPluralizer getPluralizer();

    String format(double value);

    default boolean hasEnoughBalance(double amount) {
        return getBalance() >= amount;
    }

    default String format() {
        return format(getBalance());
    }
}
