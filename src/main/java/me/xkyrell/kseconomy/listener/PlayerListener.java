package me.xkyrell.kseconomy.listener;

import lombok.RequiredArgsConstructor;
import me.xkyrell.kseconomy.economy.EconomyResolver;
import me.xkyrell.kseconomy.player.repository.PlayerRepository;
import me.xkyrell.kseconomy.player.service.PlayerService;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerListener implements Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerListener.class);

    private final Plugin plugin;
    private final PlayerService playerService;
    private final PlayerRepository repository;
    private final EconomyResolver economyResolver;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        repository.loadPlayer(event.getPlayer())
                .thenAccept(player -> {
                    if (player.getEconomies() == null) {
                        player.setEconomies(economyResolver
                                .getEconomies().stream().toList()
                        );
                    }
                    LOGGER.info("Player {} loaded.", player.getName());
                    playerService.register(player);
                });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        var cachedPlayer = playerService.getResolver().resolve(player);
        cachedPlayer.ifPresent(economyPlayer -> {
            LOGGER.info("Player {} unloaded.", economyPlayer.getName());
            repository.savePlayer(economyPlayer);
        });
        playerService.unregister(player);
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (!plugin.equals(event.getPlugin())) {
            return;
        }

        Server server = event.getPlugin().getServer();
        playerService.registerAll(server.getOnlinePlayers());
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (!plugin.equals(event.getPlugin())) {
            return;
        }

        Server server = event.getPlugin().getServer();
        playerService.unregisterAll(server.getOnlinePlayers());
    }
}
