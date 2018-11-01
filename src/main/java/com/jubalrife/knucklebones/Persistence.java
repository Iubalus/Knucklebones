package com.jubalrife.knucklebones;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Persistence {

    private Connection connection;
    private HashMap<String, Object> parameters = new HashMap<>();

    public Persistence(Connection connection) {
        this.connection = connection;
    }

    public <DAO> List<DAO> find(String query, Class<DAO> type) throws SQLException {
        ParameterizedQuery parameterizedQuery = ParameterizedQuery.create(query);
        parameterizedQuery.setParameters(parameters);

        try (PreparedStatement statement = connection.prepareStatement(parameterizedQuery.getQuery())) {
            parameterizedQuery.fill(statement);
            try (ResultSet result = statement.executeQuery()) {
                return DAOFiller.fillFromResultSet(type, result);
            }
        }
    }

    public Persistence setParameter(String key, Object value) {
        this.parameters.put(key, value);
        return this;
    }
}
