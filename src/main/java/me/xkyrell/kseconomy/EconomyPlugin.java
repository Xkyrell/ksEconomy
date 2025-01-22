package me.xkyrell.kseconomy;

import me.xkyrell.kseconomy.command.registry.CommandRegistry;
import me.xkyrell.kseconomy.dagger.DaggerEconomyComponent;
import me.xkyrell.kseconomy.dagger.EconomyComponent;
import me.xkyrell.kseconomy.dagger.modules.DefaultModule;
import me.xkyrell.kseconomy.database.ConnectionPool;
import me.xkyrell.kseconomy.economy.hook.HookRegistry;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Optional;

public class EconomyPlugin extends JavaPlugin {

    private static final String[] LOGO = {
            " _  __ __  ___ ___ __  __  _  __  __ ____   __",
            "| |/ /' _/| __/ _//__\\|  \\| |/__\\|  V  \\ `v' /",
            "|   <`._`.| _| \\_| \\/ | | ' | \\/ | \\_/ |`. .' ",
            "|_|\\_\\___/|___\\__/\\__/|_|\\__|\\__/|_| |_| !_!  ",
            "",
            "                Plugin by Xkyrell",
            ""
    };

    private ConnectionPool connectionPool;
    private CommandRegistry commandRegistry;
    private HookRegistry hookRegistry;

    @Override
    public void onEnable() {
        EconomyComponent component = DaggerEconomyComponent.builder()
                .defaultModule(new DefaultModule(this))
                .build();

        connectionPool = component.getConnectionPool();
        commandRegistry = component.getCommandRegistry();
        hookRegistry = component.getHookRegistry();

        component.getCommandRegistry().registerAll();
        component.getHookRegistry().registerAll();

        getServer().getPluginManager().registerEvents(
                component.getPlayerListener(), this
        );

        for (String line : LOGO) {
            getLogger().info(line);
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        Optional.ofNullable(connectionPool).ifPresent(ConnectionPool::close);
        Optional.ofNullable(commandRegistry).ifPresent(CommandRegistry::unregisterAll);
        Optional.ofNullable(hookRegistry).ifPresent(HookRegistry::unregisterAll);
    }
}