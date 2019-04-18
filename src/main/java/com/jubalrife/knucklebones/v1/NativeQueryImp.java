package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

class NativeQueryImp<QueryResultType> implements Persistence.NativeQuery<QueryResultType> {
    private final Connection connection;
    private final SupportedTypesRegistered supportedTypes;
    private final DAOFactory cache;

    private final Class<QueryResultType> type;
    private final String sql;
    private final HashMap<String, Object> parameters = new HashMap<>();

    NativeQueryImp(Connection connection, SupportedTypesRegistered supportedTypes, DAOFactory cache, Class<QueryResultType> type, String sql) {
        this.connection = connection;
        this.supportedTypes = supportedTypes;
        this.cache = cache;
        this.type = type;
        this.sql = sql;
    }


    public NativeQueryImp<QueryResultType> setParameter(String key, Object value) {
        this.parameters.put(key, value);
        return this;
    }

    public List<QueryResultType> findResults() {
        ParameterizedQuery parameterizedQuery = ParameterizedQuery.create(sql);
        parameterizedQuery.setParameters(parameters);
        DAO<QueryResultType> dao = cache.create(type);

        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.execute(
                connection,
                parameterizedQuery.getQuery(),
                parameterizedQuery.getParameters(),
                supportedTypes
        )) {
            try (ResultSet result = statement.executeQuery()) {
                return dao.fillFromResultSet(result, supportedTypes);
            }
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotFetchData(e);
        }
    }

    public QueryResultType findSingleResult() {
        List<QueryResultType> results = findResults();
        if (results.size() != 1) {
            throw new KnuckleBonesException.ExpectedSingeResult(results.size());
        }
        return results.get(0);
    }

}
