package me.xkyrell.kseconomy.player.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import me.xkyrell.kseconomy.database.ConnectionPool;
import me.xkyrell.kseconomy.database.executor.QueryExecutor;
import me.xkyrell.kseconomy.economy.*;
import me.xkyrell.kseconomy.player.EconomyPlayer;
import me.xkyrell.kseconomy.player.impl.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractPlayerRepository implements PlayerRepository {

    private static final Gson GSON = new Gson();

    private final ConnectionPool connectionPool;
    private final EconomyResolver economyResolver;

    protected final List<Economy> deserializeEconomies(String economiesResult) {
        Map<String, Double> economies = GSON.fromJson(economiesResult, new TypeToken<Map<String, Double>>(){}.getType());
        return economies.entrySet().stream()
                .map(entry -> economyResolver.resolve(entry.getKey())
                        .map(economy -> {
                            economy.setBalance(entry.getValue());
                            return economy;
                        })
                        .orElseThrow(() -> new IllegalArgumentException("Economy not found: " + entry.getKey())))
                .collect(Collectors.toList());
    }

    protected final String serializeEconomies(List<Economy> economies) {
        return GSON.toJson(economies.stream()
                .collect(Collectors.toMap(
                        Economy::getName,
                        Economy::getBalance
                ))
        );
    }

    protected final EconomyPlayer<?> createPlayer(UUID uuid, boolean online) {
        return online
                ? new EconomyOnlinePlayer(uuid)
                : new EconomyOfflinePlayer(uuid);
    }

    protected final QueryExecutor getExecutor() {
        return connectionPool.getQueryExecutor();
    }
}
