package me.xkyrell.kseconomy.dagger.modules;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import lombok.RequiredArgsConstructor;
import me.xkyrell.kseconomy.config.GeneralConfig;
import me.xkyrell.kseconomy.config.LanguageConfig;
import me.xkyrell.kseconomy.database.ConnectionPool;
import me.xkyrell.kseconomy.database.ConnectionType;
import me.xkyrell.kseconomy.economy.EconomyResolver;
import me.xkyrell.kseconomy.player.repository.PlayerRepository;
import me.xkyrell.kseconomy.player.repository.impl.MySQLPlayerRepository;
import me.xkyrell.kseconomy.player.repository.impl.SQLitePlayerRepository;
import org.bukkit.plugin.Plugin;

@Module
@RequiredArgsConstructor
public class DefaultModule {

    private final Plugin plugin;

    @Provides
    @Reusable
    public GeneralConfig provideGeneral() {
        return new GeneralConfig(plugin);
    }

    @Provides
    @Reusable
    public LanguageConfig provideLanguage() {
        return new LanguageConfig(plugin);
    }

    @Provides
    public Plugin providePlugin() {
        return plugin;
    }

    @Provides
    public EconomyResolver provideEconomyResolver(GeneralConfig general) {
        return general.getLoadedResolver();
    }

    @Provides
    public ConnectionPool provideConnectionPool(GeneralConfig general) {
        return general.getConnectionPool();
    }

    @Provides
    @Reusable
    public PlayerRepository providePlayerRepository(GeneralConfig general, ConnectionPool connectionPool, EconomyResolver economyResolver) {
        ConnectionType type = general.getConnectionPool().getType();
        if (type.equals(ConnectionType.MYSQL)) {
            return new MySQLPlayerRepository(connectionPool, economyResolver);
        }
        else {
            return new SQLitePlayerRepository(connectionPool, economyResolver);
        }
    }
}
