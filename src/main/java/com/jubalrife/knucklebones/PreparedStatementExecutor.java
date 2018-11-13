package com.jubalrife.knucklebones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PreparedStatementExecutor {
    public PreparedStatement execute(
            Connection connection,
            String query,
            List<Object> parameters,
            SupportedTypesRegistered supportedTypes
    ) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < parameters.size(); i++) {
            Object parameter = parameters.get(i);
            supportedTypes
                    .getRefiner(parameter == null ? null : parameter.getClass())
                    .refine(i + 1, parameter, statement);
        }
        return statement;
    }
}
