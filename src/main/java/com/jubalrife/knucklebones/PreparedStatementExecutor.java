package com.jubalrife.knucklebones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PreparedStatementExecutor {
    public PreparedStatement execute(Connection connection, String query, List<Object> parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < parameters.size(); i++) {
            Object parameter = parameters.get(i);
            SupportedTypes.getRefiner(parameter == null ? null : parameter.getClass()).refine(i + 1, parameter, statement);
        }
        return statement;
    }
}
