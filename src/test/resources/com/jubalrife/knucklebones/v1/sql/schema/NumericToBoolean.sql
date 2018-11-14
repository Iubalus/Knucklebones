CREATE TABLE NumericToBoolean(
  TestCaseName VARCHAR(500) PRIMARY KEY,

  NullableBigInt BIGINT,
  NonNullableBigInt BIGINT NOT NULL,

  NullableInt INT,
  NonNullableInt INT NOT NULL,

  NullableSmallInt SMALLINT,
  NonNullableSmallInt SMALLINT NOT NULL,

  NullableTinyInt TINYINT,
  NonNullableTinyInt TINYINT NOT NULL,

  NullableBit BIT,
  NonNullableBit BIT NOT NULL
)