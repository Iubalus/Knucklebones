INSERT INTO TinyIntToNumeric(
  TestCaseName,
  NonNullableTinyIntLong,
  NonNullableTinyIntInt,
  NonNullableTinyIntShort,
  NonNullableTinyIntByte,
  NullableTinyIntLong,
  NullableTinyIntInt,
  NullableTinyIntShort,
  NullableTinyIntByte
)
VALUES ('All Zeros',0,0,0,0,0,0,0,0);

INSERT INTO TinyIntToNumeric(
  TestCaseName,
  NonNullableTinyIntLong,
  NonNullableTinyIntInt,
  NonNullableTinyIntShort,
  NonNullableTinyIntByte,
  NullableTinyIntLong,
  NullableTinyIntInt,
  NullableTinyIntShort,
  NullableTinyIntByte
)
VALUES ('All Negative',-1,-1,-1,-1,-1,-1,-1,-1);

INSERT INTO TinyIntToNumeric(
  TestCaseName,
  NonNullableTinyIntLong,
  NonNullableTinyIntInt,
  NonNullableTinyIntShort,
  NonNullableTinyIntByte,
  NullableTinyIntLong,
  NullableTinyIntInt,
  NullableTinyIntShort,
  NullableTinyIntByte
)
VALUES ('Null Row',0,0,0,0,NULL,NULL,NULL,NULL);