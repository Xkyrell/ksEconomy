package me.xkyrell.kseconomy.player.service;

import lombok.NonNull;
import me.xkyrell.kseconomy.player.EconomyPlayer;
import me.xkyrell.kseconomy.player.impl.*;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.util.UUID;

public interface PlayerService {

    void register(@NonNull String playerName, @NonNull Class<? extends EconomyPlayer<?>> clazz);

    void unregister(@NonNull String playerName);

    void unregister(@NonNull UUID uuid);

    PlayerResolver getResolver();

    default void register(@NonNull Player player) {
        if (player.isOnline()) {
            register(player.getName(), EconomyOnlinePlayer.class);
            return;
        }
        register(player.getName(), EconomyOfflinePlayer.class);
    }

    default void registerAll(@NonNull Collection<? extends Player> players) {
        players.forEach(this::register);
    }

    default void unregister(@NonNull Player player) {
        unregister(player.getUniqueId());
    }

    default void unregisterAll(@NonNull Collection<? extends Player> players) {
        players.forEach(this::unregister);
    }
}
