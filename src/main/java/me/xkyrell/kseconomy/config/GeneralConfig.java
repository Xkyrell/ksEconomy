package me.xkyrell.kseconomy.config;

import lombok.Getter;
import me.xkyrell.kseconomy.database.ConnectionPool;
import me.xkyrell.kseconomy.economy.*;
import me.xkyrell.kseconomy.economy.impl.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class GeneralConfig extends Config {

    private boolean vaultHook;
    private ConnectionPool connectionPool;

    private final Map<String, Economy> economies = new HashMap<>();

    public GeneralConfig(Plugin plugin) {
        super(plugin, "config");

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

    // TODO: database initialization
    private ConnectionPool loadDatabase(ConfigurationSection dbSection) {
        return null;
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
