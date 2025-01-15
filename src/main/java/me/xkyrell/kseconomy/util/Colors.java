package me.xkyrell.kseconomy.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

@UtilityClass
public class Colors {

    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

    public Component fromLegacy(String message) {
        return AMPERSAND_SERIALIZER.deserialize(message);
    }

    public List<Component> fromLegacy(List<String> content) {
        return content.stream()
                .map(Colors::fromLegacy)
                .toList();
    }
}
