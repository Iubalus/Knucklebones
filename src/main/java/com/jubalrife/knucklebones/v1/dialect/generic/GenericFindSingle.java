package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.DAO;
import com.jubalrife.knucklebones.v1.PersistenceContext;
import com.jubalrife.knucklebones.v1.PreparedStatementExecutor;
import com.jubalrife.knucklebones.v1.SupportedTypesRegistered;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.*;

public class GenericFindSingle {
    @SuppressWarnings("unchecked")
    public <DAOType> DAOType find(PersistenceContext context, DAOType o) {
        DAO<DAOType> daoMeta = (DAO<DAOType>) context.getCache().create(o.getClass());
        if (daoMeta.getNumberOfIdColumns() == 0)
            throw new KnuckleBonesException.OperationRequiresIdOnAtLeastOneField(daoMeta.getType());

        SQLWithParameters sql = createQuery(daoMeta, o);


        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement find = executor.execute(context.getConnection(), sql.getSql(), sql.getParameters(), context.getSupportedTypes())) {
            try (ResultSet s = find.executeQuery()) {
                daoMeta.fillFromResultSet(s, context.getSupportedTypes(), o);
            }
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotFetchData(e);
        }

        return o;
    }

    <DAOType> SQLWithParameters createQuery(DAO<DAOType> daoMeta, DAOType o) {
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
        return sql;
    }


}
