package com.jubalrife.knucklebones.v1.dialect;

import com.jubalrife.knucklebones.v1.DAO;
import com.jubalrife.knucklebones.v1.SupportedTypesRegistered;

import java.sql.Connection;

public interface Dialect {
    <Type> Type insert(Connection connection, DAO<Type> meta, Type object, SupportedTypesRegistered supportedTypes);

    <Type> Type find(Connection connection, DAO<Type> daoMeta, Object object, SupportedTypesRegistered supportedTypes);

    <Type> int update(Connection connection, DAO<Type> daoMeta, Object object, SupportedTypesRegistered supportedTypes);

    <Type> int delete(Connection connection, DAO<Type> daoMeta, Object object, SupportedTypesRegistered supportedTypes);
}
