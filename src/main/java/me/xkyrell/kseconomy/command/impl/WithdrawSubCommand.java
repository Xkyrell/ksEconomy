package me.xkyrell.kseconomy.command.impl;

import me.xkyrell.kseconomy.command.AbstractSubCommand;
import me.xkyrell.kseconomy.command.registry.CommandRegistry;
import me.xkyrell.kseconomy.config.LanguageConfig;
import me.xkyrell.kseconomy.economy.Economy;
import me.xkyrell.kseconomy.economy.EconomyResolver;
import me.xkyrell.kseconomy.util.Numbers;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WithdrawSubCommand extends AbstractSubCommand {

    private final EconomyResolver economyResolver;
    private final CommandRegistry registry;
    private final LanguageConfig language;
    private final Plugin plugin;

    protected WithdrawSubCommand(EconomyResolver economyResolver, CommandRegistry registry, LanguageConfig language, Plugin plugin) {
        super("withdraw", 4);

        this.economyResolver = economyResolver;
        this.registry = registry;
        this.language = language;
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        registry.loadPlayerOrSelf(args[2], loadedPlayer -> {
            Economy foundedEconomy = loadedPlayer.getEconomyByName(args[1]);
            if (foundedEconomy == null) {
                String formattedEconomies = String.join(", ", getEconomies());
                Map<String, String> placeholders = Map.of("{economies}", formattedEconomies);
                sender.sendMessage(language.getPrefixedMsg("unknown-economy", placeholders));
                return;
            }

            Map<String, String> placeholders = Map.of(
                    "{balance_formatted_symbol}", foundedEconomy.formatAsSymbol(),
                    "{balance_formatted}", foundedEconomy.formatAsPluralize(),
                    "{sender}", sender.getName(),
                    "{receiver}", args[2]
            );

            Optional<Double> enteredAmount = Numbers.parseAmount(args[3]);
            if (enteredAmount.isEmpty()) {
                sender.sendMessage(language.getPrefixedMsg("not-double"));
                return;
            }

            EconomyResponse response = foundedEconomy.withdraw(enteredAmount.get());
            if (!response.transactionSuccess()) {
                sender.sendMessage(language.getPrefixedMsg("not-double"));
                return;
            }

            Player onlinePlayer = loadedPlayer.toBukkitPlayer().getPlayer();
            if (onlinePlayer != null) {
                onlinePlayer.sendMessage(language.getPrefixedMsg("withdraw-funds-receiver", placeholders));
            }
            sender.sendMessage(language.getPrefixedMsg("withdraw-funds-sender", placeholders));
        }, () -> sender.sendMessage(language.getPrefixedMsg("unknown-player")));
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return TabCompleter.create()
                .from(1)
                .supply(getEconomies())
                .supply(getOnlinePlayers())
                .supply(Collections.singletonList("5000"))
                .toSuggestions(args);
    }

    private List<String> getEconomies() {
        return economyResolver.getEconomies().stream()
                .map(Economy::getName)
                .toList();
    }

    private List<String> getOnlinePlayers() {
        return plugin.getServer().getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
    }

    @Override
    public @NotNull String getUsage() {
        return "withdraw <economy-name> <player> <amount>";
    }
}
