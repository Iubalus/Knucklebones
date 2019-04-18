package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;
import com.jubalrife.knucklebones.v1.query.ParameterizedQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

class NativeQueryImp<QueryResultType> implements Persistence.NativeQuery<QueryResultType> {
    private final PersistenceContext persistenceContext;

    private final Class<QueryResultType> type;
    private final String sql;
    private final HashMap<String, Object> parameters = new HashMap<>();

    NativeQueryImp(PersistenceContext persistenceContext, Class<QueryResultType> type, String sql) {
        this.persistenceContext = persistenceContext;
        this.type = type;
        this.sql = sql;
    }

    public NativeQueryImp<QueryResultType> setParameter(String key, Object value) {
        this.parameters.put(key, value);
        return this;
    }

    public List<QueryResultType> findResults() {
        ParameterizedQuery parameterizedQuery = ParameterizedQuery.create(sql, parameters);
        DAO<QueryResultType> dao = persistenceContext.getCache().create(type);

        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.execute(
                persistenceContext.getConnection(),
                parameterizedQuery.getQuery(),
                parameterizedQuery.getParameters(),
                persistenceContext.getSupportedTypes()
        )) {
            try (ResultSet result = statement.executeQuery()) {
                return dao.fillFromResultSet(result, persistenceContext.getSupportedTypes());
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
