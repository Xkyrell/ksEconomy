package me.xkyrell.kseconomy.config;

import me.xkyrell.kseconomy.util.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.plugin.Plugin;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class LanguageConfig extends Config {

    private Map<String, Component> localization = new HashMap<>();

    @Inject
    public LanguageConfig(Plugin plugin) {
        super(plugin, "language");

        updateOrLoad();
    }

    @Override
    public void reload() {
        super.reload();
        updateOrLoad();
    }

    private void updateOrLoad() {
        localization = getSource().getKeys(false).stream()
                .filter(getSource()::isString)
                .collect(Collectors.toMap(
                        Function.identity(),
                        msg -> Colors.fromLegacy(getSource().getString(msg)))
                );
    }

    public Component getMsg(String key) {
        return localization.getOrDefault(key,
                Component.text("Key not found: " + key)
        );
    }

    public Component getPrefixedMsg(String key) {
        return getMsg(key).replaceText(TextReplacementConfig.builder()
                .matchLiteral("{prefix}")
                .replacement(getMsg("prefix"))
                .build()
        );
    }

    public Component getMsg(String key, Map<String, String> placeholders) {
        return getMsg(key, this::getMsg, placeholders);
    }

    public Component getPrefixedMsg(String key, Map<String, String> placeholders) {
        return getMsg(key, this::getPrefixedMsg, placeholders);
    }

    private Component getMsg(String key, Function<String, Component> factory, Map<String, String> placeholders) {
        Component message = factory.apply(key);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replaceText(builder -> builder
                    .matchLiteral(entry.getKey())
                    .replacement(entry.getValue())
            );
        }
        return message;
    }
}
