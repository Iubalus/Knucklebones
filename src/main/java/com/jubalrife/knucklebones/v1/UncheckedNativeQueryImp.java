package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;
import com.jubalrife.knucklebones.v1.query.ParameterizedQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class UncheckedNativeQueryImp implements Persistence.UncheckedNativeQuery {
    private final PersistenceContext persistenceContext;
    private final HashMap<String, Object> parameters = new HashMap<>();
    private final String sql;


    UncheckedNativeQueryImp(PersistenceContext persistenceContext, String sql) {
        this.persistenceContext = persistenceContext;
        this.sql = sql;
    }

    public UncheckedNativeQueryImp setParameter(String key, Object value) {
        this.parameters.put(key, value);
        return this;
    }

    public int executeUpdate() {

        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        ParameterizedQuery parameterizedQuery = ParameterizedQuery.create(sql, parameters);
        try (PreparedStatement statement = executor.execute(
                persistenceContext.getConnection(),
                parameterizedQuery.getQuery(),
                parameterizedQuery.getParameters(),
                persistenceContext.getSupportedTypes()
        )) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotUpdateData(e);
        }
    }

    public <DesiredType> DesiredType findSingleResult() {
        List<DesiredType> results = findResults();

        if (results.size() != 1) {
            throw new KnuckleBonesException.ExpectedSingeResult(results.size());
        }

        return results.get(0);
    }


    @SuppressWarnings("unchecked")
    public <DesiredType> List<DesiredType> findResults() {
        ParameterizedQuery parameterizedQuery = ParameterizedQuery.create(sql, parameters);
        ArrayList<Object> resultList = new ArrayList<>();

        PreparedStatementExecutor executor = new PreparedStatementExecutor();
        try (PreparedStatement statement = executor.execute(
                persistenceContext.getConnection(),
                parameterizedQuery.getQuery(),
                parameterizedQuery.getParameters(),
                persistenceContext.getSupportedTypes()
        )) {
            try (ResultSet result = statement.executeQuery()) {
                int columnCount = result.getMetaData().getColumnCount();
                while (result.next()) {
                    if (columnCount > 1) {
                        Object[] resultRow = new Object[columnCount];
                        for (int i = 0; i < columnCount; i++) {
                            resultRow[i] = result.getObject(i + 1);
                        }
                        resultList.add(resultRow);
                    } else {
                        resultList.add(result.getObject(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotFetchData(e);
        }
        return (List<DesiredType>) resultList;
    }
}
