package me.xkyrell.kseconomy.player.service;

import lombok.NonNull;
import me.xkyrell.kseconomy.player.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PlayerResolver {

    Optional<GeneralEconomyPlayer> resolve(@NonNull String name);

    Optional<GeneralEconomyPlayer> resolve(@NonNull UUID uuid);

    Collection<EconomyPlayer<? extends OfflinePlayer>> getPlayers();

    default Optional<GeneralEconomyPlayer> resolve(@NonNull Player player) {
        return resolve(player.getName());
    }
}
