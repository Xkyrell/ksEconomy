package me.xkyrell.kseconomy.economy.hook;

import me.xkyrell.kseconomy.economy.EconomyResolver;
import me.xkyrell.kseconomy.economy.hook.impl.PlaceholderApiHook;
import me.xkyrell.kseconomy.economy.hook.impl.VaultHook;
import me.xkyrell.kseconomy.player.service.PlayerService;
import me.xkyrell.kseconomy.util.UncheckedConsumer;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class HookRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(HookRegistry.class);

    private final Plugin plugin;
    private final List<Hook> hooks;

    public HookRegistry(Plugin plugin, PlayerService playerService, EconomyResolver economyResolver) {
        this.plugin = plugin;
        this.hooks = List.of(
                new VaultHook(playerService, economyResolver),
                new PlaceholderApiHook(playerService)
        );
    }

    public void registerAll() {
        processHook(hook -> hook.register(plugin), "registering");
    }

    public void unregisterAll() {
        processHook(hook -> hook.unregister(plugin), "unregistering");
    }

    private void processHook(UncheckedConsumer<Hook> action, String type) {
        hooks.forEach(hook -> {
            String name = hook.getClass().getSimpleName();
            try {
                action.accept(hook);
                LOGGER.info("Successfully {} hook: {}", type, name);
            }
            catch (Throwable t) {
                LOGGER.error("Error while {} hook: {}", type, name, t);
            }
        });
    }
}
