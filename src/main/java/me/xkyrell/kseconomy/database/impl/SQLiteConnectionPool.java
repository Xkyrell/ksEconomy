package me.xkyrell.kseconomy.database.impl;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.xkyrell.kseconomy.database.AbstractConnectionPool;
import me.xkyrell.kseconomy.database.ConnectionPool;
import me.xkyrell.kseconomy.database.ConnectionType;
import java.io.File;

public final class SQLiteConnectionPool extends AbstractConnectionPool {

    public SQLiteConnectionPool(HikariDataSource dataSource) {
        super(dataSource);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public ConnectionType getType() {
        return ConnectionType.SQLITE;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Builder extends AbstractBuilder<Builder> {

        public Builder uri(File uri) {
            return uri(uri.getAbsoluteFile());
        }

        @Override
        public ConnectionPool build() {
            Preconditions.checkState((getUri() != null), "Database file not initialized");

            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName("org.sqlite.JDBC");
            dataSource.setJdbcUrl("jdbc:sqlite:" + getUri());

            return new SQLiteConnectionPool(dataSource);
        }
    }
}
