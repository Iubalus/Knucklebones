package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class PersistenceBase implements Persistence {
    private final PersistenceContext persistenceContext;

    PersistenceBase(PersistenceContext persistenceContext) {
        this.persistenceContext = persistenceContext;
    }

    @SuppressWarnings("unchecked")
    public <ResultType> ResultType find(ResultType record) {
        return (ResultType) persistenceContext.find(record);
    }

    @SuppressWarnings("unchecked")
    public <ResultType> ResultType insert(ResultType record) {
        return (ResultType) persistenceContext.insert(record);
    }

    @SuppressWarnings("unchecked")
    public <ResultType> void insert(List<ResultType> o) {
        if (o.isEmpty()) return;

        persistenceContext.insert((List<Object>) o);
    }

    public int update(Object record) {
        return persistenceContext.update(record);
    }

    @SuppressWarnings("unchecked")
    public <Type> int update(List<Type> o) {
        if (o.isEmpty()) return 0;
        return persistenceContext.update((List<Object>) o);
    }

    public int delete(Object record) {
        return persistenceContext.delete(record);
    }

    public <ResultType> Persistence.NativeQuery<ResultType> createNativeQuery(String query, Class<ResultType> type) {
        return new NativeQueryImp<>(persistenceContext, type, query);
    }

    public Persistence.UncheckedNativeQuery createNativeQuery(String query) {
        return new UncheckedNativeQueryImp(persistenceContext, query);
    }

    public SupportedTypes getSupportedTypes() {
        return persistenceContext.getSupportedTypes();
    }

    public Connection getConnection() {
        return persistenceContext.getConnection();
    }

    public void begin() {
        try {
            persistenceContext.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotCreateATransaction(e);
        }

    }

    public void rollback() {
        try {
            persistenceContext.getConnection().rollback();
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotRollbackATransaction(e);
        }
    }

    public void commit() {
        try {
            persistenceContext.getConnection().commit();
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotCommitATransaction(e);
        }
    }

    public void close() {
        try {
            persistenceContext.getConnection().close();
        } catch (SQLException e) {
            throw new KnuckleBonesException("Unable to close connection.", e);
        }
    }

}
