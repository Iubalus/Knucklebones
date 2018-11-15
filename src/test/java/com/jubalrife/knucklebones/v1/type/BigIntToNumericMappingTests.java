package com.jubalrife.knucklebones.v1.type;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.v1.annotation.Id;
import com.jubalrife.knucklebones.v1.annotation.Table;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BigIntToNumericMappingTests extends WithInMemoryDB {
    @Test
    public void expectZeroRowToBeZeroed() {
        BigIntToNumeric id = new BigIntToNumeric();
        id.TestCaseName = "All Zeros";

        BigIntToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableBigIntByte, is((byte) 0));
        assertThat(record.nonNullableBigIntShort, is((short) 0));
        assertThat(record.nonNullableBigIntInt, is((int) 0));
        assertThat(record.nonNullableBigIntLong, is((long) 0));

        assertThat(record.nullableBigIntByte, is((byte) 0));
        assertThat(record.nullableBigIntShort, is((short) 0));
        assertThat(record.nullableBigIntInt, is((int) 0));
        assertThat(record.nullableBigIntLong, is((long) 0));
    }

    @Test
    public void expectNegativeOneRowToBeNegative() {
        BigIntToNumeric id = new BigIntToNumeric();
        id.TestCaseName = "All Negative";

        BigIntToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableBigIntByte, is((byte) -1));
        assertThat(record.nonNullableBigIntShort, is((short) -1));
        assertThat(record.nonNullableBigIntInt, is((int) -1));
        assertThat(record.nonNullableBigIntLong, is((long) -1));

        assertThat(record.nullableBigIntByte, is((byte) -1));
        assertThat(record.nullableBigIntShort, is((short) -1));
        assertThat(record.nullableBigIntInt, is((int) -1));
        assertThat(record.nullableBigIntLong, is((long) -1));
    }

    @Test
    public void expectNullRowToBeNulled() {
        BigIntToNumeric id = new BigIntToNumeric();
        id.TestCaseName = "Null Row";

        BigIntToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableBigIntByte, is((byte) 0));
        assertThat(record.nonNullableBigIntShort, is((short) 0));
        assertThat(record.nonNullableBigIntInt, is((int) 0));
        assertThat(record.nonNullableBigIntLong, is((long) 0));

        assertThat(record.nullableBigIntByte, is((Byte) null));
        assertThat(record.nullableBigIntShort, is((Short) null));
        assertThat(record.nullableBigIntInt, is((Integer) null));
        assertThat(record.nullableBigIntLong, is((Long) null));
    }

    @Test
    public void givenInsertedRecordExpectedResultsSameAsOriginalValues() {
        BigIntToNumeric id = new BigIntToNumeric();
        id.TestCaseName = "Inserted";
        id.nonNullableBigIntByte = 1;
        id.nonNullableBigIntInt = 2;
        id.nonNullableBigIntLong = 3;
        id.nonNullableBigIntShort = 4;
        id.nullableBigIntByte = 5;
        id.nullableBigIntInt = 6;
        id.nullableBigIntShort = 7;
        id.nullableBigIntLong = 8L;


        getPersistence().insert(id);
        BigIntToNumeric record = getPersistence().find(id);

        assertThat(record.nonNullableBigIntByte, is(id.nonNullableBigIntByte));
        assertThat(record.nonNullableBigIntShort, is(id.nonNullableBigIntShort));
        assertThat(record.nonNullableBigIntInt, is(id.nonNullableBigIntInt));
        assertThat(record.nonNullableBigIntLong, is(id.nonNullableBigIntLong));

        assertThat(record.nullableBigIntByte, is(id.nullableBigIntByte));
        assertThat(record.nullableBigIntShort, is(id.nullableBigIntShort));
        assertThat(record.nullableBigIntInt, is(id.nullableBigIntInt));
        assertThat(record.nullableBigIntLong, is(id.nullableBigIntLong));
    }

    @Table(name = "BigIntToNumeric")
    public static class BigIntToNumeric {
        @Id
        public String TestCaseName;

        public long nonNullableBigIntLong;
        public int nonNullableBigIntInt;
        public short nonNullableBigIntShort;
        public byte nonNullableBigIntByte;

        public Long nullableBigIntLong;
        public Integer nullableBigIntInt;
        public Short nullableBigIntShort;
        public Byte nullableBigIntByte;
    }
}
