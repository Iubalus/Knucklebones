package com.jubalrife.knucklebones.v1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PreparedStatementExecutor {
    public PreparedStatement execute(
            Connection connection,
            String query,
            List<Object> parameters,
            SupportedTypesRegistered supportedTypes
    ) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        setParameters(parameters, supportedTypes, statement);
        return statement;
    }

    public PreparedStatement prepareUpdate(Connection connection, String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public void setParameters(List<Object> parameters, SupportedTypesRegistered supportedTypes, PreparedStatement statement) throws SQLException {
        statement.clearParameters();
        for (int i = 0; i < parameters.size(); i++) {
            Object parameter = parameters.get(i);
            supportedTypes
                    .getRefiner(parameter == null ? null : parameter.getClass())
                    .refine(i + 1, parameter, statement);
        }
    }

    public PreparedStatement prepareInsert(Connection connection, String query) throws SQLException {
        return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }
}
