package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.DAO;
import com.jubalrife.knucklebones.v1.DAOColumnField;
import com.jubalrife.knucklebones.v1.PreparedStatementExecutor;
import com.jubalrife.knucklebones.v1.SupportedTypesRegistered;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenericUpdate {
    public <Type> int update(DAO<Type> daoMeta, Object dao, Connection connection, SupportedTypesRegistered supportedTypes) {
        if (daoMeta.getNumberOfIdColumns() == 0)
            throw new KnuckleBonesException.OperationRequiresIdOnAtLeastOneField(daoMeta.getType());

        String query = createQuery(daoMeta);

        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.prepareUpdate(
                connection,
                query
        )) {
            executor.setParameters(extractParameters(daoMeta, dao), supportedTypes, statement);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotUpdateData(e);
        }
    }

    public <Type> int update(DAO<Type> daoMeta, List<Type> dao, Connection connection, SupportedTypesRegistered supportedTypes) {
        if (daoMeta.getNumberOfIdColumns() == 0)
            throw new KnuckleBonesException.OperationRequiresIdOnAtLeastOneField(daoMeta.getType());

        String query = createQuery(daoMeta);

        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.prepareUpdate(
                connection,
                query
        )) {
            int updated = 0;
            for (Type type : dao) {
                executor.setParameters(extractParameters(daoMeta, type), supportedTypes, statement);
                updated += statement.executeUpdate();
            }
            return updated;
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotUpdateData(e);
        }
    }

    private <Type> List<Object> extractParameters(DAO<Type> daoMeta, Object dao) {
        ArrayList<Object> results = new ArrayList<>();
        for (DAOColumnField field : daoMeta.getColumns()) {
            try {
                if (field.isId()) continue;
                results.add(field.getField().get(dao));
            } catch (IllegalAccessException e) {
                throw new KnuckleBonesException.PropertyInaccessible(field.getField(), field.getField().getDeclaringClass(), e);
            }
        }

        for (DAOColumnField field : daoMeta.getColumns()) {
            try {
                if (!field.isId()) continue;
                results.add(field.getField().get(dao));
            } catch (IllegalAccessException e) {
                throw new KnuckleBonesException.PropertyInaccessible(field.getField(), field.getField().getDeclaringClass(), e);
            }
        }
        return results;
    }

    private <Type> String createQuery(DAO<Type> daoMeta) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(daoMeta.getTableName());
        sql.append(" SET ");
        String sep = "";
        for (DAOColumnField daoColumnField : daoMeta.getColumns()) {
            if (daoColumnField.isId()) continue;

            sql.append(sep);
            sql.append(daoColumnField.getName());
            sql.append(" = ?");
            sep = ", ";
        }

        sql.append(" WHERE ");

        sep = "";
        for (DAOColumnField daoColumnField : daoMeta.getColumns()) {
            if (!daoColumnField.isId()) continue;
            sql.append(daoColumnField.getName());
            sql.append(" = ?");
            sql.append(sep);

            sep = " AND ";
        }
        return sql.toString();
    }
}
