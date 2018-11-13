package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.exception.KnuckleBonesException;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class SupportedTypesRegistered implements SupportedTypes {

    private Map<ExtractorKey, Extractor> supported = new HashMap<>();
    private Map<Class<?>, Refiner> supportedRefiners = new HashMap<>();

    SupportedTypesRegistered() {
        registerType(Types.BIGINT, Long.class, (columnIndex, results) -> results.getLong(columnIndex));
        registerType(Types.BIGINT, Long.TYPE, (columnIndex, results) -> results.getLong(columnIndex));

        registerType(Types.BIGINT, Integer.TYPE, (columnIndex, results) -> results.getInt(columnIndex));
        registerType(Types.BIGINT, Integer.class, (columnIndex, results) -> results.getInt(columnIndex));

        registerType(Types.INTEGER, Integer.TYPE, (columnIndex, results) -> results.getInt(columnIndex));
        registerType(Types.INTEGER, Integer.class, (columnIndex, results) -> results.getInt(columnIndex));

        registerType(Types.VARCHAR, String.class, ((columnIndex, results) -> results.getString(columnIndex)));

        registerType(Types.DATE, Date.class, ((columnIndex, results) -> results.getDate(columnIndex)));
        registerType(Types.TIMESTAMP, Date.class, ((columnIndex, results) -> results.getTimestamp(columnIndex)));

        registerType(
                Date.class,
                (index, value, statement) -> statement.setTimestamp(index, new Timestamp(((Date) value).getTime()))
        );

        registerType(Integer.TYPE, (index, value, statement) -> statement.setInt(index, (int) value));
        registerType(Integer.class, (index, value, statement) -> statement.setObject(index, (Integer) value));
        registerType(String.class, (index, value, statement) -> statement.setString(index, (String) value));
        registerType(null, (index, value, statement) -> statement.setObject(index, null));
    }

    public Extractor getExtractor(Integer type, Class<?> toType) {
        Extractor extractor = supported.get(new ExtractorKey(type, toType));
        if (extractor == null) {
            throw new UnsupportedType(type, toType);
        }
        return extractor;
    }

    public Refiner getRefiner(Class<?> type) {
        Refiner refiner = supportedRefiners.get(type);
        if (refiner == null) {
            throw new UnsupportedType(type);
        }
        return refiner;
    }

    public void registerType(Integer type, Class<?> toType, Extractor extraction) {
        supported.put(new ExtractorKey(type, toType), extraction);
    }

    public void registerType(Class<?> type, Refiner refiner) {
        supportedRefiners.put(type, refiner);
    }

    static class UnsupportedType extends KnuckleBonesException {
        UnsupportedType(Integer type, Class<?> toType) {
            super(String.format(
                    "Type %d converting to %s is not supported. See %s and register an %s in %s",
                    type,
                    toType.getName(),
                    java.sql.Types.class.getName(),
                    Extractor.class.getName(),
                    SupportedTypesRegistered.class.getName()
            ));
        }

        UnsupportedType(Class<?> type) {
            super(String.format(
                    "Class %s is not supported. Register an %s in %s",
                    type.getName(),
                    Refiner.class.getName(),
                    SupportedTypesRegistered.class.getName()
            ));
        }
    }

    private static class ExtractorKey {
        private final Integer sqlType;
        private final Class<?> type;

        private ExtractorKey(Integer sqlType, Class<?> type) {
            this.sqlType = sqlType;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExtractorKey extractorKey = (ExtractorKey) o;
            return Objects.equals(sqlType, extractorKey.sqlType) &&
                    Objects.equals(type, extractorKey.type);
        }

        @Override
        public int hashCode() {

            return Objects.hash(sqlType, type);
        }
    }
}
