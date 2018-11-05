package com.jubalrife.knucklebones.dialect;

import com.jubalrife.knucklebones.DAO;

import java.sql.Connection;

public interface Dialect {
    <Type> Type insert(Connection connection, DAO<Type> meta, Type object);

    <Type> Type find(Connection connection, DAO<Type> daoMeta, Object object);

    <Type> int update(Connection connection, DAO<Type> daoMeta, Object object);

    <Type> int delete(Connection connection, DAO<Type> daoMeta, Object object);
}
