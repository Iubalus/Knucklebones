package com.jubalrife.knucklebones.v1.dialect;

import com.jubalrife.knucklebones.v1.DAO;
import com.jubalrife.knucklebones.v1.SupportedTypesRegistered;

import java.sql.Connection;
import java.util.List;

public interface Dialect {
    <Type> Type insert(Connection connection, DAO<Type> meta, Type object, SupportedTypesRegistered supportedTypes);

    <Type> void insert(Connection connection, DAO<Type> meta, List<Type> object, SupportedTypesRegistered supportedTypes);

    <Type> Type find(Connection connection, DAO<Type> daoMeta, Type object, SupportedTypesRegistered supportedTypes);

    <Type> int update(Connection connection, DAO<Type> daoMeta, Object object, SupportedTypesRegistered supportedTypes);

    <Type> int update(Connection connection, DAO<Type> daoMeta, List<Type> object, SupportedTypesRegistered supportedTypes);

    <Type> int delete(Connection connection, DAO<Type> daoMeta, Object object, SupportedTypesRegistered supportedTypes);
}
