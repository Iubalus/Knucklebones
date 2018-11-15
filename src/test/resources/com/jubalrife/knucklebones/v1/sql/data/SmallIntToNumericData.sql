INSERT INTO SmallIntToNumeric(
  TestCaseName,
  NonNullableSmallIntLong,
  NonNullableSmallIntInt,
  NonNullableSmallIntShort,
  NonNullableSmallIntByte,
  NullableSmallIntLong,
  NullableSmallIntInt,
  NullableSmallIntShort,
  NullableSmallIntByte
)
VALUES ('All Zeros',0,0,0,0,0,0,0,0);

INSERT INTO SmallIntToNumeric(
  TestCaseName,
  NonNullableSmallIntLong,
  NonNullableSmallIntInt,
  NonNullableSmallIntShort,
  NonNullableSmallIntByte,
  NullableSmallIntLong,
  NullableSmallIntInt,
  NullableSmallIntShort,
  NullableSmallIntByte
)
VALUES ('All Negative',-1,-1,-1,-1,-1,-1,-1,-1);

INSERT INTO SmallIntToNumeric(
  TestCaseName,
  NonNullableSmallIntLong,
  NonNullableSmallIntInt,
  NonNullableSmallIntShort,
  NonNullableSmallIntByte,
  NullableSmallIntLong,
  NullableSmallIntInt,
  NullableSmallIntShort,
  NullableSmallIntByte
)
VALUES ('Null Row',0,0,0,0,NULL,NULL,NULL,NULL);