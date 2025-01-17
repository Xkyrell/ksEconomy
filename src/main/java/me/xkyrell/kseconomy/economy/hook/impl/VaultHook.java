package me.xkyrell.kseconomy.economy.hook.impl;

import lombok.RequiredArgsConstructor;
import me.xkyrell.kseconomy.economy.*;
import me.xkyrell.kseconomy.economy.hook.Hook;
import me.xkyrell.kseconomy.player.impl.EconomyOnlinePlayer;
import me.xkyrell.kseconomy.player.service.PlayerService;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public final class VaultHook implements Hook {

    private final PlayerService playerService;
    private final EconomyResolver economyResolver;

    @Override
    public void register(Plugin plugin) throws Throwable {
        economyResolver.getEconomies().forEach(economy -> {
            plugin.getServer().getServicesManager().register(
                    net.milkbowl.vault.economy.Economy.class, new EconomyHandler(economy),
                    plugin, ServicePriority.High
            );
        });
    }

    @Override
    public void unregister(Plugin plugin) throws Throwable {
        ServicesManager manager = plugin.getServer().getServicesManager();
        manager.getRegistrations(net.milkbowl.vault.economy.Economy.class).stream()
                .filter(provider -> provider.getPlugin().equals(plugin))
                .map(RegisteredServiceProvider::getProvider)
                .forEach(manager::unregister);
    }

    @RequiredArgsConstructor
    private final class EconomyHandler extends AbstractEconomy {

        private final Economy economy;

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public String getName() {
            return economy.getName();
        }

        @Override
        public boolean hasBankSupport() {
            return false;
        }

        @Override
        public int fractionalDigits() {
            return economy.getFractionalDigits();
        }

        @Override
        public String format(double amount) {
            return economy.format(amount);
        }

        @Override
        public String currencyNamePlural() {
            return economy.getPluralizer().plural()[0];
        }

        @Override
        public String currencyNameSingular() {
            return economy.getPluralizer().singular();
        }

        @Override
        public boolean hasAccount(String playerName) {
            return playerService.getResolver().resolve(playerName).isPresent();
        }

        @Override
        public boolean hasAccount(String playerName, String worldName) {
            return hasAccount(playerName);
        }

        @Override
        public double getBalance(String playerName) {
            return lookupPlayerEconomy(playerName)
                    .map(Economy::getBalance)
                    .orElse(0.0);
        }

        @Override
        public double getBalance(String playerName, String worldName) {
            return getBalance(playerName);
        }

        @Override
        public boolean has(String playerName, double amount) {
            return lookupPlayerEconomy(playerName)
                    .map(e -> e.hasEnoughBalance(amount))
                    .orElse(false);
        }

        @Override
        public boolean has(String playerName, String worldName, double amount) {
            return has(playerName, amount);
        }

        @Override
        public EconomyResponse withdrawPlayer(String playerName, double amount) {
            if (!hasAccount(playerName)) {
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account not found");
            }

            return lookupPlayerEconomy(playerName)
                    .map(economy -> economy.withdraw(amount))
                    .orElseGet(() -> new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Player not found"));
        }

        @Override
        public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
            return withdrawPlayer(playerName, amount);
        }

        @Override
        public EconomyResponse depositPlayer(String playerName, double amount) {
            if (!hasAccount(playerName)) {
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Account not found");
            }

            return lookupPlayerEconomy(playerName)
                    .map(economy -> economy.withdraw(amount))
                    .orElseGet(() -> new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Player not found"));
        }

        @Override
        public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
            return depositPlayer(playerName, amount);
        }

        @Override
        public EconomyResponse createBank(String bankName, String playerName) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
        }

        @Override
        public EconomyResponse deleteBank(String playerName) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
        }

        @Override
        public EconomyResponse bankBalance(String playerName) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
        }

        @Override
        public EconomyResponse bankHas(String playerName, double amount) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
        }

        @Override
        public EconomyResponse bankWithdraw(String playerName, double amount) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
        }

        @Override
        public EconomyResponse bankDeposit(String playerName, double amount) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
        }

        @Override
        public EconomyResponse isBankOwner(String bankName, String playerName) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
        }

        @Override
        public EconomyResponse isBankMember(String bankName, String playerName) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
        }

        @Override
        public List<String> getBanks() {
            return Collections.emptyList();
        }

        @Override
        public boolean createPlayerAccount(String playerName) {
            if (hasAccount(playerName)) {
                return false;
            }

            playerService.register(playerName, EconomyOnlinePlayer.class);
            return true;
        }

        @Override
        public boolean createPlayerAccount(String playerName, String worldName) {
            return createPlayerAccount(playerName);
        }

        private Optional<Economy> lookupPlayerEconomy(String playerName) {
            return playerService.getResolver().resolve(playerName)
                    .map(economyPlayer -> economyPlayer.getEconomyByName(getName()));
        }
    }
}
