package me.xkyrell.kseconomy.database;

import javax.sql.DataSource;
import java.sql.Connection;

public interface ConnectionPool {

    Connection getConnection();

    DataSource getDataSource();

    void close();

}
