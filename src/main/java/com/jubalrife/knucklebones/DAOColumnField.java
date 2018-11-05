package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.annotation.Column;
import com.jubalrife.knucklebones.annotation.GeneratedValue;
import com.jubalrife.knucklebones.annotation.Id;

import java.lang.reflect.Field;

public class DAOColumnField {
    private final String name;
    private final Field field;
    private final boolean isId;
    private final boolean isGenerated;


    public DAOColumnField(Field field) {
        this.field = field;
        this.name = findName(field);
        this.isId = field.isAnnotationPresent(Id.class);
        this.isGenerated = field.isAnnotationPresent(GeneratedValue.class);
    }

    public String getName() {
        return name;
    }

    public Field getField() {
        return field;
    }

    public boolean isId() {
        return isId;
    }

    public boolean isGenerated() {
        return isGenerated;
    }

    private String findName(Field field) {
        if (field.isAnnotationPresent(Column.class))
            return field.getAnnotation(Column.class).name();
        return field.getName();
    }

}
