CREATE TABLE RealishNumberToNumeric(
  TestCaseName                 VARCHAR(500) PRIMARY KEY,
  NonNullableRealToFloat       REAL NOT NULL,
  NonNullableRealToDouble      REAL NOT NULL,
  NonNullableFloatToFloat      DOUBLE NOT NULL,
  NonNullableFloatToDouble     DOUBLE NOT NULL,
  NonNullableDecimalToFloat    DECIMAL(5,2) NOT NULL,
  NonNullableDecimalToDouble   DECIMAL(5,2) NOT NULL,
  NullableRealToFloat          REAL,
  NullableRealToDouble         REAL,
  NullableRealBigDecimal       REAL,
  NullableFloatToFloat         DOUBLE,
  NullableFloatToDouble        DOUBLE,
  NullableFloatBigDecimal      DOUBLE,
  NullableDecimalToFloat       DECIMAL,
  NullableDecimalToDouble      DECIMAL,
  NullableDecimalBigDecimal    DECIMAL
)