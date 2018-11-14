package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.*;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenericInsert {
    public <Type> Type insert(DAO<Type> meta, Type o, Connection connection, SupportedTypesRegistered supportedTypes) {
        List<DAOColumnField> columns1 = meta.getColumns();
        SQLWithParameters sql = new SQLWithParameters();
        sql.append("INSERT INTO ");
        sql.append(meta.getTableName());
        sql.append("(");
        String sep = "";

        for (DAOColumnField DAOColumnField1 : columns1) {
            if (!DAOColumnField1.isGenerated()) {
                sql.append(sep);
                sql.append(DAOColumnField1.getName());
                sep = ", ";
            }
        }
        sql.append(") VALUES (");
        sep = "";
        for (DAOColumnField DAOColumnField1 : columns1) {
            if (!DAOColumnField1.isGenerated()) {
                sql.append(sep);
                sql.append("?");
                sql.add(DAOColumnField1.getField(), o);
                sep = ", ";
            }
        }
        sql.append(")");

        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.execute(
                connection,
                sql.getSql(),
                sql.getParameters(),
                supportedTypes
        )) {
            statement.executeUpdate();

            DAOColumnField generatedKey = meta.getGeneratedId();
            if (generatedKey == null) {
                return o;
            }

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                Object o1 = supportedTypes
                        .getExtractor(keys.getMetaData().getColumnType(1), generatedKey.getField().getType()).extract(1, keys);
                try {
                    generatedKey.getField().set(o, o1);
                } catch (IllegalAccessException e) {
                    throw new KnuckleBonesException.PropertyInaccessible(generatedKey.getField(), meta.getType(), e);
                }
            }

            if (meta.hasAdditionalGeneratedColumns()) {
                return new GenericFindSingle().find(connection, o, DAOFactory.create(meta.getType()),
                        supportedTypes
                );
            }

        } catch (SQLException e) {
            throw new KnuckleBonesException("Unable to insert", e);
        }

        return o;
    }
}
