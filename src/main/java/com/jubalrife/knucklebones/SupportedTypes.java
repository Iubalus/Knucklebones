package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.exception.KnuckleBonesException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class SupportedTypes {

    private static Map<Integer, Extractor> supported = new HashMap<>();

    static {
        registerType(Types.INTEGER, (columnIndex, results) -> results.getInt(columnIndex));
        registerType(Types.VARCHAR, ((columnIndex, results) -> results.getString(columnIndex)));
    }

    public static Extractor getExtractor(Integer type) {
        Extractor extractor = supported.get(type);
        if (extractor == null) {
            throw new UnsupportedType(type);
        }
        return extractor;
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

    interface Extractor {
        Object extract(Integer columnIndex, ResultSet results) throws SQLException;
    }

}
