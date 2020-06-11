package com.jubalrife.knucklebones.v1.type;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.v1.annotation.Id;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class BinaryMappingTests extends WithInMemoryDB {
    @Test
    public void insertAndRead() {
        Binary record = new Binary();
        record.testCaseName = "TestInsertAndFind";
        record.nonNullableBinary = "Non Nullable Binary".getBytes();
        record.nonNullableVarBinary = "Non Nullable Var Binary".getBytes();
        record.nullableBinary = "Nullable Binary".getBytes();
        record.nullableVarBinary = "Nullable Var Binary".getBytes();

        getPersistence().insert(record);
        Binary r = new Binary();
        r.testCaseName = record.testCaseName;
        getPersistence().find(r);
        assertThat(new String(r.nonNullableBinary), is("Non Nullable Binary"));
        assertThat(new String(r.nonNullableVarBinary), is("Non Nullable Var Binary"));
        assertThat(new String(r.nullableBinary), is("Nullable Binary"));
        assertThat(new String(r.nullableVarBinary), is("Nullable Var Binary"));
    }

    @Test
    public void testInsertNulls() {
        Binary record = new Binary();
        record.testCaseName = "TestInsertAndFind";
        record.nonNullableBinary = "Non Nullable Binary".getBytes();
        record.nonNullableVarBinary = "Non Nullable Var Binary".getBytes();

        getPersistence().insert(record);

        Binary r = new Binary();
        r.testCaseName = record.testCaseName;
        getPersistence().find(r);
        assertThat(new String(r.nonNullableBinary), is("Non Nullable Binary"));
        assertThat(new String(r.nonNullableVarBinary), is("Non Nullable Var Binary"));
        assertNull(r.nullableBinary);
        assertNull(r.nullableVarBinary);
    }

    public static class Binary {
        @Id
        public String testCaseName;
        public byte[] nonNullableBinary;
        public byte[] nonNullableVarBinary;
        public byte[] nullableBinary;
        public byte[] nullableVarBinary;
    }
}
