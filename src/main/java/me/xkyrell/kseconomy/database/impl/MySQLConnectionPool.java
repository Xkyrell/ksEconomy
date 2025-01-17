package me.xkyrell.kseconomy.database.impl;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariDataSource;
import me.xkyrell.kseconomy.database.AbstractConnectionPool;
import me.xkyrell.kseconomy.database.ConnectionPool;

public final class MySQLConnectionPool extends AbstractConnectionPool {

    public MySQLConnectionPool(HikariDataSource dataSource) {
        super(dataSource);
    }

    public final class Builder extends AbstractBuilder<Builder> {

        private String username = "root";
        private String password;

        public Builder username(String username) {
            this.username = username;
            return self;
        }

        public Builder password(String password) {
            this.password = password;
            return self;
        }

        public Builder uri(String hostname, String port, String database) {
            String uri = String.format(
                    "%s:%s/%s?useUnicode=true&characterEncoding=utf-8&useSSL=false",
                    hostname, port, database
            );
            return uri(uri);
        }

        @Override
        public ConnectionPool build() {
            Preconditions.checkState((getUri() != null), "Database not initialized");

            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://" + getUri());
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            return new MySQLConnectionPool(dataSource);
        }
    }
}
