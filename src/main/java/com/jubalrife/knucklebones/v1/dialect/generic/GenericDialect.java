package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.DAO;
import com.jubalrife.knucklebones.v1.SupportedTypesRegistered;
import com.jubalrife.knucklebones.v1.dialect.Dialect;

import java.sql.Connection;
import java.util.List;

public class GenericDialect implements Dialect {
    @Override
    public <Type> Type insert(Connection connection, DAO<Type> meta, Type object, SupportedTypesRegistered supportedTypes) {
        return new GenericInsert().insert(meta, object, connection, supportedTypes);
    }

    @Override
    public <Type> void insert(Connection connection, DAO<Type> meta, List<Type> object, SupportedTypesRegistered supportedTypes) {
        new GenericInsert().insert(meta, object, connection, supportedTypes);
    }

    @Override
    public <Type> Type find(Connection connection, DAO<Type> daoMeta, Type object, SupportedTypesRegistered supportedTypes) {
        return new GenericFindSingle().find(connection, object, daoMeta, supportedTypes);
    }

    @Override
    public <Type> int update(Connection connection, DAO<Type> daoMeta, Object o, SupportedTypesRegistered supportedTypes) {
        return new GenericUpdate().update(daoMeta, o, connection, supportedTypes);
    }

    @Override
    public <Type> int update(Connection connection, DAO<Type> daoMeta, List<Type> object, SupportedTypesRegistered supportedTypes) {
        return new GenericUpdate().update(daoMeta, object, connection, supportedTypes);
    }

    @Override
    public <Type> int delete(Connection connection, DAO<Type> daoMeta, Object o, SupportedTypesRegistered supportedTypes) {
        return new GenericDelete().delete(connection, daoMeta, o, supportedTypes);
    }
}
