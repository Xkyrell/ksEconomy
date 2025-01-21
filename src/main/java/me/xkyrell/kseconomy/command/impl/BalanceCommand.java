package me.xkyrell.kseconomy.command.impl;

import me.xkyrell.kseconomy.command.AbstractCommand;
import me.xkyrell.kseconomy.command.registry.CommandRegistry;
import me.xkyrell.kseconomy.config.LanguageConfig;
import me.xkyrell.kseconomy.economy.*;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static me.xkyrell.kseconomy.command.AbstractExecutable.AvailabilityType.HANDLER;

public class BalanceCommand extends AbstractCommand {

    private final EconomyResolver economyResolver;
    private final CommandRegistry registry;
    private final LanguageConfig language;
    private final Plugin plugin;

    public BalanceCommand(
            EconomyResolver economyResolver, CommandRegistry registry,
            LanguageConfig language, Plugin plugin
    ) {
        super("ksbalance", List.of("balance", "bal"));

        this.economyResolver = economyResolver;
        this.registry = registry;
        this.language = language;
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!checkAvailabilities(sender, HANDLER)) {
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(language.getPrefixedMsg("not-enough-args"));
            return true;
        }

        String playerName = args.length > 1 ? args[1] : sender.getName();
        registry.loadPlayerOrSelf(playerName, loadedPlayer -> {
            Economy foundedEconomy = loadedPlayer.getEconomyByName(args[0]);
            if (foundedEconomy == null) {
                String formattedEconomies = String.join(", ", getEconomies());
                Map<String, String> placeholders = Map.of("{economies}", formattedEconomies);
                sender.sendMessage(language.getPrefixedMsg("unknown-economy", placeholders));
                return;
            }

            Map<String, String> placeholders = Map.of(
                    "{balance_formatted_symbol}", foundedEconomy.getSymbol(),
                    "{balance_formatted}", foundedEconomy.format()
            );

            String key = args.length < 2 ? "balance" : "balance-other";
            sender.sendMessage(language.getPrefixedMsg(key, placeholders));
        }, () -> sender.sendMessage(language.getPrefixedMsg("unknown-player")));
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) throws IllegalArgumentException {
        if (!checkAvailabilities(sender)) {
            return Collections.emptyList();
        }

        return TabCompleter.create()
                .supply(getEconomies())
                .supply(getOnlinePlayers())
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
        return "balance <economy-name> [player]";
    }
}
