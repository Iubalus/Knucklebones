package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.Connection;
import java.util.List;

class PersistenceAutoTransactionWrap implements Persistence {

    private Persistence delegate;

    public PersistenceAutoTransactionWrap(Persistence delegate) {
        this.delegate = delegate;
    }

    @Override
    public <ResultType> ResultType find(ResultType record) {
        return delegate.find(record);
    }

    @Override
    public <ResultType> ResultType insert(ResultType record) {
        return delegate.insert(record);
    }

    @Override
    public <ResultType> void insert(List<ResultType> o) {
        delegate.insert(o);
    }

    @Override
    public int update(Object record) {
        return delegate.update(record);
    }

    @Override
    public <Type> int update(List<Type> o) {
        return delegate.update(o);
    }

    @Override
    public int delete(Object record) {
        return delegate.delete(record);
    }

    @Override
    public <ResultType> NativeQuery<ResultType> createNativeQuery(String query, Class<ResultType> type) {
        return delegate.createNativeQuery(query, type);
    }

    @Override
    public UncheckedNativeQuery createNativeQuery(String query) {
        return delegate.createNativeQuery(query);
    }

    @Override
    public SupportedTypes getSupportedTypes() {
        return delegate.getSupportedTypes();
    }

    @Override
    public Connection getConnection() {
        throw new KnuckleBonesException.FeatureUnavailable("getConnection is not available from inside Persistence.inTransaction");
    }

    @Override
    public void begin() {
        throw new KnuckleBonesException.FeatureUnavailable("begin is not available from inside Persistence.inTransaction");
    }

    @Override
    public void rollback() {
        throw new KnuckleBonesException.FeatureUnavailable("rollback is not available from inside Persistence.inTransaction");
    }

    @Override
    public void commit() {
        throw new KnuckleBonesException.FeatureUnavailable("commit is not available from inside Persistence.inTransaction");
    }

    @Override
    public void close() {
        throw new KnuckleBonesException.FeatureUnavailable("close is not available from inside Persistence.inTransaction");
    }
}
