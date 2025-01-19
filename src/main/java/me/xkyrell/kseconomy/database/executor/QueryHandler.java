package me.xkyrell.kseconomy.database.executor;

import java.sql.SQLException;
import java.sql.Wrapper;

public interface QueryHandler<T extends AutoCloseable & Wrapper, R> {

    R handle(T t) throws SQLException;

}
