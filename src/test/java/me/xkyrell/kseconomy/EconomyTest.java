package me.xkyrell.kseconomy;

import me.xkyrell.kseconomy.economy.Economy;
import me.xkyrell.kseconomy.economy.EconomyPluralizer;
import me.xkyrell.kseconomy.economy.impl.SimpleEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class EconomyTest {

    private Economy economy;

    @BeforeEach
    public void setUp() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat format = new DecimalFormat("#,##0.00", symbols);
        EconomyPluralizer pluralizer = new EconomyPluralizer(
                "coin", new String[] { "coins" },
                value -> value == 1 ? "coin" : "coins"
        );

        economy = new SimpleEconomy(
                "TestEconomy", "*", pluralizer,
                format, 125, 1000
        );
    }

    @Test
    void testDepositValid() {
        EconomyResponse response = economy.deposit(50.0);
        assertEquals(50.0, response.amount);
        assertEquals(175.0, response.balance);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertTrue(response.errorMessage.isEmpty());
    }

    @Test
    void testDepositInvalid() {
        EconomyResponse response = economy.deposit(Double.POSITIVE_INFINITY);
        assertEquals(0.0, response.amount);
        assertEquals(0.0, response.balance);
        assertEquals(EconomyResponse.ResponseType.FAILURE, response.type);
        assertEquals("Invalid amount format", response.errorMessage);
    }

    @Test
    void testDepositMaxBalance() {
        EconomyResponse response = economy.deposit(2500.0);
        assertEquals(2500.0, response.amount);
        assertEquals(125.0, response.balance);
        assertEquals(EconomyResponse.ResponseType.FAILURE, response.type);
        assertEquals("Exceeds max balance limit", response.errorMessage);
    }

    @Test
    void testWithdrawValid() {
        EconomyResponse response = economy.withdraw(50.0);
        assertEquals(50.0, response.amount);
        assertEquals(75.0, response.balance);
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertTrue(response.errorMessage.isEmpty());
    }

    @Test
    void testWithdrawInvalid() {
        EconomyResponse response = economy.withdraw(Double.NaN);
        assertEquals(0.0, response.amount);
        assertEquals(0.0, response.balance);
        assertEquals(EconomyResponse.ResponseType.FAILURE, response.type);
        assertEquals("Invalid amount format", response.errorMessage);
    }

    @Test
    void testWithdrawInsufficientFunds() {
        EconomyResponse response = economy.withdraw(560.25);
        assertEquals(560.25, response.amount);
        assertEquals(125.0, response.balance);
        assertEquals(EconomyResponse.ResponseType.FAILURE, response.type);
        assertEquals("Insufficient funds", response.errorMessage);
    }

    @Test
    void testFormat() {
        assertEquals("1.00coin", economy.format(1.0));
        assertEquals("10,000.00coins", economy.format(10000.0));
    }
}
