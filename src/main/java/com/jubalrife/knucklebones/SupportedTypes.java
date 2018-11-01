package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.exception.KnuckleBonesException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class SupportedTypes {

    private static Map<Integer, Extractor> supported = new HashMap<>();
    private static Map<Class<?>, Refiner> supportedRefiners = new HashMap<>();

    static {
        registerType(Types.INTEGER, (columnIndex, results) -> results.getInt(columnIndex));
        registerType(Types.VARCHAR, ((columnIndex, results) -> results.getString(columnIndex)));

        registerType(Integer.TYPE, (index, value, statement) -> statement.setInt(index, (int) value));
        registerType(Integer.class, (index, value, statement) -> statement.setObject(index, (Integer) value));
        registerType(String.class, (index, value, statement) -> statement.setString(index, (String) value));
        registerType(null, (index, value, statement) -> statement.setObject(index, null));
    }

    public static Extractor getExtractor(Integer type) {
        Extractor extractor = supported.get(type);
        if (extractor == null) {
            throw new UnsupportedType(type);
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
        UnsupportedType(Integer type) {
            super(String.format(
                    "Type %d is not supported. See %s and register an %s in %s",
                    type,
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
     * @param type       is a {@link java.sql.Types} which this {@link Extractor} applies to
     * @param extraction is an {@link Extractor} which is responsible for extracting the data from the results set as the appropriate type
     */
    public static void registerType(Integer type, Extractor extraction) {
        supported.put(type, extraction);
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
}
