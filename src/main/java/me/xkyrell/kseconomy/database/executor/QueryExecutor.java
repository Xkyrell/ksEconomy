package me.xkyrell.kseconomy.database.executor;

import java.util.concurrent.CompletableFuture;

public interface QueryExecutor {

    <T> CompletableFuture<T> query(
            String sql, QueryHandler<T> handler, Object... params
    );

    CompletableFuture<Integer> update(String sql, Object... params);

    CompletableFuture<Void> execute(String sql, Object... params);

}
