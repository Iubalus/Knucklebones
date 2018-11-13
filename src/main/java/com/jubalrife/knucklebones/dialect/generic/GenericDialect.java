package com.jubalrife.knucklebones.dialect.generic;

import com.jubalrife.knucklebones.DAO;
import com.jubalrife.knucklebones.DAOFactory;
import com.jubalrife.knucklebones.SupportedTypesRegistered;
import com.jubalrife.knucklebones.dialect.Dialect;

import java.sql.Connection;

public class GenericDialect implements Dialect {
    @Override
    public <Type> Type insert(Connection connection, DAO<Type> meta, Type object, SupportedTypesRegistered supportedTypes) {
        return new GenericInsert().insert(meta, object, connection, supportedTypes);
    }

    @Override
    public <Type> Type find(Connection connection, DAO<Type> daoMeta, Object object, SupportedTypesRegistered supportedTypes) {
        return new GenericFindSingle().find(connection, object, daoMeta, supportedTypes);
    }

    @Override
    public <Type> int update(Connection connection, DAO<Type> daoMeta, Object o, SupportedTypesRegistered supportedTypes) {
        return new GenericUpdate().update(DAOFactory.create(o.getClass()), o, connection, supportedTypes);
    }

    @Override
    public <Type> int delete(Connection connection, DAO<Type> daoMeta, Object o, SupportedTypesRegistered supportedTypes) {
        return new GenericDelete().delete(connection, DAOFactory.create(o.getClass()), o, supportedTypes);
    }
}
