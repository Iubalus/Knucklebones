package com.jubalrife.knucklebones.dialect;

import com.jubalrife.knucklebones.DAO;
import com.jubalrife.knucklebones.SupportedTypesRegistered;

import java.sql.Connection;

public interface Dialect {
    <Type> Type insert(Connection connection, DAO<Type> meta, Type object, SupportedTypesRegistered supportedTypes);

    <Type> Type find(Connection connection, DAO<Type> daoMeta, Object object, SupportedTypesRegistered supportedTypes);

    <Type> int update(Connection connection, DAO<Type> daoMeta, Object object, SupportedTypesRegistered supportedTypes);

    <Type> int delete(Connection connection, DAO<Type> daoMeta, Object object, SupportedTypesRegistered supportedTypes);
}
