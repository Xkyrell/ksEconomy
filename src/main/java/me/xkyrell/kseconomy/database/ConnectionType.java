package me.xkyrell.kseconomy.database;

import java.io.InputStream;

public enum ConnectionType {

    SQLITE, MYSQL;

    public InputStream getSchemeStream(Class<?> clazz) {
        InputStream stream = clazz.getClassLoader().getResourceAsStream(getSchemePath());
        if (stream == null) {
            throw new RuntimeException("Schema file not found in JAR: " + getSchemePath());
        }
        return stream;
    }

    private String getSchemePath() {
        return "schema/" + name().toLowerCase() + ".sql";
    }
}
