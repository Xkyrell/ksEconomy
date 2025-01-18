package me.xkyrell.kseconomy.database;

import me.xkyrell.kseconomy.database.executor.QueryExecutor;

import javax.sql.DataSource;
import java.sql.Connection;

public interface ConnectionPool {

    Connection getConnection();

    DataSource getDataSource();

    QueryExecutor getQueryExecutor();

    void close();

}
