package me.xkyrell.kseconomy.player.service;

import lombok.NonNull;
import me.xkyrell.kseconomy.player.*;
import org.bukkit.OfflinePlayer;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public interface PlayerResolver {

    Optional<EconomyPlayer<?>> resolve(@NonNull String name);

    Optional<EconomyPlayer<?>> resolve(@NonNull UUID uuid);

    Collection<EconomyPlayer<?>> getPlayers();

    @SuppressWarnings("unchecked")
    default <P extends OfflinePlayer> Optional<EconomyPlayer<P>> resolve(@NonNull P player) {
        return resolve(Objects.requireNonNull(player.getName()))
                .map(instance -> (EconomyPlayer<P>) instance);
    }
}
