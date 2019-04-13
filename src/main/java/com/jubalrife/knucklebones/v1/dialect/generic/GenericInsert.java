package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.*;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenericInsert {
    public <Type> Type insert(DAO<Type> meta, Type o, Connection connection, SupportedTypesRegistered supportedTypes) {
        String query = createQuery(meta);
        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.execute(
                connection,
                query,
                extractParameters(meta, o),
                supportedTypes
        )) {
            statement.executeUpdate();

            DAOColumnField generatedKey = meta.getGeneratedId();
            if (generatedKey == null) {
                return o;
            }

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                Object generatedValue = supportedTypes
                        .getExtractor(keys.getMetaData().getColumnType(1), generatedKey.getField().getType())
                        .extract(1, keys);

                try {
                    generatedKey.getField().set(o, generatedValue);
                } catch (IllegalAccessException e) {
                    throw new KnuckleBonesException.PropertyInaccessible(generatedKey.getField(), meta.getType(), e);
                }
            }

            if (meta.hasAdditionalGeneratedColumns()) {
                return new GenericFindSingle().find(connection, o, meta, supportedTypes);
            } else {
                return o;
            }
        } catch (SQLException e) {
            throw new KnuckleBonesException("Unable to insert", e);
        }
    }

    public <Type> void insert(DAO<Type> meta, List<Type> insertList, Connection connection, SupportedTypesRegistered supportedTypes) {
        String query = createQuery(meta);
        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.prepareInsert(
                connection,
                query
        )) {
            for (Type record : insertList) {
                executor.setParameters(extractParameters(meta, record), supportedTypes, statement);
                statement.executeUpdate();
                ResultSet keys = statement.getGeneratedKeys();
                DAOColumnField generatedKey = meta.getGeneratedId();
                if (generatedKey == null) continue;

                if (keys.next()) {
                    Object generatedValue = supportedTypes
                            .getExtractor(keys.getMetaData().getColumnType(1), generatedKey.getField().getType())
                            .extract(1, keys);


                    try {
                        generatedKey.getField().set(record, generatedValue);
                    } catch (IllegalAccessException e) {
                        throw new KnuckleBonesException.PropertyInaccessible(generatedKey.getField(), meta.getType(), e);
                    }

                    if (meta.hasAdditionalGeneratedColumns()) {
                        new GenericFindSingle().find(connection, record, meta, supportedTypes);
                    }
                }
            }
        } catch (SQLException e) {
            throw new KnuckleBonesException("Unable to insert", e);
        }
    }


    private <Type> List<Object> extractParameters(DAO<Type> meta, Type object) {
        ArrayList<Object> params = new ArrayList<>();
        for (DAOColumnField field : meta.getColumns()) {
            if (!field.isGenerated()) {
                try {
                    params.add(field.getField().get(object));
                } catch (IllegalAccessException e) {
                    throw new KnuckleBonesException.PropertyInaccessible(field.getField(), field.getField().getDeclaringClass(), e);
                }
            }
        }
        return params;

    }

    private <Type> String createQuery(DAO<Type> meta) {
        List<DAOColumnField> columns = meta.getColumns();
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(meta.getTableName());
        sql.append("(");
        String sep = "";

        for (DAOColumnField field : columns) {
            if (!field.isGenerated()) {
                sql.append(sep);
                sql.append(field.getName());
                sep = ", ";
            }
        }
        sql.append(") VALUES (");
        sep = "";
        for (DAOColumnField field : columns) {
            if (!field.isGenerated()) {
                sql.append(sep);
                sql.append("?");
                sep = ", ";
            }
        }
        sql.append(")");
        return sql.toString();
    }
}
