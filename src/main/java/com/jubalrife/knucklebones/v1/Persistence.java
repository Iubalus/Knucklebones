package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.Connection;
import java.util.List;

/**
 * Created by calling {@link PersistenceFactory#create()}.
 */
public interface Persistence extends AutoCloseable {

    /**
     * Attempt to perform a SQL SELECT statement for the record specified.
     * The record will be found by the fields annotated with {@link com.jubalrife.knucklebones.v1.annotation.Id}.
     *
     * @param record       to find
     * @param <ResultType> the type of the record to find
     * @return The record found.
     */
    <ResultType> ResultType find(ResultType record);

    /**
     * Attempt to perform a SQL INSERT statement for the record provided.
     *
     * @param record       to insert
     * @param <ResultType> type of the record to insert.
     * @return the record inserted with its generated key.
     */
    <ResultType> ResultType insert(ResultType record);

    <ResultType> void insert(List<ResultType> o);

    /**
     * Attempt to perform a SQL UPDATE statement using fields annotated with {@link com.jubalrife.knucklebones.v1.annotation.Id}
     * in the where clause to specify the record to update. Fields annotated with {@link com.jubalrife.knucklebones.v1.annotation.Id}
     * will not be updated.
     *
     * @param record to update
     * @return the number of rows modified
     */
    int update(Object record);

    <Type> int update(List<Type> o);

    /**
     * Attempt to perform a sql DELETE Statement using fields annotated with {@link com.jubalrife.knucklebones.v1.annotation.Id}
     * in the where clause to specify the record to delete.
     *
     * @param record to delete
     * @return the number of entries deleted.
     */
    int delete(Object record);

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
    <ResultType> NativeQuery<ResultType> createNativeQuery(String query, Class<ResultType> type);

    /**
     * Creating a native query will require a sql expression that can contain colon parameters.
     * Parameters in the string will need to be populates with {@link UncheckedNativeQuery#setParameter(String, Object)}.
     * Using colon parameters will sanitize the provided value.
     *
     * @param query a string containing colon parameters.
     * @return a new {@link UncheckedNativeQuery}
     */
    UncheckedNativeQuery createNativeQuery(String query);

    /**
     * @return the {@link SupportedTypes} for this persistence.
     */
    SupportedTypes getSupportedTypes();

    /**
     * Modifying the underlying connection can lead to undefined behavior.
     *
     * @return the underlying {@link Connection}
     */
    Connection getConnection();

    /**
     * Calling this method will set the underlying connection to stop auto committing each statement.
     * {@link #commit()} or {@link #rollback()} must be called after this method to either keep or reject changes made in
     * the transaction that begin creates.
     */
    void begin();

    /**
     * Calling this method will rollback the current transaction.
     * See {@link #begin()} to begin a transaction
     */
    void rollback();

    /**
     * Calling this method will commit the current transaction.
     * See {@link #begin()} to begin a transaction
     */
    void commit();

    /**
     * Closes the underlying connection.
     */
    void close();

    /**
     * Utility which allows for atomic execution of provided logic using an isolated persistence.
     *
     * @param factory       is the {@link PersistenceFactory} which will supplie a {@link Persistence} for the duration of the transaction
     * @param inTransaction will contain the work that must be done in a transaction.
     * @param errorHandler  will consume Exceptions produced during execution of the transaction (both setup and execution)
     * @throws KnuckleBonesException.FeatureUnavailable if begin, commit, rollback, inTransaction, or getConnection is called on the {@link Persistence} supplied to {@link TransactionWrappedOperation}.
     */
    static void inTransaction(PersistenceFactory factory, TransactionWrappedOperation inTransaction, ErrorHandler errorHandler) {
        factory.inTransaction(inTransaction, errorHandler);
    }

    /**
     * This will use the persistence it is called from to perform an operation in a transaction
     * The persistence will not be closed when the operation is completed
     *
     * @param inTransaction will contain the work to be completed in a transaction
     * @param errorHandler will consume exceptions produced during execution of the transaction
     * @throws KnuckleBonesException.FeatureUnavailable if begin, commit, rollback, inTransaction, or getConnection is called on the {@link Persistence} supplied to {@link TransactionWrappedOperation}.
     */
    void inTransaction(TransactionWrappedOperation inTransaction, ErrorHandler errorHandler);

    interface TransactionWrappedOperation {
        void run(Persistence persistence) throws Exception;
    }

    interface ErrorHandler {
        void accept(Exception e);
    }

    /**
     * A Native Query allows a user to get results form a sql query.
     * It will perform parameter sanitation on any colon parameters that are set using
     * {@link NativeQuery#setParameter}.
     * <p>
     * Results can be retrieved by either {@link NativeQuery#findSingleResult()} or
     * {@link NativeQuery#findResults()}
     */
    interface NativeQuery<ResultType> {
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
    interface UncheckedNativeQuery {
        /**
         * @param key   the name of the colon parameter in the query to replace
         * @param value the value to use when running the query
         * @return this to allow for chaining.
         */
        UncheckedNativeQuery setParameter(String key, Object value);

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
