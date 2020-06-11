CREATE TABLE Binary(
  TestCaseName VARCHAR(500) PRIMARY KEY,

  NonNullableBinary      BINARY NOT NULL,
  NonNullableVarBinary   VARBINARY NOT NULL,
  NullableBinary         BINARY,
  NullableVarBinary      VARBINARY
)