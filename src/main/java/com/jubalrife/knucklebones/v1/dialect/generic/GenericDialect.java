package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.PersistenceContext;
import com.jubalrife.knucklebones.v1.dialect.Dialect;

import java.util.List;

public class GenericDialect implements Dialect {
    @Override
    @SuppressWarnings("unchecked")
    public <Type> Type insert(PersistenceContext context, Type object) {
        return new GenericInsert().insert(context, object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Type> void insert(PersistenceContext context, List<Type> object) {
        new GenericInsert().insert(context, object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Type> Type find(PersistenceContext context, Type object) {
        return new GenericFindSingle().find(context, object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int update(PersistenceContext context, Object o) {
        return new GenericUpdate().update(context, o);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Type> int update(PersistenceContext context, List<Type> toUpdate) {
        return new GenericUpdate().update(context, toUpdate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int delete(PersistenceContext context, Object o) {
        return new GenericDelete().delete(context, o);
    }
}
