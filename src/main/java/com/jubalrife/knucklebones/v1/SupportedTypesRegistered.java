package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;
import com.jubalrife.knucklebones.v1.type.*;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class SupportedTypesRegistered implements SupportedTypes {

    private Map<ExtractorKey, Extractor> supported = new HashMap<>();
    private Map<Class<?>, Refiner> supportedRefiners = new HashMap<>();

    SupportedTypesRegistered() {
        registerType(Types.BIGINT, Boolean.class, new NumericToBoolean());
        registerType(Types.INTEGER, Boolean.class, new NumericToBoolean());
        registerType(Types.SMALLINT, Boolean.class, new NumericToBoolean());
        registerType(Types.TINYINT, Boolean.class, new NumericToBoolean());
        registerType(Types.BIT, Boolean.class, new NumericToBoolean());
        registerType(Types.BOOLEAN, Boolean.class, new NumericToBoolean());
        registerType(Types.BIGINT, Boolean.TYPE, new NumericToBool());
        registerType(Types.INTEGER, Boolean.TYPE, new NumericToBool());
        registerType(Types.SMALLINT, Boolean.TYPE, new NumericToBool());
        registerType(Types.TINYINT, Boolean.TYPE, new NumericToBool());
        registerType(Types.BIT, Boolean.TYPE, new NumericToBool());
        registerType(Types.BOOLEAN, Boolean.TYPE, new NumericToBool());

        registerType(Types.BIGINT, Long.class, new LongExtractor());
        registerType(Types.BIGINT, Long.TYPE, new LongPrimitiveExtractor());
        registerType(Types.BIGINT, Integer.class, new IntegerExtractor());
        registerType(Types.BIGINT, Integer.TYPE, new IntExtractor());
        registerType(Types.BIGINT, Short.class, new ShortExtractor());
        registerType(Types.BIGINT, Short.TYPE, new ShortPrimitiveExtractor());
        registerType(Types.BIGINT, Byte.class, new ByteExtractor());
        registerType(Types.BIGINT, Byte.TYPE, new BytePrimitiveExtractor());

        registerType(Types.INTEGER, Long.class, new LongExtractor());
        registerType(Types.INTEGER, Long.TYPE, new LongPrimitiveExtractor());
        registerType(Types.INTEGER, Integer.class, new IntegerExtractor());
        registerType(Types.INTEGER, Integer.TYPE, new IntExtractor());
        registerType(Types.INTEGER, Short.class, new ShortExtractor());
        registerType(Types.INTEGER, Short.TYPE, new ShortPrimitiveExtractor());
        registerType(Types.INTEGER, Byte.class, new ByteExtractor());
        registerType(Types.INTEGER, Byte.TYPE, new BytePrimitiveExtractor());

        registerType(Types.SMALLINT, Long.class, new LongExtractor());
        registerType(Types.SMALLINT, Long.TYPE, new LongPrimitiveExtractor());
        registerType(Types.SMALLINT, Integer.class, new IntegerExtractor());
        registerType(Types.SMALLINT, Integer.TYPE, new IntExtractor());
        registerType(Types.SMALLINT, Short.class, new ShortExtractor());
        registerType(Types.SMALLINT, Short.TYPE, new ShortPrimitiveExtractor());
        registerType(Types.SMALLINT, Byte.class, new ByteExtractor());
        registerType(Types.SMALLINT, Byte.TYPE, new BytePrimitiveExtractor());

        registerType(Types.TINYINT, Long.class, new LongExtractor());
        registerType(Types.TINYINT, Long.TYPE, new LongPrimitiveExtractor());
        registerType(Types.TINYINT, Integer.class, new IntegerExtractor());
        registerType(Types.TINYINT, Integer.TYPE, new IntExtractor());
        registerType(Types.TINYINT, Short.class, new ShortExtractor());
        registerType(Types.TINYINT, Short.TYPE, new ShortPrimitiveExtractor());
        registerType(Types.TINYINT, Byte.class, new ByteExtractor());
        registerType(Types.TINYINT, Byte.TYPE, new BytePrimitiveExtractor());

        registerType(Types.VARCHAR, String.class, ((columnIndex, results) -> results.getString(columnIndex)));

        registerType(Types.DATE, java.util.Date.class, new JavaUtilDateExtractor());
        registerType(Types.DATE, java.sql.Date.class, new JavaSqlDateExtractor());
        registerType(Types.DATE, java.sql.Timestamp.class, new TimestampExtractor());
        registerType(Types.TIMESTAMP, java.util.Date.class, new JavaUtilDateExtractor());
        registerType(Types.TIMESTAMP, java.sql.Date.class, new JavaSqlDateExtractor());
        registerType(Types.TIMESTAMP, java.sql.Timestamp.class, new TimestampExtractor());

        registerType(String.class, (index, value, statement) -> statement.setString(index, (String) value));

        registerType(Boolean.TYPE, (index, value, statement) -> statement.setBoolean(index, (boolean) value));
        registerType(Boolean.class, (index, value, statement) -> {
            if (value == null) {
                statement.setObject(index, null);
            } else {
                statement.setBoolean(index, (boolean) value);
            }
        });

        registerType(Byte.TYPE, (index, value, statement) -> statement.setInt(index, (byte) value));
        registerType(Byte.class, (index, value, statement) -> statement.setObject(index, value));
        registerType(Short.TYPE, (index, value, statement) -> statement.setInt(index, (short) value));
        registerType(Short.class, (index, value, statement) -> statement.setObject(index, value));
        registerType(Integer.TYPE, (index, value, statement) -> statement.setInt(index, (int) value));
        registerType(Integer.class, (index, value, statement) -> statement.setObject(index, value));
        registerType(Long.TYPE, (index, value, statement) -> statement.setLong(index, (long) value));
        registerType(Long.class, (index, value, statement) -> statement.setObject(index, value));
        registerType(java.util.Date.class, (index, value, statement) -> statement.setTimestamp(index, new Timestamp(((java.util.Date) value).getTime())));
        registerType(java.sql.Date.class, (index, value, statement) -> statement.setDate(index, (java.sql.Date) value));
        registerType(java.sql.Timestamp.class, (index, value, statement) -> statement.setTimestamp(index, (java.sql.Timestamp) value));
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
