package com.jubalrife.knucklebones.type;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.annotation.Id;
import com.jubalrife.knucklebones.annotation.Table;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BooleanMappingTests extends WithInMemoryDB {

    @Test
    public void allFalseRecordIsFalse() {
        NumericToBoolean id = new NumericToBoolean();
        id.testCaseName = "All False";
        NumericToBoolean numericToBoolean = getPersistence().find(id);

        assertFalse(numericToBoolean.nonNullableBigInt);
        assertFalse(numericToBoolean.nonNullableInt);
        assertFalse(numericToBoolean.nonNullableSmallInt);
        assertFalse(numericToBoolean.nonNullableTinyInt);
        assertFalse(numericToBoolean.nonNullableBit);

        assertFalse(numericToBoolean.nullableBigInt);
        assertFalse(numericToBoolean.nullableInt);
        assertFalse(numericToBoolean.nullableSmallInt);
        assertFalse(numericToBoolean.nullableTinyInt);
        assertFalse(numericToBoolean.nullableBit);
    }

    @Test
    public void allTrueRecordIsTrue() {
        NumericToBoolean id = new NumericToBoolean();
        id.testCaseName = "All True";
        NumericToBoolean numericToBoolean = getPersistence().find(id);

        assertTrue(numericToBoolean.nonNullableBigInt);
        assertTrue(numericToBoolean.nonNullableInt);
        assertTrue(numericToBoolean.nonNullableSmallInt);
        assertTrue(numericToBoolean.nonNullableTinyInt);
        assertTrue(numericToBoolean.nonNullableBit);

        assertTrue(numericToBoolean.nullableBigInt);
        assertTrue(numericToBoolean.nullableInt);
        assertTrue(numericToBoolean.nullableSmallInt);
        assertTrue(numericToBoolean.nullableTinyInt);
        assertTrue(numericToBoolean.nullableBit);
    }

    @Test
    public void allTrueAndNullRecords() {
        NumericToBoolean id = new NumericToBoolean();
        id.testCaseName = "True And Null";
        NumericToBoolean numericToBoolean = getPersistence().find(id);

        assertTrue(numericToBoolean.nonNullableBigInt);
        assertTrue(numericToBoolean.nonNullableInt);
        assertTrue(numericToBoolean.nonNullableSmallInt);
        assertTrue(numericToBoolean.nonNullableTinyInt);
        assertTrue(numericToBoolean.nonNullableBit);

        assertNull(numericToBoolean.nullableBigInt);
        assertNull(numericToBoolean.nullableInt);
        assertNull(numericToBoolean.nullableSmallInt);
        assertNull(numericToBoolean.nullableTinyInt);
        assertNull(numericToBoolean.nullableBit);
    }

    @Table(name = "NumericToBoolean")
    public static class NumericToBoolean {
        @Id
        public String testCaseName;

        public boolean nonNullableBigInt;
        public boolean nonNullableInt;
        public boolean nonNullableSmallInt;
        public boolean nonNullableTinyInt;
        public boolean nonNullableBit;

        public Boolean nullableBigInt;
        public Boolean nullableInt;
        public Boolean nullableSmallInt;
        public Boolean nullableTinyInt;
        public Boolean nullableBit;
    }
}
