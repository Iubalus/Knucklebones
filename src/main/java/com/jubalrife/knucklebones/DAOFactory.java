package com.jubalrife.knucklebones;

import java.util.HashMap;

public class DAOFactory {
    private static HashMap<Class<?>, DAO<?>> cache = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> DAO<T> create(Class<T> type) {
        if (cache.containsKey(type)) return (DAO<T>) cache.get(type);
        DAO<T> dao = new DAO<>(type);
        cache.put(type, dao);
        return dao;
    }
}
