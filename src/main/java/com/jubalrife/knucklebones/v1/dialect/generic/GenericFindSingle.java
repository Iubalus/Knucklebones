package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.DAO;
import com.jubalrife.knucklebones.v1.PreparedStatementExecutor;
import com.jubalrife.knucklebones.v1.SupportedTypesRegistered;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenericFindSingle {
    public <DAOType> DAOType find(Connection c, Object o, DAO<DAOType> daoMeta, SupportedTypesRegistered supportedTypes) {
        if (daoMeta.getNumberOfIdColumns() == 0) throw new KnuckleBonesException.OperationRequiresIdOnAtLeastOneField(daoMeta.getType());

        SQLWithParameters sql = new SQLWithParameters();
        sql.append("SELECT * FROM ");
        sql.append(daoMeta.getTableName());
        sql.append(" WHERE ");
        String sep = "";

        for ( com.jubalrife.knucklebones.v1.DAOColumnField DAOColumnField : daoMeta.getColumns()) {
            if (!DAOColumnField.isId()) continue;
            sql.append(sep);
            sql.append(DAOColumnField.getName());
            sql.add(DAOColumnField.getField(), o);
            sql.append(" = ?");
            sep = " AND ";
        }

        List<DAOType> resultList;
        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement find = executor.execute(c, sql.getSql(), sql.getParameters(), supportedTypes)) {
            try (ResultSet s = find.executeQuery()) {
                resultList = daoMeta.fillFromResultSet(s, supportedTypes);
            }
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotFetchData(e);
        }

        if (resultList.size() == 1)
            return resultList.get(0);
        return null;
    }


}
