INSERT INTO NumericToBoolean(
  TestCaseName,
  NullableBigInt,
  NonNullableBigInt,
  NullableInt,
  NonNullableInt,
  NullableSmallInt,
  NonNullableSmallInt,
  NullableTinyInt,
  NonNullableTinyInt,
  NullableBit,
  NonNullableBit
)
VALUES ('All False',0,0,0,0,0,0,0,0,0,0);

INSERT INTO NumericToBoolean(
  TestCaseName,
  NullableBigInt,
  NonNullableBigInt,
  NullableInt,
  NonNullableInt,
  NullableSmallInt,
  NonNullableSmallInt,
  NullableTinyInt,
  NonNullableTinyInt,
  NullableBit,
  NonNullableBit
)
VALUES ('All True',1,1,1,1,1,1,1,1,1,1);

INSERT INTO NumericToBoolean(
  TestCaseName,
  NullableBigInt,
  NonNullableBigInt,
  NullableInt,
  NonNullableInt,
  NullableSmallInt,
  NonNullableSmallInt,
  NullableTinyInt,
  NonNullableTinyInt,
  NullableBit,
  NonNullableBit
)
VALUES ('True And Null',NULL,1,NULL,1,NULL,1,NULL,1,NULL,1);