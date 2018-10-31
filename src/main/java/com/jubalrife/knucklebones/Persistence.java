package com.jubalrife.knucklebones;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Persistence {

    private Connection connection;

    public Persistence(Connection connection) {
        this.connection = connection;
    }

    public <DAO> List<DAO> find(String query, Class<DAO> type) throws SQLException {
        try (ResultSet result = connection.prepareStatement(query).executeQuery()) {
            return DAOFiller.fillFromResultSet(type, result);
        }
    }
}
