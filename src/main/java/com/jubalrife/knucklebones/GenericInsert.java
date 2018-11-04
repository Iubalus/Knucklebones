package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.exception.KnuckleBonesException;
import com.jubalrife.knucklebones.exception.KnuckleBonesException.PropertyInaccessible;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenericInsert {
    public <Type> Type insert(DAO<Type> meta, Type o, Connection connection) {
        List<ColumnField> columns1 = meta.getColumns();
        SQLWithParameters sql = new SQLWithParameters();
        sql.append("INSERT INTO ");
        sql.append(meta.getTableName());
        sql.append("(");
        String sep = "";

        for (ColumnField columnField1 : columns1) {
            if (!columnField1.isGenerated()) {
                sql.append(sep);
                sql.append(columnField1.getName());
                sep = ", ";
            }
        }
        sql.append(") VALUES (");
        sep = "";
        for (ColumnField columnField1 : columns1) {
            if (!columnField1.isGenerated()) {
                sql.append(sep);
                sql.append("?");
                sql.add(columnField1.getField(), o);
                sep = ", ";
            }
        }
        sql.append(")");

        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.execute(connection, sql.getSql(), sql.getParameters())) {
            statement.executeUpdate();

            ColumnField generatedKey = meta.getGeneratedId();
            if (generatedKey == null) {
                return o;
            }

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                Object o1 = SupportedTypes.getExtractor(keys.getMetaData().getColumnType(1), generatedKey.getField().getType()).extract(1, keys);
                try {
                    generatedKey.getField().set(o, o1);
                } catch (IllegalAccessException e) {
                    throw new PropertyInaccessible(generatedKey.getField(), meta.getType(), e);
                }
            }

            if (meta.hasAdditionalGeneratedColumns()) {
                return new GenericFindSingle().find(connection, meta.getType(), o);
            }

        } catch (SQLException e) {
            throw new KnuckleBonesException("Unable to insert", e);
        }

        return o;
    }
}
