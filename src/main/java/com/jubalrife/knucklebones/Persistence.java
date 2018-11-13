package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.dialect.Dialect;
import com.jubalrife.knucklebones.exception.KnuckleBonesException.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by calling {@link PersistenceFactory#create()}.
 */
public class Persistence implements AutoCloseable {
    private Connection connection;
    private Dialect dialect;
    private SupportedTypesRegistered supportedTypes;

    Persistence(Connection connection, Dialect dialect) {
        this.connection = connection;
        this.dialect = dialect;
        supportedTypes = new SupportedTypesRegistered();
    }

    @SuppressWarnings("unchecked")
    public <ResultType> ResultType find(ResultType item) {
        return dialect.find(connection, DAOFactory.create((Class<ResultType>) item.getClass()), item, supportedTypes);
    }

    @SuppressWarnings("unchecked")
    public <ResultType> ResultType insert(ResultType o) {
        return dialect.insert(connection, DAOFactory.create((Class<ResultType>) o.getClass()), o, supportedTypes);
    }

    public int update(Object o) {
        return dialect.update(connection, DAOFactory.create(o.getClass()), o, supportedTypes);
    }

    public int delete(Object o) {
        return dialect.delete(connection, DAOFactory.create(o.getClass()), o, supportedTypes);
    }

    public <ResultType> NativeQuery<ResultType> createNativeQuery(String query, Class<ResultType> type) {
        return new NativeQuery<>(type, query);
    }

    /**
     * @return the {@link SupportedTypes} for this persistence.
     */
    public SupportedTypes getSupportedTypes() {
        return supportedTypes;
    }

    SupportedTypesRegistered getSupportedTypesRegistered(){
        return supportedTypes;
    }

    /**
     * Modifying the underlying connection can lead to undefined behavior.
     *
     * @return the underlying {@link Connection}
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Calling this method will set the underlying connection to stop auto committing each statement.
     * {@link #commit()} or {@link #rollback()} must be called after this method to either keep or reject changes made in
     * the transaction that begin creates.
     */
    public void begin() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new CouldNotCreateATransaction(e);
        }

    }

    /**
     * Calling this method will rollback the current transaction.
     * See {@link #begin()} to begin a transaction
     */
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new CouldNotRollbackATransaction(e);
        }
    }

    /**
     * Calling this method will commit the current transaction.
     * See {@link #begin()} to begin a transaction
     */
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new CouldNotCommitATransaction(e);
        }
    }

    /**
     * Closes the underlying connection.
     */
    public void close() throws Exception {
        connection.close();
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
            DAO<QueryResultType> dao = DAOFactory.create(type);

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
                throw new CouldNotFetchData(e);
            }
        }
    }
}
