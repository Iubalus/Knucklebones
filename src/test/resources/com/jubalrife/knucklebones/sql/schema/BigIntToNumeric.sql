CREATE TABLE BigIntToNumeric(
  TestCaseName VARCHAR(500) PRIMARY KEY,

  NonNullableBigIntLong  BIGINT NOT NULL,
  NonNullableBigIntInt   BIGINT NOT NULL,
  NonNullableBigIntShort BIGINT NOT NULL,
  NonNullableBigIntByte  BIGINT NOT NULL,
  NullableBigIntLong     BIGINT,
  NullableBigIntInt      BIGINT,
  NullableBigIntShort    BIGINT,
  NullableBigIntByte     BIGINT,
)