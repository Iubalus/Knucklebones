package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.annotation.Table;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class DAO<Type> {
    private final Class<Type> type;
    private String tableName;
    private List<DAOColumnField> columns;
    private DAOColumnField generatedId;
    private int numberOfGeneratedColumns = 0;
    private int numberOfIdColumns = 0;

    DAO(Class<Type> type) {
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
        ArrayList<DAOColumnField> columns = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            DAOColumnField e = new DAOColumnField(field);
            if (e.isGenerated()) numberOfGeneratedColumns++;
            if (e.isId()) numberOfIdColumns++;
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

    public List<DAOColumnField> getColumns() {
        return columns;
    }

    public Class<Type> getType() {
        return type;
    }

    public DAOColumnField getGeneratedId() {
        return generatedId;
    }

    public boolean hasAdditionalGeneratedColumns() {
        return numberOfGeneratedColumns > 1;
    }

    public int getNumberOfIdColumns() {
        return numberOfIdColumns;
    }

    public List<Type> fillFromResultSet(ResultSet resultSet, SupportedTypesRegistered supportedTypes) {
        Map<DAOColumnField, Integer> fieldToColumn = createFieldToColumnMapping(resultSet);

        ArrayList<Type> results = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Type type = newInstance();
                fillObject(resultSet, supportedTypes, type, fieldToColumn);
                results.add(type);
            }
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotFetchData(e);
        }

        return results;
    }

    public void fillFromResultSet(ResultSet resultSet, SupportedTypesRegistered supportedTypes, Type instance) {
        Map<DAOColumnField, Integer> fieldToColumn = createFieldToColumnMapping(resultSet);
        try {
            if (resultSet.next()) {
                fillObject(resultSet, supportedTypes, instance, fieldToColumn);
            } else {
                throw new KnuckleBonesException.CouldNotFetchData();
            }
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotFetchData(e);
        }

    }

    private void fillObject(ResultSet resultSet, SupportedTypesRegistered supportedTypes, Type instance, Map<DAOColumnField, Integer> fieldToColumn) throws SQLException {
        for (Map.Entry<DAOColumnField, Integer> toMap : fieldToColumn.entrySet()) {
            DAOColumnField key = toMap.getKey();
            try {
                key.getField().set(instance, extractValue(resultSet, toMap, key, supportedTypes));
            } catch (IllegalAccessException e) {
                throw new KnuckleBonesException.PropertyInaccessible(key.getField(), getType(), e);
            }
        }
    }

    private Map<DAOColumnField, Integer> createFieldToColumnMapping(ResultSet resultSet) {
        Map<DAOColumnField, Integer> fieldToColumn = new HashMap<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnLabel = metaData.getColumnLabel(i);
                for (DAOColumnField daoColumnField : getColumns()) {
                    if (columnLabel.equalsIgnoreCase(daoColumnField.getName())) {
                        fieldToColumn.put(daoColumnField, metaData.getColumnType(i));
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotFetchData(e);
        }
        return fieldToColumn;
    }

    private Object extractValue(
            ResultSet resultSet,
            Map.Entry<DAOColumnField, Integer> toMap,
            DAOColumnField key,
            SupportedTypesRegistered supportedTypes
    ) throws SQLException {
        return supportedTypes
                .getExtractor(toMap.getValue(), key.getField().getType())
                .extract(resultSet.findColumn(key.getName()), resultSet);
    }

    public Type newInstance() {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new KnuckleBonesException.CouldNotConstruct(type, e);
        }
    }

}
