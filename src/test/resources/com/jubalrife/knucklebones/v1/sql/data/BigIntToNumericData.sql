INSERT INTO BigIntToNumeric(
  TestCaseName,
  NonNullableBigIntLong,
  NonNullableBigIntInt,
  NonNullableBigIntShort,
  NonNullableBigIntByte,
  NullableBigIntLong,
  NullableBigIntInt,
  NullableBigIntShort,
  NullableBigIntByte
)
VALUES ('All Zeros',0,0,0,0,0,0,0,0);

INSERT INTO BigIntToNumeric(
  TestCaseName,
  NonNullableBigIntLong,
  NonNullableBigIntInt,
  NonNullableBigIntShort,
  NonNullableBigIntByte,
  NullableBigIntLong,
  NullableBigIntInt,
  NullableBigIntShort,
  NullableBigIntByte
)
VALUES ('All Negative',-1,-1,-1,-1,-1,-1,-1,-1);

INSERT INTO BigIntToNumeric(
  TestCaseName,
  NonNullableBigIntLong,
  NonNullableBigIntInt,
  NonNullableBigIntShort,
  NonNullableBigIntByte,
  NullableBigIntLong,
  NullableBigIntInt,
  NullableBigIntShort,
  NullableBigIntByte
)
VALUES ('Null Row',0,0,0,0,NULL,NULL,NULL,NULL);