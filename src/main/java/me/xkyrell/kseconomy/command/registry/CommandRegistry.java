package me.xkyrell.kseconomy.command.registry;

import me.xkyrell.kseconomy.command.AbstractCommand;
import me.xkyrell.kseconomy.command.impl.BalanceCommand;
import me.xkyrell.kseconomy.command.impl.MainCommand;
import me.xkyrell.kseconomy.config.*;
import me.xkyrell.kseconomy.economy.EconomyResolver;
import me.xkyrell.kseconomy.player.EconomyPlayer;
import me.xkyrell.kseconomy.player.repository.PlayerRepository;
import me.xkyrell.kseconomy.player.service.PlayerService;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import javax.inject.Inject;
import java.util.Set;
import java.util.function.Consumer;

public class CommandRegistry {

    private final Set<AbstractCommand> commands;
    private final PlayerService playerService;
    private final PlayerRepository repository;
    private final Plugin plugin;

    @Inject
    public CommandRegistry(
            PlayerService playerService, EconomyResolver economyResolver,
            PlayerRepository repository, GeneralConfig general,
            LanguageConfig language, Plugin plugin
    ) {
        this.playerService = playerService;
        this.repository = repository;
        this.plugin = plugin;

        AbstractCommand balance = new BalanceCommand(
                economyResolver, this, language, plugin
        );

        AbstractCommand main = new MainCommand(
                economyResolver, this,
                general, language, plugin
        );

        commands = Set.of(balance, main);
    }

    public void registerAll() {
        commands.forEach(command -> {
            plugin.getServer().getCommandMap().register(
                    plugin.getName(), command
            );
        });
    }

    public void unregisterAll() {
        commands.forEach(command -> command.unregister(plugin));
    }

    public void loadPlayerOrSelf(
            String playerName, Consumer<EconomyPlayer<?>> onLoadedPlayer, Runnable onFail
    ) {
        var cachedPlayer = playerService.getResolver().resolve(playerName);
        if (cachedPlayer.isPresent()) {
            onLoadedPlayer.accept(cachedPlayer.get());
            return;
        }

        OfflinePlayer target = plugin.getServer().getOfflinePlayerIfCached(playerName);
        if (target == null) {
            onFail.run();
            return;
        }

        repository.loadPlayer(target).thenAccept(player -> {
            if (player.getEconomies() != null) {
                playerService.register(player);
                onLoadedPlayer.accept(player);
            }
            else {
                onFail.run();
            }
        });
    }
}
