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
    @SuppressWarnings("unchecked")
    public <Type> Type insert(PersistenceContext context, Type o) {
        DAO<Type> meta = (DAO<Type>) context.getCache().create(o.getClass());
        String query = createQuery(meta);
        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.execute(
                context.getConnection(),
                query,
                extractParameters(meta, o),
                context.getSupportedTypes()
        )) {
            statement.executeUpdate();

            DAOColumnField generatedKey = meta.getGeneratedId();
            if (generatedKey == null) {
                return o;
            }

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                Object generatedValue = context.getSupportedTypes()
                        .getExtractor(keys.getMetaData().getColumnType(1), generatedKey.getField().getType())
                        .extract(1, keys);

                try {
                    generatedKey.getField().set(o, generatedValue);
                } catch (IllegalAccessException e) {
                    throw new KnuckleBonesException.PropertyInaccessible(generatedKey.getField(), meta.getType(), e);
                }
            }

            if (meta.hasAdditionalGeneratedColumns()) {
                return new GenericFindSingle().find(context, o);
            } else {
                return o;
            }
        } catch (SQLException e) {
            throw new KnuckleBonesException("Unable to insert", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <Type> void insert(PersistenceContext context, List<Type> insertList) {
        DAO<Type> meta = (DAO<Type>) context.getCache().create(insertList.get(0).getClass());
        String query = createQuery(meta);
        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.prepareInsert(
                context.getConnection(),
                query
        )) {
            for (Type record : insertList) {
                executor.setParameters(extractParameters(meta, record), context.getSupportedTypes(), statement);
                statement.executeUpdate();
                ResultSet keys = statement.getGeneratedKeys();
                DAOColumnField generatedKey = meta.getGeneratedId();
                if (generatedKey == null) continue;

                if (keys.next()) {
                    Object generatedValue = context.getSupportedTypes()
                            .getExtractor(keys.getMetaData().getColumnType(1), generatedKey.getField().getType())
                            .extract(1, keys);


                    try {
                        generatedKey.getField().set(record, generatedValue);
                    } catch (IllegalAccessException e) {
                        throw new KnuckleBonesException.PropertyInaccessible(generatedKey.getField(), meta.getType(), e);
                    }

                    if (meta.hasAdditionalGeneratedColumns()) {
                        new GenericFindSingle().find(context, record);
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

    <Type> String createQuery(DAO<Type> meta) {
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
