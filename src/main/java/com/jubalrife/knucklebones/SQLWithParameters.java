package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.exception.KnuckleBonesException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SQLWithParameters {
    private StringBuilder sql = new StringBuilder();
    private List<Object> parameters = new ArrayList<>();

    public void append(String text) {
        sql.append(text);
    }

    public void add(Field field, Object object) {
        try {
            parameters.add(field.get(object));
        } catch (IllegalAccessException e) {
            throw new KnuckleBonesException.PropertyInaccessible(field, field.getDeclaringClass(), e);
        }
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public String getSql() {
        return sql.toString();
    }
}
