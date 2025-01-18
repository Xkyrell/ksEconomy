package me.xkyrell.kseconomy.config;

import lombok.Getter;
import me.xkyrell.kseconomy.database.ConnectionPool;
import me.xkyrell.kseconomy.database.impl.MySQLConnectionPool;
import me.xkyrell.kseconomy.database.impl.SQLiteConnectionPool;
import me.xkyrell.kseconomy.economy.*;
import me.xkyrell.kseconomy.economy.impl.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralConfig extends Config {

    private final File dataFolder;
    private final Map<String, Economy> economies = new HashMap<>();

    @Getter
    private boolean vaultHook;
    @Getter
    private ConnectionPool connectionPool;

    public GeneralConfig(Plugin plugin) {
        super(plugin, "config");

        dataFolder = plugin.getDataFolder();

        updateOrLoad();
    }

    @Override
    public void reload() {
        super.reload();
        updateOrLoad();
    }

    private void updateOrLoad() {
        vaultHook = getSource().getBoolean("general.vault-enabled");
        connectionPool = loadDatabase(
                getSource().getConfigurationSection("database")
        );

        loadEconomies(getSource().getConfigurationSection("economies"));
    }

    private ConnectionPool loadDatabase(ConfigurationSection dbSection) {
        String type = dbSection.getString("type", "sqlite");
        String poolName = dbSection.getString("pool-name");
        int maxPoolSize = dbSection.getInt("max-pool-size");
        return (type.equalsIgnoreCase("mysql"))
                ? createMySQLConnection(dbSection, poolName, maxPoolSize)
                : createSQLiteConnection(poolName, maxPoolSize);
    }

    private ConnectionPool createSQLiteConnection(String poolName, int maxPoolSize) {
        return SQLiteConnectionPool.builder()
                .poolName(poolName)
                .maxPoolSize(maxPoolSize)
                .uri(dataFolder.getAbsolutePath().concat("\\storage.db"))
                .build();
    }

    private ConnectionPool createMySQLConnection(ConfigurationSection dbSection, String poolName, int maxPoolSize) {
        String hostname = dbSection.getString("hostname");
        String port = dbSection.getString("port");
        String database = dbSection.getString("database");
        String username = dbSection.getString("username");
        String password = dbSection.getString("password");
        return MySQLConnectionPool.builder()
                .poolName(poolName)
                .maxPoolSize(maxPoolSize)
                .uri(hostname, port, database)
                .username(username)
                .password(password)
                .build();
    }

    private void loadEconomies(ConfigurationSection economiesSection) {
        if (economiesSection == null) {
            return;
        }

        economies.clear();
        for (String key : economiesSection.getKeys(false)) {
            ConfigurationSection economySection = economiesSection.getConfigurationSection(key);
            if (economySection == null) {
                continue;
            }

            String symbol = economySection.getString("symbol");
            EconomyPluralizer pluralizer = loadPluralizer(economySection);
            DecimalFormat format = loadFormat(economySection);
            double balance = economySection.getDouble("start-balance");
            double maxBalance = economySection.getDouble("max-balance");

            Economy economy = new SimpleEconomy(
                    key, symbol, pluralizer, format, balance, maxBalance
            );
            economies.put(key, economy);
        }
    }

    private EconomyPluralizer loadPluralizer(ConfigurationSection section) {
        String singular = section.getString("singular");
        List<String> plural = section.getStringList("plural");
        return new EconomyPluralizer(singular, plural.toArray(new String[0]),
                balance -> balance == 1 ? singular : plural.get(0)
        );
    }

    private DecimalFormat loadFormat(ConfigurationSection section) {
        String format = section.getString("format");
        return new DecimalFormat((format != null) ? format : "#.##");
    }

    public EconomyResolver getLoadedResolver() {
        return new SimpleEconomyResolver(economies);
    }
}
