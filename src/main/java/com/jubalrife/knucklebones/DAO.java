package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.annotation.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DAO<Type> {
    private final Class<Type> type;
    private String tableName;
    private List<ColumnField> columns;
    private ColumnField generatedId;
    private int numberOfGeneratedColumns = 0;


    public DAO(Class<Type> type) {
        this.type = type;
        findTableName(type);
        findColumns(type);
    }

    private void findTableName(Class<?> type) {
        if (type.isAnnotationPresent(Table.class)) {
            this.tableName = type.getAnnotation(Table.class).name();
        } else {
            this.tableName = type.getSimpleName();
        }
    }

    private void findColumns(Class<?> type) {
        ArrayList<ColumnField> columns = new ArrayList<>();
        for (Field field : type.getFields()) {
            ColumnField e = new ColumnField(field);
            if (e.isGenerated()) numberOfGeneratedColumns++;
            if (e.isId() && e.isGenerated() && generatedId == null) {
                generatedId = e;
            }
            columns.add(e);
        }
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnField> getColumns() {
        return columns;
    }

    public Class<Type> getType() {
        return type;
    }

    public ColumnField getGeneratedId() {
        return generatedId;
    }

    public boolean hasAdditionalGeneratedColumns() {
        return numberOfGeneratedColumns > 1;
    }

}
