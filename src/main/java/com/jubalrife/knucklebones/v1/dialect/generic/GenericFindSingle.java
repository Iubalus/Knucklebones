package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.DAO;
import com.jubalrife.knucklebones.v1.PreparedStatementExecutor;
import com.jubalrife.knucklebones.v1.SupportedTypesRegistered;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.*;
import java.util.List;

public class GenericFindSingle {
    public <DAOType> DAOType find(Connection c, DAOType o, DAO<DAOType> daoMeta, SupportedTypesRegistered supportedTypes) {
        if (daoMeta.getNumberOfIdColumns() == 0)
            throw new KnuckleBonesException.OperationRequiresIdOnAtLeastOneField(daoMeta.getType());

        SQLWithParameters sql = new SQLWithParameters();
        sql.append("SELECT * FROM ");
        sql.append(daoMeta.getTableName());
        sql.append(" WHERE ");
        String sep = "";

        for (com.jubalrife.knucklebones.v1.DAOColumnField DAOColumnField : daoMeta.getColumns()) {
            if (!DAOColumnField.isId()) continue;
            sql.append(sep);
            sql.append(DAOColumnField.getName());
            sql.add(DAOColumnField.getField(), o);
            sql.append(" = ?");
            sep = " AND ";
        }


        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement find = executor.execute(c, sql.getSql(), sql.getParameters(), supportedTypes)) {
            try (ResultSet s = find.executeQuery()) {
                daoMeta.fillFromResultSet(s, supportedTypes, o);
            }
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotFetchData(e);
        }

        return o;
    }


}
