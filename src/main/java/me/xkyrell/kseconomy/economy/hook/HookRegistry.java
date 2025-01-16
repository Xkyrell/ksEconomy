package me.xkyrell.kseconomy.economy.hook;

import lombok.RequiredArgsConstructor;
import me.xkyrell.kseconomy.util.UncheckedConsumer;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RequiredArgsConstructor
public class HookRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(HookRegistry.class);

    private final Plugin plugin;
    private final List<Hook> hooks = List.of();

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
