package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.dialect.Dialect;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by calling {@link PersistenceFactory#create()}.
 */
public class Persistence implements AutoCloseable {
    private Connection connection;
    private Dialect dialect;
    private SupportedTypesRegistered supportedTypes;
    private DAOFactory cache;

    Persistence(Connection connection, Dialect dialect, DAOFactory cache) {
        this.connection = connection;
        this.dialect = dialect;
        this.cache = cache;
        supportedTypes = new SupportedTypesRegistered();
    }

    @SuppressWarnings("unchecked")
    public <ResultType> ResultType find(ResultType item) {
        return dialect.find(connection, cache.create((Class<ResultType>) item.getClass()), item, supportedTypes);
    }

    @SuppressWarnings("unchecked")
    public <ResultType> ResultType insert(ResultType o) {
        return dialect.insert(connection, cache.create((Class<ResultType>) o.getClass()), o, supportedTypes);
    }

    @SuppressWarnings("unchecked")
    public <ResultType> void insert(List<ResultType> o) {
        if (o.isEmpty()) return;

        dialect.insert(connection, cache.create((Class<ResultType>) o.get(0).getClass()), o, supportedTypes);
    }

    public int update(Object o) {
        return dialect.update(connection, cache.create(o.getClass()), o, supportedTypes);
    }

    @SuppressWarnings("unchecked")
    public <Type> int update(List<Type> o) {
        if (o.isEmpty()) return 0;
        DAO<Type> daoMeta = (DAO<Type>) cache.create(o.get(0).getClass());
        return dialect.update(connection, daoMeta, o, supportedTypes);
    }

    public int delete(Object o) {
        return dialect.delete(connection, cache.create(o.getClass()), o, supportedTypes);
    }

    public <ResultType> NativeQuery<ResultType> createNativeQuery(String query, Class<ResultType> type) {
        return new NativeQuery<>(type, query);
    }

    /**
     * Creating a native query will require a sql expression that can contain colon parameters.
     * Parameters in the string will need to be populates with {@link UncheckedNativeQuery#setParameter(String, Object)}
     * using colon parameters will sanitize the provided value.
     *
     * @param query a string containing colon parameters.
     * @return a new {@link UncheckedNativeQuery}
     */
    public UncheckedNativeQuery createNativeQuery(String query) {
        return new UncheckedNativeQuery(query);
    }

    /**
     * @return the {@link SupportedTypes} for this persistence.
     */
    public SupportedTypes getSupportedTypes() {
        return supportedTypes;
    }

    SupportedTypesRegistered getSupportedTypesRegistered() {
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
            throw new KnuckleBonesException.CouldNotCreateATransaction(e);
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
            throw new KnuckleBonesException.CouldNotRollbackATransaction(e);
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
            throw new KnuckleBonesException.CouldNotCommitATransaction(e);
        }
    }

    /**
     * Closes the underlying connection.
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new KnuckleBonesException("Unable to close connection.", e);
        }
    }

    public class NativeQuery<QueryResultType> {
        private Class<QueryResultType> type;
        private final ParameterizedQuery parameterizedQuery;
        private final HashMap<String, Object> parameters = new HashMap<>();

        NativeQuery(Class<QueryResultType> type, String sql) {
            this.type = type;
            parameterizedQuery = ParameterizedQuery.create(sql);
        }

        /**
         * @param key   the name of the colon parameter in the query to replace
         * @param value the value to use when running the query
         * @return this to allow for chaining.
         */
        public NativeQuery<QueryResultType> setParameter(String key, Object value) {
            this.parameters.put(key, value);
            return this;
        }

        public List<QueryResultType> findResults() {
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

    /**
     * An Unchecked Native Query allows a user to get results form a sql query.
     * It will perform parameter sanitation on any colon parameters that are set using
     * {@link UncheckedNativeQuery#setParameter}.
     * <p>
     * Results can be retrieved by either {@link UncheckedNativeQuery#findSingleResult()} or
     * {@link UncheckedNativeQuery#findResults()}
     */
    public class UncheckedNativeQuery {
        private final ParameterizedQuery parameterizedQuery;
        private final HashMap<String, Object> parameters = new HashMap<>();

        private UncheckedNativeQuery(String query) {
            this.parameterizedQuery = ParameterizedQuery.create(query);
        }

        /**
         * @param key   the name of the colon parameter in the query to replace
         * @param value the value to use when running the query
         * @return this to allow for chaining.
         */
        public UncheckedNativeQuery setParameter(String key, Object value) {
            this.parameters.put(key, value);
            return this;
        }

        /**
         * Executes the statement as an update. The number of rows modified will be returned.
         *
         * @return the number of rows modified in the update statement.
         */
        public int executeUpdate() {
            PreparedStatementExecutor executor = new PreparedStatementExecutor();
            parameterizedQuery.setParameters(parameters);
            try (PreparedStatement statement = executor.execute(
                    connection,
                    parameterizedQuery.getQuery(),
                    parameterizedQuery.getParameters(),
                    supportedTypes
            )) {
                return statement.executeUpdate();
            } catch (SQLException e) {
                throw new KnuckleBonesException.CouldNotUpdateData(e);
            }
        }


        /**
         * Executes the query with the provided parameters expecting exactly one result to be returned.
         *
         * @param <DesiredType> the expected type should be either an Object[] if more than one column is specified in the result
         *                      or if the result has only one column the expected type of that column.
         * @return the first row in the result after running the query with the provided parameters.
         */
        public <DesiredType> DesiredType findSingleResult() {
            List<DesiredType> results = findResults();

            if (results.size() != 1) {
                throw new KnuckleBonesException.ExpectedSingeResult(results.size());
            }

            return results.get(0);
        }

        /**
         * Executes the query with the provided parameters.
         *
         * @param <DesiredType> the expected type should be either a List of Object[] if more than one column is specified in the result
         *                      or if the result has only one column a List of the expected type of that column.
         * @return the first row in the result after running the query with the provided parameters.
         */
        @SuppressWarnings("unchecked")
        public <DesiredType> List<DesiredType> findResults() {
            parameterizedQuery.setParameters(parameters);
            ArrayList<Object> resultList = new ArrayList<>();

            PreparedStatementExecutor executor = new PreparedStatementExecutor();
            try (PreparedStatement statement = executor.execute(
                    connection,
                    parameterizedQuery.getQuery(),
                    parameterizedQuery.getParameters(),
                    supportedTypes
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
}
