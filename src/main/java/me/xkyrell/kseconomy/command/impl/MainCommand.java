package me.xkyrell.kseconomy.command.impl;

import lombok.Getter;
import me.xkyrell.kseconomy.command.AbstractCommand;
import me.xkyrell.kseconomy.command.AbstractSubCommand;
import me.xkyrell.kseconomy.command.registry.CommandRegistry;
import me.xkyrell.kseconomy.config.GeneralConfig;
import me.xkyrell.kseconomy.config.LanguageConfig;
import me.xkyrell.kseconomy.economy.EconomyResolver;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainCommand extends AbstractCommand {

    @Getter
    private final Set<AbstractSubCommand> subCommands = new HashSet<>();

    public MainCommand(
            EconomyResolver economyResolver,
            CommandRegistry registry, GeneralConfig general,
            LanguageConfig language, Plugin plugin
    ) {
        super("kseconomy", List.of("economy", "eco"));

        setUnknownSubCommand(sender -> {
            Component unknownSubCommand = language.getPrefixedMsg("subcommand-not-exist");
            sender.sendMessage(unknownSubCommand.replaceText(builder -> {
                builder.matchLiteral("{label}").replacement(getUsage());
            }));
        });

        addAvailability(
                sender -> !sender.hasPermission("kseconomy.admin"),
                language.getPrefixedMsg("no-permission")
        );

        setUnknownArgExecuting(sender -> {
            sender.sendMessage(language.getPrefixedMsg("not-enough-args"));
        });

        setupSubCommands(
                economyResolver, registry,
                general, language, plugin
        );
    }

    private void setupSubCommands(
            EconomyResolver economyResolver, CommandRegistry registry,
            GeneralConfig general, LanguageConfig language,
            Plugin plugin
    ) {
        subCommands.add(new GiveSubCommand(economyResolver, registry, language, plugin));
        subCommands.add(new SetSubCommand(economyResolver, registry, language, plugin));
        subCommands.add(new WithdrawSubCommand(economyResolver, registry, language, plugin));
        subCommands.add(new ReloadSubCommand(general, language));
    }
}
