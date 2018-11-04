package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.exception.KnuckleBonesException.CouldNotFetchData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Persistence {

    private Connection connection;

    public Persistence(Connection connection) {
        this.connection = connection;
    }

    @SuppressWarnings("unchecked")
    public <ResultType> ResultType find(ResultType item) {
        return new GenericFindSingle().<ResultType>find(connection, (Class<ResultType>) item.getClass(), item);
    }

    public <ResultType> NativeQuery<ResultType> createNativeQuery(String query, Class<ResultType> type) {
        return new NativeQuery<>(type, query);
    }

    @SuppressWarnings("unchecked")
    public <ResultType> ResultType insert(ResultType o) {
        return new GenericInsert().insert(new DAO<ResultType>((Class<ResultType>) o.getClass()), o, connection);
    }

    public class NativeQuery<QueryResultType> {
        private Class<QueryResultType> type;
        private final ParameterizedQuery parameterizedQuery;
        private final HashMap<String, Object> parameters = new HashMap<>();

        NativeQuery(Class<QueryResultType> type, String sql) {
            this.type = type;
            parameterizedQuery = ParameterizedQuery.create(sql);
        }

        public NativeQuery<QueryResultType> setParameter(String key, Object value) {
            this.parameters.put(key, value);
            return this;
        }

        public List<QueryResultType> findResults() {
            parameterizedQuery.setParameters(parameters);

            PreparedStatementExecutor executor = new PreparedStatementExecutor();
            try (PreparedStatement statement = executor.execute(connection, parameterizedQuery.getQuery(), parameterizedQuery.getParameters())) {
                try (ResultSet result = statement.executeQuery()) {
                    return DAOFiller.fillFromResultSet(type, result);
                }
            } catch (SQLException e) {
                throw new CouldNotFetchData(e);
            }
        }
    }
}
