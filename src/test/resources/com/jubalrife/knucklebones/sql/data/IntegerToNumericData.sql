INSERT INTO IntegerToNumeric(
  TestCaseName,
  NonNullableIntegerLong,
  NonNullableIntegerInteger,
  NonNullableIntegerShort,
  NonNullableIntegerByte,
  NullableIntegerLong,
  NullableIntegerInteger,
  NullableIntegerShort,
  NullableIntegerByte
)
VALUES ('All Zeros',0,0,0,0,0,0,0,0);

INSERT INTO IntegerToNumeric(
  TestCaseName,
  NonNullableIntegerLong,
  NonNullableIntegerInteger,
  NonNullableIntegerShort,
  NonNullableIntegerByte,
  NullableIntegerLong,
  NullableIntegerInteger,
  NullableIntegerShort,
  NullableIntegerByte
)
VALUES ('Null Record',0,0,0,0,NULL,NULL,NULL,NULL);

INSERT INTO IntegerToNumeric(
  TestCaseName,
  NonNullableIntegerLong,
  NonNullableIntegerInteger,
  NonNullableIntegerShort,
  NonNullableIntegerByte,
  NullableIntegerLong,
  NullableIntegerInteger,
  NullableIntegerShort,
  NullableIntegerByte
)
VALUES ('All Negative',-1,-1,-1,-1,-1,-1,-1,-1);