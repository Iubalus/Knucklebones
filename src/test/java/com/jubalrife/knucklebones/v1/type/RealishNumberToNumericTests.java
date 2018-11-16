package com.jubalrife.knucklebones.v1.type;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.v1.annotation.Id;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RealishNumberToNumericTests extends WithInMemoryDB {

    @Test
    public void givenAllZeroesExpectZeroes() {
        RealishNumberToNumeric id = new RealishNumberToNumeric();
        id.testCaseName = "Zeroes";

        RealishNumberToNumeric item = getPersistence().find(id);

        assertThat(item.nonNullableRealToFloat, is(0F));
        assertThat(item.nonNullableFloatToFloat, is(0F));
        assertThat(item.nonNullableDecimalToFloat, is(0F));
        assertThat(item.nonNullableRealToDouble, is(0D));
        assertThat(item.nonNullableFloatToDouble, is(0D));
        assertThat(item.nonNullableDecimalToDouble, is(0D));
        assertThat(item.nullableRealToFloat, is(0F));
        assertThat(item.nullableFloatToFloat, is(0F));
        assertThat(item.nullableDecimalToFloat, is(0F));
        assertThat(item.nullableRealToDouble, is(0D));
        assertThat(item.nullableFloatToDouble, is(0D));
        assertThat(item.nullableDecimalToDouble, is(0D));
        assertThat(item.nullableRealBigDecimal.setScale(0), is(BigDecimal.ZERO));
        assertThat(item.nullableFloatBigDecimal.setScale(0), is(BigDecimal.ZERO));
        assertThat(item.nullableDecimalBigDecimal.setScale(0), is(BigDecimal.ZERO));
    }

    @Test
    public void givenNullsAndZeroesExpectZeroes() {
        RealishNumberToNumeric id = new RealishNumberToNumeric();
        id.testCaseName = "Nulls And Zeros";

        RealishNumberToNumeric item = getPersistence().find(id);

        assertThat(item.nonNullableRealToFloat, is(0F));
        assertThat(item.nonNullableFloatToFloat, is(0F));
        assertThat(item.nonNullableDecimalToFloat, is(0F));
        assertThat(item.nonNullableRealToDouble, is(0D));
        assertThat(item.nonNullableFloatToDouble, is(0D));
        assertThat(item.nonNullableDecimalToDouble, is(0D));

        assertThat(item.nullableRealToFloat, nullValue());
        assertThat(item.nullableFloatToFloat, nullValue());
        assertThat(item.nullableDecimalToFloat, nullValue());
        assertThat(item.nullableRealToDouble, nullValue());
        assertThat(item.nullableFloatToDouble, nullValue());
        assertThat(item.nullableDecimalToDouble, nullValue());

        assertThat(item.nullableRealBigDecimal, nullValue());
        assertThat(item.nullableFloatBigDecimal, nullValue());
        assertThat(item.nullableDecimalBigDecimal, nullValue());
    }

    @Test
    public void givenInsertedZerosExpectZeroes() {
        RealishNumberToNumeric id = new RealishNumberToNumeric();
        id.testCaseName = "Inserted";
        id.nonNullableRealToFloat = 1;
        id.nonNullableFloatToFloat = 1;
        id.nonNullableDecimalToFloat = 1;
        id.nonNullableRealToDouble = 1;
        id.nonNullableFloatToDouble = 1;
        id.nonNullableDecimalToDouble = 1;
        id.nullableRealToFloat = 1F;
        id.nullableFloatToFloat = 1F;
        id.nullableDecimalToFloat = 1F;
        id.nullableRealToDouble = 1D;
        id.nullableFloatToDouble = 1D;
        id.nullableDecimalToDouble = 1D;
        id.nullableRealBigDecimal = BigDecimal.ONE;
        id.nullableFloatBigDecimal = BigDecimal.ONE;
        id.nullableDecimalBigDecimal = BigDecimal.ONE;

        getPersistence().insert(id);
        RealishNumberToNumeric item = getPersistence().find(id);

        assertThat(item.nonNullableRealToFloat, is(1F));
        assertThat(item.nonNullableFloatToFloat, is(1F));
        assertThat(item.nonNullableDecimalToFloat, is(1F));
        assertThat(item.nonNullableRealToDouble, is(1D));
        assertThat(item.nonNullableFloatToDouble, is(1D));
        assertThat(item.nonNullableDecimalToDouble, is(1D));
        assertThat(item.nullableRealToFloat, is(1F));
        assertThat(item.nullableFloatToFloat, is(1F));
        assertThat(item.nullableDecimalToFloat, is(1F));
        assertThat(item.nullableRealToDouble, is(1D));
        assertThat(item.nullableFloatToDouble, is(1D));
        assertThat(item.nullableDecimalToDouble, is(1D));
        assertThat(item.nullableRealBigDecimal.setScale(0), is(BigDecimal.ONE));
        assertThat(item.nullableFloatBigDecimal.setScale(0), is(BigDecimal.ONE));
        assertThat(item.nullableDecimalBigDecimal.setScale(0), is(BigDecimal.ONE));
    }

    public static class RealishNumberToNumeric {
        @Id
        public String testCaseName;
        public float nonNullableRealToFloat;
        public float nonNullableFloatToFloat;
        public float nonNullableDecimalToFloat;
        public double nonNullableRealToDouble;
        public double nonNullableFloatToDouble;
        public double nonNullableDecimalToDouble;
        public Float nullableRealToFloat;
        public Float nullableFloatToFloat;
        public Float nullableDecimalToFloat;
        public Double nullableRealToDouble;
        public Double nullableFloatToDouble;
        public Double nullableDecimalToDouble;
        public BigDecimal nullableRealBigDecimal;
        public BigDecimal nullableFloatBigDecimal;
        public BigDecimal nullableDecimalBigDecimal;
    }
}
