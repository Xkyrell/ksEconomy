package me.xkyrell.kseconomy.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.xkyrell.kseconomy.database.executor.QueryExecutor;
import me.xkyrell.kseconomy.database.executor.impl.SimpleQueryExecutor;
import me.xkyrell.kseconomy.util.Buildable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.*;

@Getter
@RequiredArgsConstructor
public abstract class AbstractConnectionPool implements ConnectionPool {

    private static final ExecutorService FIXED_EXECUTOR_SERVICE = new ThreadPoolExecutor(
            0, 4, 60L,
            TimeUnit.SECONDS, new SynchronousQueue<>()
    );

    protected final HikariDataSource dataSource;

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        }
        catch (SQLException e) {
            throw new RuntimeException("An exception found due getting connection:", e);
        }
    }

    @Override
    public QueryExecutor getQueryExecutor() {
        return new SimpleQueryExecutor(this, FIXED_EXECUTOR_SERVICE);
    }

    @Override
    public final void close() {
        dataSource.close();
    }

    @Getter(AccessLevel.PROTECTED)
    protected static abstract class AbstractBuilder<B extends AbstractBuilder<B>> implements Buildable<ConnectionPool> {

        private int maxPoolSize = 4;
        private String poolName = "EconomyPool";
        private String uri;

        @SuppressWarnings("unchecked")
        protected final B self = (B) this;

        public B uri(String uri) {
            this.uri = uri;
            return self;
        }

        public B maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return self;
        }

        public B poolName(String poolName) {
            this.poolName = poolName;
            return self;
        }
    }
}
