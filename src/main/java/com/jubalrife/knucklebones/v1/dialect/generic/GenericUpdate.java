package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.DAO;
import com.jubalrife.knucklebones.v1.DAOColumnField;
import com.jubalrife.knucklebones.v1.PersistenceContext;
import com.jubalrife.knucklebones.v1.PreparedStatementExecutor;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenericUpdate {
    @SuppressWarnings("unchecked")
    public <Type> int update(PersistenceContext context, Object dao) {
        DAO<Type> daoMeta = (DAO<Type>) context.getCache().create(dao.getClass());
        if (daoMeta.getNumberOfIdColumns() == 0)
            throw new KnuckleBonesException.OperationRequiresIdOnAtLeastOneField(daoMeta.getType());

        String query = createQuery(daoMeta);

        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.prepareUpdate(
                context.getConnection(),
                query
        )) {
            executor.setParameters(extractParameters(daoMeta, dao), context.getSupportedTypes(), statement);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotUpdateData(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <Type> int update(PersistenceContext context, List<Type> dao) {
        DAO<Type> daoMeta = (DAO<Type>) context.getCache().create(dao.getClass());
        if (daoMeta.getNumberOfIdColumns() == 0)
            throw new KnuckleBonesException.OperationRequiresIdOnAtLeastOneField(daoMeta.getType());

        String query = createQuery(daoMeta);

        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.prepareUpdate(
                context.getConnection(),
                query
        )) {
            int updated = 0;
            for (Type type : dao) {
                executor.setParameters(extractParameters(daoMeta, type), context.getSupportedTypes(), statement);
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

    <Type> String createQuery(DAO<Type> daoMeta) {
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
            sql.append(sep);
            sql.append(daoColumnField.getName());
            sql.append(" = ?");

            sep = " AND ";
        }
        return sql.toString();
    }
}
