package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.dialect.Dialect;

import java.sql.Connection;

public class PersistenceContext {
    private final Connection connection;
    private final Dialect dialect;
    private final SupportedTypesRegistered supportedTypes;
    private final DAOFactory cache;

    public PersistenceContext(Connection connection, Dialect dialect, SupportedTypesRegistered supportedTypes, DAOFactory cache) {
        this.connection = connection;
        this.dialect = dialect;
        this.supportedTypes = supportedTypes;
        this.cache = cache;
    }

    public Connection getConnection() {
        return connection;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public SupportedTypesRegistered getSupportedTypes() {
        return supportedTypes;
    }

    public DAOFactory getCache() {
        return cache;
    }
}
