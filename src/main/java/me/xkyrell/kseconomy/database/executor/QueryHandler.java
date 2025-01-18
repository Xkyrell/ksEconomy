package me.xkyrell.kseconomy.database.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface QueryHandler<T> {

    T handle(ResultSet resultSet) throws SQLException;

}
