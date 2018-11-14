package com.jubalrife.knucklebones.v1.type;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.v1.annotation.Id;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class IntegerToNumericMappingTests extends WithInMemoryDB {

    @Test
    public void givenAllZeroRecordExpectZeroes() {
        IntegerToNumeric id = new IntegerToNumeric();
        id.testCaseName = "All Zeros";

        IntegerToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableIntegerByte, is((byte) 0));
        assertThat(record.nonNullableIntegerShort, is((short) 0));
        assertThat(record.nonNullableIntegerInteger, is((int) 0));
        assertThat(record.nonNullableIntegerLong, is((long) 0));

        assertThat(record.nullableIntegerByte, is((byte) 0));
        assertThat(record.nullableIntegerShort, is((short) 0));
        assertThat(record.nullableIntegerInteger, is((int) 0));
        assertThat(record.nullableIntegerLong, is((long) 0));
    }

    @Test
    public void givenAllNegativeRecordExpectNegatives() {
        IntegerToNumeric id = new IntegerToNumeric();
        id.testCaseName = "All Negative";

        IntegerToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableIntegerByte, is((byte) -1));
        assertThat(record.nonNullableIntegerShort, is((short) -1));
        assertThat(record.nonNullableIntegerInteger, is((int) -1));
        assertThat(record.nonNullableIntegerLong, is((long) -1));

        assertThat(record.nullableIntegerByte, is((byte) -1));
        assertThat(record.nullableIntegerShort, is((short) -1));
        assertThat(record.nullableIntegerInteger, is((int) -1));
        assertThat(record.nullableIntegerLong, is((long) -1));
    }

    @Test
    public void givenNullRecordExpectNulls() {
        IntegerToNumeric id = new IntegerToNumeric();
        id.testCaseName = "Null Record";

        IntegerToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableIntegerByte, is((byte) 0));
        assertThat(record.nonNullableIntegerShort, is((short) 0));
        assertThat(record.nonNullableIntegerInteger, is((int) 0));
        assertThat(record.nonNullableIntegerLong, is((long) 0));

        assertThat(record.nullableIntegerByte, is((Byte) null));
        assertThat(record.nullableIntegerShort, is((Short) null));
        assertThat(record.nullableIntegerInteger, is((Integer) null));
        assertThat(record.nullableIntegerLong, is((Long) null));
    }

    public static class IntegerToNumeric {
        @Id
        public String testCaseName;

        public long nonNullableIntegerLong;
        public int nonNullableIntegerInteger;
        public short nonNullableIntegerShort;
        public byte nonNullableIntegerByte;

        public Long nullableIntegerLong;
        public Integer nullableIntegerInteger;
        public Short nullableIntegerShort;
        public Byte nullableIntegerByte;
    }
}
