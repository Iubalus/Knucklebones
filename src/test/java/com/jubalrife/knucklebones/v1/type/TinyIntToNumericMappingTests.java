package com.jubalrife.knucklebones.v1.type;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.v1.annotation.Id;
import com.jubalrife.knucklebones.v1.annotation.Table;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TinyIntToNumericMappingTests extends WithInMemoryDB {
    @Test
    public void expectZeroRowToBeZeroed() {
        TinyIntToNumeric id = new TinyIntToNumeric();
        id.TestCaseName = "All Zeros";

        TinyIntToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableTinyIntByte, is((byte) 0));
        assertThat(record.nonNullableTinyIntShort, is((short) 0));
        assertThat(record.nonNullableTinyIntInt, is((int) 0));
        assertThat(record.nonNullableTinyIntLong, is((long) 0));

        assertThat(record.nullableTinyIntByte, is((byte) 0));
        assertThat(record.nullableTinyIntShort, is((short) 0));
        assertThat(record.nullableTinyIntInt, is((int) 0));
        assertThat(record.nullableTinyIntLong, is((long) 0));
    }

    @Test
    public void expectNegativeOneRowToBeNegative() {
        TinyIntToNumeric id = new TinyIntToNumeric();
        id.TestCaseName = "All Negative";

        TinyIntToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableTinyIntByte, is((byte) -1));
        assertThat(record.nonNullableTinyIntShort, is((short) -1));
        assertThat(record.nonNullableTinyIntInt, is((int) -1));
        assertThat(record.nonNullableTinyIntLong, is((long) -1));

        assertThat(record.nullableTinyIntByte, is((byte) -1));
        assertThat(record.nullableTinyIntShort, is((short) -1));
        assertThat(record.nullableTinyIntInt, is((int) -1));
        assertThat(record.nullableTinyIntLong, is((long) -1));
    }

    @Test
    public void expectNullRowToBeNulled() {
        TinyIntToNumeric id = new TinyIntToNumeric();
        id.TestCaseName = "Null Row";

        TinyIntToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableTinyIntByte, is((byte) 0));
        assertThat(record.nonNullableTinyIntShort, is((short) 0));
        assertThat(record.nonNullableTinyIntInt, is((int) 0));
        assertThat(record.nonNullableTinyIntLong, is((long) 0));

        assertThat(record.nullableTinyIntByte, is((Byte) null));
        assertThat(record.nullableTinyIntShort, is((Short) null));
        assertThat(record.nullableTinyIntInt, is((Integer) null));
        assertThat(record.nullableTinyIntLong, is((Long) null));
    }

    @Table(name = "TinyIntToNumeric")
    public static class TinyIntToNumeric {
        @Id
        public String TestCaseName;

        public long nonNullableTinyIntLong;
        public int nonNullableTinyIntInt;
        public short nonNullableTinyIntShort;
        public byte nonNullableTinyIntByte;

        public Long nullableTinyIntLong;
        public Integer nullableTinyIntInt;
        public Short nullableTinyIntShort;
        public Byte nullableTinyIntByte;
    }
}
