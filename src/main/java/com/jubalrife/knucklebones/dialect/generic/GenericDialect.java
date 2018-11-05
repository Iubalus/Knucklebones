package com.jubalrife.knucklebones.dialect.generic;

import com.jubalrife.knucklebones.DAO;
import com.jubalrife.knucklebones.DAOFactory;
import com.jubalrife.knucklebones.dialect.Dialect;

import java.sql.Connection;

public class GenericDialect implements Dialect {
    @Override
    public <Type> Type insert(Connection connection, DAO<Type> meta, Type object) {
        return new GenericInsert().insert(meta, object, connection);
    }

    @Override
    public <Type> Type find(Connection connection, DAO<Type> daoMeta, Object object) {
        return new GenericFindSingle().find(connection, object, daoMeta);
    }

    @Override
    public <Type> int update(Connection connection, DAO<Type> daoMeta, Object o) {
        return new GenericUpdate().update(DAOFactory.create(o.getClass()), o, connection);
    }

    @Override
    public <Type> int delete(Connection connection, DAO<Type> daoMeta, Object o) {
        return new GenericDelete().delete(connection, DAOFactory.create(o.getClass()), o);
    }
}
