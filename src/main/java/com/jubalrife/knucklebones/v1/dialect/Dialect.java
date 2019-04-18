package com.jubalrife.knucklebones.v1.dialect;

import com.jubalrife.knucklebones.v1.PersistenceContext;

import java.util.List;

public interface Dialect {
    <Type> Type insert(PersistenceContext context, Type object);

    <Type> void insert(PersistenceContext context, List<Type> object);

    <Type> Type find(PersistenceContext context, Type object);

    int update(PersistenceContext context, Object object);

    <Type> int update(PersistenceContext context, List<Type> object);

    int delete(PersistenceContext context, Object object);
}
