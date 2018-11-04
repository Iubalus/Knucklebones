package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.exception.KnuckleBonesException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SupportedTypes {

    private static Map<ExtractorKey, Extractor> supported = new HashMap<>();
    private static Map<Class<?>, Refiner> supportedRefiners = new HashMap<>();

    static {
        registerType(Types.BIGINT, Long.class, (columnIndex, results) -> results.getLong(columnIndex));
        registerType(Types.BIGINT, Long.TYPE, (columnIndex, results) -> results.getLong(columnIndex));
        registerType(Types.BIGINT, Integer.TYPE, (columnIndex, results) -> results.getInt(columnIndex));
        registerType(Types.BIGINT, Integer.class, (columnIndex, results) -> results.getInt(columnIndex));

        registerType(Types.INTEGER, Integer.TYPE, (columnIndex, results) -> results.getInt(columnIndex));
        registerType(Types.INTEGER, Integer.class, (columnIndex, results) -> results.getInt(columnIndex));
        registerType(Types.VARCHAR, String.class, ((columnIndex, results) -> results.getString(columnIndex)));

        registerType(Integer.TYPE, (index, value, statement) -> statement.setInt(index, (int) value));
        registerType(Integer.class, (index, value, statement) -> statement.setObject(index, (Integer) value));
        registerType(String.class, (index, value, statement) -> statement.setString(index, (String) value));
        registerType(null, (index, value, statement) -> statement.setObject(index, null));
    }

    public static Extractor getExtractor(Integer type, Class<?> toType) {
        Extractor extractor = supported.get(new ExtractorKey(type, toType));
        if (extractor == null) {
            throw new UnsupportedType(type, toType);
        }
        return extractor;
    }

    public static Refiner getRefiner(Class<?> type) {
        Refiner refiner = supportedRefiners.get(type);
        if (refiner == null) {
            throw new UnsupportedType(type);
        }
        return refiner;
    }

    static class UnsupportedType extends KnuckleBonesException {
        UnsupportedType(Integer type, Class<?> toType) {
            super(String.format(
                    "Type %d converting to %s is not supported. See %s and register an %s in %s",
                    type,
                    toType.getName(),
                    java.sql.Types.class.getName(),
                    Extractor.class.getName(),
                    SupportedTypes.class.getName()
            ));
        }

        UnsupportedType(Class<?> type) {
            super(String.format(
                    "Class %s is not supported. Register an %s in %s",
                    type.getName(),
                    Refiner.class.getName(),
                    SupportedTypes.class.getName()
            ));
        }

    }

    /**
     * This method exists to allow for customizable coercion of data and additional implementation of types which may not have been implemented
     * the base system. For instance, if special care must be taken when coercing a date type into a java form, this is where that logic will live.
     * <br>
     * Existing type definitions can be overwritten by registering the same type multiple times.
     *
     * @param type       is a {@link Types} which this {@link Extractor} applies to
     * @param toType     is a class representing the value that the type will be converted to
     * @param extraction is an {@link Extractor} which is responsible for extracting the data from the results set as the appropriate type
     */
    public static void registerType(Integer type, Class<?> toType, Extractor extraction) {
        supported.put(new ExtractorKey(type, toType), extraction);
    }

    /**
     * This method exists to allow for customizable coercion of data and additional implementation of types which may not have been implemented
     * the base system. For instance, if special care must be taken when coercing a date type into a java form, this is where that logic will live.
     * <br>
     * Existing type definitions can be overwritten by registering the same type multiple times.
     *
     * @param type    is Class which this {@link Refiner} applies to
     * @param refiner is a {@link Refiner} which is responsible for refining the provided value into the {@link java.sql.PreparedStatement} at the provided index
     */
    public static void registerType(Class<?> type, Refiner refiner) {
        supportedRefiners.put(type, refiner);
    }

    public interface Extractor {
        Object extract(Integer columnIndex, ResultSet results) throws SQLException;
    }

    public interface Refiner {
        void refine(Integer parameterNumber, Object value, PreparedStatement statement) throws SQLException;
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
