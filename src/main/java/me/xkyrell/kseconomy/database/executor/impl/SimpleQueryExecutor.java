package me.xkyrell.kseconomy.database.executor.impl;

import lombok.RequiredArgsConstructor;
import me.xkyrell.kseconomy.database.ConnectionPool;
import me.xkyrell.kseconomy.database.executor.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public final class SimpleQueryExecutor implements QueryExecutor {

    private final ConnectionPool connectionPool;
    private final ExecutorService executorService;

    @Override
    public <T> CompletableFuture<T> query(
            String sql, QueryHandler<ResultSet, T> handler, Object... params
    ) {
        return performAsync(sql, params, statement -> {
            try (ResultSet resultSet = statement.executeQuery()) {
                return handler.handle(resultSet);
            }
        });
    }

    @Override
    public CompletableFuture<Integer> update(String sql, Object... params) {
        return performAsync(sql, params, PreparedStatement::executeUpdate);
    }

    @Override
    public CompletableFuture<Void> execute(String sql, Object... params) {
        return performAsync(sql, params, statement -> {
            statement.execute();
            return null;
        });
    }

    private <T> CompletableFuture<T> performAsync(
            String sql, Object[] params,
            QueryHandler<PreparedStatement, T> handler
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = connectionPool.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
                return handler.handle(statement);
            }
            catch (SQLException ex) {
                throw new RuntimeException("Error executing query:", ex);
            }
        }, executorService);
    }
}
