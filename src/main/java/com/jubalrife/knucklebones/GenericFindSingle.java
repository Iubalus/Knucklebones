package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.exception.KnuckleBonesException.CouldNotFetchData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenericFindSingle {
    public <DAOType> DAOType find(Connection c, Class<DAOType> type, Object o) {
        DAO<DAOType> dao = DAOFactory.create(type);
        SQLWithParameters sql = new SQLWithParameters();
        sql.append("SELECT * FROM ");
        sql.append(dao.getTableName());
        sql.append(" WHERE ");
        String sep = "";

        for (DAOColumnField DAOColumnField : dao.getColumns()) {
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
                resultList = dao.fillFromResultSet(s);
            }
        } catch (SQLException e) {
            throw new CouldNotFetchData(e);
        }

        return resultList.get(0);
    }


}
