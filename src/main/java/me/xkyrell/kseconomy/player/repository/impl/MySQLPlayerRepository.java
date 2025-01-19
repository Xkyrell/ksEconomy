package me.xkyrell.kseconomy.player.repository.impl;

import lombok.NonNull;
import me.xkyrell.kseconomy.database.ConnectionPool;
import me.xkyrell.kseconomy.economy.*;
import me.xkyrell.kseconomy.player.EconomyPlayer;
import me.xkyrell.kseconomy.player.repository.AbstractPlayerRepository;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class MySQLPlayerRepository extends AbstractPlayerRepository {

    public MySQLPlayerRepository(ConnectionPool connectionPool, EconomyResolver economyResolver) {
        super(connectionPool, economyResolver);
    }

    @Override
    public CompletableFuture<EconomyPlayer<?>> loadPlayer(@NonNull UUID uuid, boolean online) {
        String sql = "SELECT economies FROM players WHERE uuid = ?";
        return getExecutor().query(sql, resultSet -> {
            if (resultSet.next()) {
                String economiesResult = resultSet.getString("economies");
                List<Economy> economies = deserializeEconomies(economiesResult);

                EconomyPlayer<?> player = createPlayer(uuid, online);
                player.setEconomies(economies);
                return player;
            }
            return createPlayer(uuid, online);
        }, uuid);
    }

    @Override
    public CompletableFuture<Void> savePlayer(@NonNull EconomyPlayer<?> player) {
        String uuid = player.getUuid().toString();
        String name = player.getName();
        String economies = serializeEconomies(player.getEconomies());

        String sql = "INSERT INTO players (uuid, playername, economies) " +
                "VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "playername = VALUES(playername), " +
                "economies = VALUES(economies)";

        return getExecutor().execute(sql, uuid, name, economies);
    }
}
