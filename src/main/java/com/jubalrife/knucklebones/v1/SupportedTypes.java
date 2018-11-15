package com.jubalrife.knucklebones.v1;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * {@link SupportedTypesRegistered} contains the capability to interpret types from the database by coercing and transforming them into java types
 * <p>
 * This transformation is done by {@link Extractor}s and {@link Refiner}s both of which can be registered with {@link SupportedTypes}
 */
public interface SupportedTypes {

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
    void registerType(Integer type, Class<?> toType, Extractor extraction);

    /**
     * This method exists to allow for customizable coercion of data and additional implementation of types which may not have been implemented
     * the base system. For instance, if special care must be taken when coercing a date type into a java form, this is where that logic will live.
     * <br>
     * Existing type definitions can be overwritten by registering the same type multiple times.
     *
     * @param type    is Class which this {@link Refiner} applies to
     * @param refiner is a {@link Refiner} which is responsible for refining the provided value into the {@link java.sql.PreparedStatement} at the provided index
     */
    void registerType(Class<?> type, Refiner refiner);

    /**
     * An {@link Extractor} describes how to extractor describes how to extract a value from a {@link ResultSet} of a given type.
     */
    interface Extractor {
        /**
         * @param columnIndex should be assumed as whatever column the desired value will be extracted from. This should for the most part be delegated to the provided {@link ResultSet} in some way
         * @param results     is the {@link ResultSet} from which the data should be extracted
         * @return an {@link Object} extracted from the provided {@link ResultSet} at the provided columnIndex.
         * @throws SQLException generally caused by delegation to the provided {@link ResultSet}
         */
        Object extract(Integer columnIndex, ResultSet results) throws SQLException;
    }

    interface Refiner {
        void refine(Integer parameterNumber, Object value, PreparedStatement statement) throws SQLException;
    }
}
