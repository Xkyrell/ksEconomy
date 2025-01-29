package me.xkyrell.kseconomy.economy;

import lombok.NonNull;
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

    default String formatAsPluralize() {
        return formatAs(getPluralizer().mapper()
                .apply(getBalance())
        );
    }

    default String formatAsSymbol() {
        return formatAs(getSymbol());
    }

    default String formatAs(@NonNull String suffix) {
        return format().concat(" ").concat(suffix);
    }

    default String format() {
        return format(getBalance());
    }
}
