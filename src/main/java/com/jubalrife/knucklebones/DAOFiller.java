package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.exception.KnuckleBonesException;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAOFiller {

    private DAOFiller() {}

    static <DAO> List<DAO> fillFromResultSet(Class<DAO> type, ResultSet source) {
        ArrayList<DAO> filled = new ArrayList<>();
        try {
            Map<Field, Integer> fieldToColumn = mapFieldsToColumns(type, source);
            while (source.next()) {
                DAO fill = type.newInstance();
                for (Map.Entry<Field, Integer> field : fieldToColumn.entrySet()) {
                    try {
                        field.getKey().set(fill, extractValue(field.getValue(), source));
                    } catch (IllegalAccessException e) {
                        throw new PropertyInaccessible(field.getKey(), type, e);
                    }
                }
                filled.add(fill);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CouldNotConstruct(type, e);
        } catch (SQLException e) {
            throw new CouldNotFetchData(e);
        }
        return filled;
    }

    private static <DAO> Map<Field, Integer> mapFieldsToColumns(Class<DAO> type, ResultSet source) throws SQLException {
        Map<Field, Integer> fieldToColumn = new HashMap<>();
        for (Field field : type.getDeclaredFields()) {
            fieldToColumn.put(field, source.findColumn(field.getName()));
        }
        return fieldToColumn;
    }

    private static Object extractValue(Integer columnIndex, ResultSet source) throws SQLException {
        return SupportedTypes.getExtractor(source.getMetaData().getColumnType(columnIndex)).extract(columnIndex, source);
    }

    static class PropertyInaccessible extends KnuckleBonesException {
        PropertyInaccessible(Field field, Class<?> type, Throwable cause) {
            super(String.format("Could not access property %s of %s", field.getName(), type.getName()), cause);
        }
    }

    static class CouldNotConstruct extends KnuckleBonesException {
        CouldNotConstruct(Class<?> type, Throwable cause) {
            super(String.format("Could not construct instance of %s", type.getName()), cause);
        }
    }

    static class CouldNotFetchData extends KnuckleBonesException {
        public CouldNotFetchData(Throwable cause) {
            super("Failed to fetch data", cause);
        }
    }
}
