package me.xkyrell.kseconomy.config;

import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import java.io.File;
import java.io.IOException;

public class Config {

    @Getter
    private final FileConfiguration source;
    private final File file;

    Config(Plugin plugin, String name) {
        if (!name.endsWith(".yml")) {
            name += ".yml";
        }

        file = new File(plugin.getDataFolder(), name);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            plugin.saveResource(name, false);
        }
        source = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            source.save(file);
        }
        catch (IOException ex) {
            throw new RuntimeException("Exception due to saving file: " + file.getName(), ex);
        }
    }

    public void reload() {
        try {
            source.load(file);
        }
        catch (IOException | InvalidConfigurationException ex) {
            throw new RuntimeException("Exception due to reloading file: " + file.getName(), ex);
        }
    }
}
