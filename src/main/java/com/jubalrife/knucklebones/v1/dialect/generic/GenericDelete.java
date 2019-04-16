package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.DAO;
import com.jubalrife.knucklebones.v1.DAOColumnField;
import com.jubalrife.knucklebones.v1.PreparedStatementExecutor;
import com.jubalrife.knucklebones.v1.SupportedTypesRegistered;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenericDelete {
    public <DAOType> int delete(Connection connection, DAO<DAOType> daoMeta, Object dao, SupportedTypesRegistered supportedTypes) {
        SQLWithParameters sql = createQuery(daoMeta, dao);

        try (PreparedStatement statement = new PreparedStatementExecutor().execute(
                connection,
                sql.getSql(),
                sql.getParameters(),
                supportedTypes
        )) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotUpdateData(e);
        }
    }

    <DAOType> SQLWithParameters createQuery(DAO<DAOType> daoMeta, Object dao) {
        if (daoMeta.getNumberOfIdColumns() == 0) throw new KnuckleBonesException.OperationRequiresIdOnAtLeastOneField(daoMeta.getType());

        SQLWithParameters sql = new SQLWithParameters();
        sql.append("DELETE FROM ");
        sql.append(daoMeta.getTableName());
        sql.append(" WHERE ");

        String sep = "";

        for (DAOColumnField daoColumnField : daoMeta.getColumns()) {
            if (!daoColumnField.isId()) continue;
            sql.append(sep);
            sql.append(daoColumnField.getName());
            sql.append(" = ?");
            sql.add(daoColumnField.getField(), dao);
            sep = " AND ";
        }
        return sql;
    }
}
