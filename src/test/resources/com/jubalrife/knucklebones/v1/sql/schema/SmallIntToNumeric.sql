CREATE TABLE SmallIntToNumeric(
  TestCaseName VARCHAR(500) PRIMARY KEY,

  NonNullableSmallIntLong  SMALLINT NOT NULL,
  NonNullableSmallIntInt   SMALLINT NOT NULL,
  NonNullableSmallIntShort SMALLINT NOT NULL,
  NonNullableSmallIntByte  SMALLINT NOT NULL,
  NullableSmallIntLong     SMALLINT,
  NullableSmallIntInt      SMALLINT,
  NullableSmallIntShort    SMALLINT,
  NullableSmallIntByte     SMALLINT,
)