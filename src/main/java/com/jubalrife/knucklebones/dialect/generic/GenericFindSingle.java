package com.jubalrife.knucklebones.dialect.generic;

import com.jubalrife.knucklebones.*;
import com.jubalrife.knucklebones.exception.KnuckleBonesException.CouldNotFetchData;
import com.jubalrife.knucklebones.exception.KnuckleBonesException.OperationRequiresIdOnAtLeastOneField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenericFindSingle {
    public <DAOType> DAOType find(Connection c, Object o, DAO<DAOType> daoMeta) {
        if (daoMeta.getNumberOfIdColumns() == 0) throw new OperationRequiresIdOnAtLeastOneField(daoMeta.getType());

        SQLWithParameters sql = new SQLWithParameters();
        sql.append("SELECT * FROM ");
        sql.append(daoMeta.getTableName());
        sql.append(" WHERE ");
        String sep = "";

        for ( DAOColumnField DAOColumnField : daoMeta.getColumns()) {
            if (!DAOColumnField.isId()) continue;
            sql.append(sep);
            sql.append(DAOColumnField.getName());
            sql.add(DAOColumnField.getField(), o);
            sql.append(" = ?");
            sep = " AND ";
        }

        List<DAOType> resultList;
        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement find = executor.execute(c, sql.getSql(), sql.getParameters())) {
            try (ResultSet s = find.executeQuery()) {
                resultList = daoMeta.fillFromResultSet(s);
            }
        } catch (SQLException e) {
            throw new CouldNotFetchData(e);
        }

        if (resultList.size() == 1)
            return resultList.get(0);
        return null;
    }


}
