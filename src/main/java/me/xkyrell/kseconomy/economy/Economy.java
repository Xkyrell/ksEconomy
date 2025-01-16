package me.xkyrell.kseconomy.economy;

import net.milkbowl.vault.economy.EconomyResponse;

public interface Economy {

    String getName();

    String getSymbol();

    double getBalance();

    void setBalance(double balance);

    double getMaxBalance();

    EconomyResponse withdraw(double amount);

    EconomyResponse deposit(double amount);

    EconomyPluralizer getPluralizer();

    int getFractionalDigits();

    String format(double value);

    default boolean hasEnoughBalance(double amount) {
        return getBalance() >= amount;
    }

    default String format() {
        return format(getBalance());
    }
}
