CREATE TABLE TinyIntToNumeric(
  TestCaseName VARCHAR(500) PRIMARY KEY,

  NonNullableTinyIntLong  TINYINT NOT NULL,
  NonNullableTinyIntInt   TINYINT NOT NULL,
  NonNullableTinyIntShort TINYINT NOT NULL,
  NonNullableTinyIntByte  TINYINT NOT NULL,
  NullableTinyIntLong     TINYINT,
  NullableTinyIntInt      TINYINT,
  NullableTinyIntShort    TINYINT,
  NullableTinyIntByte     TINYINT,
)