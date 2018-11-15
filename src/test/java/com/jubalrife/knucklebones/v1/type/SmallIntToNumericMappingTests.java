package com.jubalrife.knucklebones.v1.type;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.v1.annotation.Id;
import com.jubalrife.knucklebones.v1.annotation.Table;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SmallIntToNumericMappingTests extends WithInMemoryDB {
    @Test
    public void expectZeroRowToBeZeroed() {
        SmallIntToNumeric id = new SmallIntToNumeric();
        id.TestCaseName = "All Zeros";

        SmallIntToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableSmallIntByte, is((byte) 0));
        assertThat(record.nonNullableSmallIntShort, is((short) 0));
        assertThat(record.nonNullableSmallIntInt, is((int) 0));
        assertThat(record.nonNullableSmallIntLong, is((long) 0));

        assertThat(record.nullableSmallIntByte, is((byte) 0));
        assertThat(record.nullableSmallIntShort, is((short) 0));
        assertThat(record.nullableSmallIntInt, is((int) 0));
        assertThat(record.nullableSmallIntLong, is((long) 0));
    }

    @Test
    public void expectNegativeOneRowToBeNegative() {
        SmallIntToNumeric id = new SmallIntToNumeric();
        id.TestCaseName = "All Negative";

        SmallIntToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableSmallIntByte, is((byte) -1));
        assertThat(record.nonNullableSmallIntShort, is((short) -1));
        assertThat(record.nonNullableSmallIntInt, is((int) -1));
        assertThat(record.nonNullableSmallIntLong, is((long) -1));

        assertThat(record.nullableSmallIntByte, is((byte) -1));
        assertThat(record.nullableSmallIntShort, is((short) -1));
        assertThat(record.nullableSmallIntInt, is((int) -1));
        assertThat(record.nullableSmallIntLong, is((long) -1));
    }

    @Test
    public void expectNullRowToBeNulled() {
        SmallIntToNumeric id = new SmallIntToNumeric();
        id.TestCaseName = "Null Row";

        SmallIntToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableSmallIntByte, is((byte) 0));
        assertThat(record.nonNullableSmallIntShort, is((short) 0));
        assertThat(record.nonNullableSmallIntInt, is((int) 0));
        assertThat(record.nonNullableSmallIntLong, is((long) 0));

        assertThat(record.nullableSmallIntByte, is((Byte) null));
        assertThat(record.nullableSmallIntShort, is((Short) null));
        assertThat(record.nullableSmallIntInt, is((Integer) null));
        assertThat(record.nullableSmallIntLong, is((Long) null));
    }

    @Table(name = "SmallIntToNumeric")
    public static class SmallIntToNumeric {
        @Id
        public String TestCaseName;

        public long nonNullableSmallIntLong;
        public int nonNullableSmallIntInt;
        public short nonNullableSmallIntShort;
        public byte nonNullableSmallIntByte;

        public Long nullableSmallIntLong;
        public Integer nullableSmallIntInt;
        public Short nullableSmallIntShort;
        public Byte nullableSmallIntByte;
    }
}
