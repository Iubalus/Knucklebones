package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by calling {@link PersistenceFactory#create()}.
 */
public class Persistence implements AutoCloseable {
    private final PersistenceContext persistenceContext;

    public Persistence(PersistenceContext persistenceContext) {
        this.persistenceContext = persistenceContext;
    }

    /**
     * Attempt to perform a SQL SELECT statement for the record specified.
     * The record will be found by the fields annotated with {@link com.jubalrife.knucklebones.v1.annotation.Id}.
     *
     * @param record       to find
     * @param <ResultType> the type of the record to find
     * @return The record found.
     */
    @SuppressWarnings("unchecked")
    public <ResultType> ResultType find(ResultType record) {
        return persistenceContext.getDialect().find(persistenceContext, record);
    }

    /**
     * Attempt to perform a SQL INSERT statement for the record provided.
     *
     * @param record       to insert
     * @param <ResultType> type of the record to insert.
     * @return the record inserted with its generated key.
     */
    @SuppressWarnings("unchecked")
    public <ResultType> ResultType insert(ResultType record) {
        return persistenceContext.getDialect().insert(
                persistenceContext,
                record
        );
    }

    @SuppressWarnings("unchecked")
    public <ResultType> void insert(List<ResultType> o) {
        if (o.isEmpty()) return;

        persistenceContext.getDialect().insert(persistenceContext, o);
    }

    /**
     * Attempt to perform a SQL UPDATE statement using fields annotated with {@link com.jubalrife.knucklebones.v1.annotation.Id}
     * in the where clause to specify the record to update. Fields annotated with {@link com.jubalrife.knucklebones.v1.annotation.Id}
     * will not be updated.
     *
     * @param record to update
     * @return the number of rows modified
     */
    public int update(Object record) {
        return persistenceContext.getDialect().update(persistenceContext, record);
    }

    @SuppressWarnings("unchecked")
    public <Type> int update(List<Type> o) {
        if (o.isEmpty()) return 0;
        return persistenceContext.getDialect().update(persistenceContext, o);
    }

    /**
     * Attempt to perform a sql DELETE Statement using fields annotated with {@link com.jubalrife.knucklebones.v1.annotation.Id}
     * in the where clause to specify the record to delete.
     *
     * @param record to delete
     * @return the number of entries deleted.
     */
    public int delete(Object record) {
        return persistenceContext.getDialect().delete(persistenceContext, record);
    }

    /**
     * Creating a native query will require a sql expression that can contain colon parameters.
     * Parameters in the string will need to be populates with {@link NativeQuery#setParameter(String, Object)}.
     * Using colon parameters will sanitize the provided value.
     *
     * @param query        the sq1 to be run. May contain colon parameters
     * @param type         the class of the expected result(s)
     * @param <ResultType> the type of the expected result(s)
     * @return a Native query to set parameters and retrieve results.
     */
    public <ResultType> NativeQuery<ResultType> createNativeQuery(String query, Class<ResultType> type) {
        return new NativeQueryImp<>(persistenceContext, type, query);
    }

    /**
     * Creating a native query will require a sql expression that can contain colon parameters.
     * Parameters in the string will need to be populates with {@link UncheckedNativeQuery#setParameter(String, Object)}.
     * Using colon parameters will sanitize the provided value.
     *
     * @param query a string containing colon parameters.
     * @return a new {@link UncheckedNativeQuery}
     */
    public UncheckedNativeQueryImp createNativeQuery(String query) {
        return new UncheckedNativeQueryImp(persistenceContext, query);
    }

    /**
     * @return the {@link SupportedTypes} for this persistence.
     */
    public SupportedTypes getSupportedTypes() {
        return persistenceContext.getSupportedTypes();
    }

    SupportedTypesRegistered getSupportedTypesRegistered() {
        return persistenceContext.getSupportedTypes();
    }

    /**
     * Modifying the underlying connection can lead to undefined behavior.
     *
     * @return the underlying {@link Connection}
     */
    public Connection getConnection() {
        return persistenceContext.getConnection();
    }

    /**
     * Calling this method will set the underlying connection to stop auto committing each statement.
     * {@link #commit()} or {@link #rollback()} must be called after this method to either keep or reject changes made in
     * the transaction that begin creates.
     */
    public void begin() {
        try {
            persistenceContext.getConnection().setAutoCommit(false);
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
            persistenceContext.getConnection().rollback();
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
            persistenceContext.getConnection().commit();
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotCommitATransaction(e);
        }
    }

    /**
     * Closes the underlying connection.
     */
    public void close() {
        try {
            persistenceContext.getConnection().close();
        } catch (SQLException e) {
            throw new KnuckleBonesException("Unable to close connection.", e);
        }
    }

    /**
     * A Native Query allows a user to get results form a sql query.
     * It will perform parameter sanitation on any colon parameters that are set using
     * {@link NativeQuery#setParameter}.
     * <p>
     * Results can be retrieved by either {@link NativeQuery#findSingleResult()} or
     * {@link NativeQuery#findResults()}
     */
    public interface NativeQuery<ResultType> {
        /**
         * @param key   the name of the colon parameter in the query to replace
         * @param value the value to use when running the query
         * @return this to allow for chaining.
         */
        NativeQuery<ResultType> setParameter(String key, Object value);

        /**
         * Executes the query with the provided parameters.
         *
         * @return the first row in the result after running the query with the provided parameters.
         */
        List<ResultType> findResults();

        /**
         * Executes the query with the provided parameters.
         *
         * @return the rows in the result after running the query with the provided parameters.
         */
        ResultType findSingleResult();
    }

    /**
     * An Unchecked Native Query allows a user to get results form a sql query.
     * It will perform parameter sanitation on any colon parameters that are set using
     * {@link UncheckedNativeQuery#setParameter}.
     * <p>
     * Results can be retrieved by either {@link UncheckedNativeQuery#findSingleResult()} or
     * {@link UncheckedNativeQuery#findResults()}
     */
    public interface UncheckedNativeQuery {
        /**
         * @param key   the name of the colon parameter in the query to replace
         * @param value the value to use when running the query
         * @return this to allow for chaining.
         */
        UncheckedNativeQueryImp setParameter(String key, Object value);

        /**
         * Executes the statement as an update. The number of rows modified will be returned.
         *
         * @return the number of rows modified in the update statement.
         */
        int executeUpdate();

        /**
         * Executes the query with the provided parameters expecting exactly one result to be returned.
         *
         * @param <DesiredType> the expected type should be either an Object[] if more than one column is specified in the result
         *                      or if the result has only one column the expected type of that column.
         * @return the first row in the result after running the query with the provided parameters.
         */
        <DesiredType> DesiredType findSingleResult();

        /**
         * Executes the query with the provided parameters.
         *
         * @param <DesiredType> the expected type should be either a List of Object[] if more than one column is specified in the result
         *                      or if the result has only one column a List of the expected type of that column.
         * @return the rows in the result after running the query with the provided parameters.
         */
        <DesiredType> List<DesiredType> findResults();
    }
}
