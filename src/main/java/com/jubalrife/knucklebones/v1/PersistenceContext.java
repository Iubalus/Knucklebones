package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.dialect.Dialect;

import java.sql.Connection;
import java.util.List;

public class PersistenceContext {
    private final Connection connection;
    private final Dialect dialect;
    private final SupportedTypesRegistered supportedTypes;
    private final DAOFactory cache;

    PersistenceContext(Connection connection, Dialect dialect, SupportedTypesRegistered supportedTypes, DAOFactory cache) {
        this.connection = connection;
        this.dialect = dialect;
        this.supportedTypes = supportedTypes;
        this.cache = cache;
    }

    public Connection getConnection() {
        return connection;
    }

    Object find(Object record) {
        return dialect.find(this, record);
    }

    Object insert(Object record) {
        return dialect.insert(this, record);
    }

    void insert(List<Object> record) {
        dialect.insert(this, record);
    }

    int update(Object record) {
        return dialect.update(this, record);
    }

    int update(List<Object> record) {
        return dialect.update(this, record);
    }

    int delete(Object record) {
        return dialect.delete(this, record);
    }

    public SupportedTypesRegistered getSupportedTypes() {
        return supportedTypes;
    }

    public DAOFactory getCache() {
        return cache;
    }
}
