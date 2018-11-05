package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.exception.KnuckleBonesException;
import com.jubalrife.knucklebones.exception.KnuckleBonesException.OperationRequiresIdOnAtLeastOneField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenericUpdate {
    public <Type> int update(DAO<Type> daoMeta, Object dao, Connection connection) {
        if (daoMeta.getNumberOfIdColumns() == 0) throw new OperationRequiresIdOnAtLeastOneField(daoMeta.getType());

        SQLWithParameters sql = new SQLWithParameters();
        sql.append("UPDATE ");
        sql.append(daoMeta.getTableName());
        sql.append(" SET ");
        String sep = "";
        for (DAOColumnField daoColumnField : daoMeta.getColumns()) {
            if (daoColumnField.isId()) continue;

            sql.append(daoColumnField.getName());
            sql.append(" = ?");
            sql.add(daoColumnField.getField(), dao);
            sql.append(sep);
            sep = ", ";
        }

        sql.append(" WHERE ");

        sep = "";
        for (DAOColumnField daoColumnField : daoMeta.getColumns()) {
            if (!daoColumnField.isId()) continue;
            sql.append(daoColumnField.getName());
            sql.append(" = ?");
            sql.add(daoColumnField.getField(), dao);
            sql.append(sep);

            sep = " AND ";
        }

        try (PreparedStatement statement = new PreparedStatementExecutor().execute(connection, sql.getSql(), sql.getParameters())) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotUpdateData(e);
        }
    }
}
