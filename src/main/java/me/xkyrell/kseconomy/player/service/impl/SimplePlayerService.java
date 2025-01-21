package me.xkyrell.kseconomy.player.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.xkyrell.kseconomy.player.*;
import me.xkyrell.kseconomy.player.impl.*;
import me.xkyrell.kseconomy.player.service.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import java.util.*;

@RequiredArgsConstructor
public class SimplePlayerService implements PlayerService {

    private final Plugin plugin;
    private final Map<UUID, EconomyPlayer<? extends OfflinePlayer>> players;

    @Override
    public void register(@NonNull String playerName, @NonNull Class<? extends EconomyPlayer<?>> clazz) {
        UUID uuid = getUUIDByName(playerName);
        EconomyPlayer<? extends OfflinePlayer> player = switch (clazz.getSimpleName()) {
            case "EconomyOnlinePlayer" -> new EconomyOnlinePlayer(uuid);
            case "EconomyOfflinePlayer" -> new EconomyOfflinePlayer(uuid);
            default -> throw new IllegalArgumentException("Unsupported Player class: " + clazz.getName());
        };
        players.put(uuid, player);
    }

    @Override
    public void register(@NonNull EconomyPlayer<?> player) {
        players.put(player.getUuid(), player);
    }

    @Override
    public void unregister(@NonNull String playerName) {
        unregister(getUUIDByName(playerName));
    }

    @Override
    public void unregister(@NonNull UUID uuid) {
        players.remove(uuid);
    }

    @Override
    public PlayerResolver getResolver() {
        return new SimpleResolver(players);
    }

    private UUID getUUIDByName(@NonNull String playerName) {
        return plugin.getServer().getPlayerUniqueId(playerName);
    }

    @RequiredArgsConstructor
    private final class SimpleResolver implements PlayerResolver {

        private final Map<UUID, EconomyPlayer<? extends OfflinePlayer>> players;

        @Override
        public Optional<GeneralEconomyPlayer> resolve(@NonNull String name) {
            return resolve(getUUIDByName(name));
        }

        @Override
        public Optional<GeneralEconomyPlayer> resolve(@NonNull UUID uuid) {
            return Optional.ofNullable((GeneralEconomyPlayer) players.get(uuid));
        }

        @Override
        public Collection<EconomyPlayer<? extends OfflinePlayer>> getPlayers() {
            return players.values();
        }
    }
}
