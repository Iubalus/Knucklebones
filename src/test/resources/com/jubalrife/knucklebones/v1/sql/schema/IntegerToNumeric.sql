CREATE TABLE IntegerToNumeric(
   TestCaseName VARCHAR(500) PRIMARY KEY,

   NonNullableIntegerLong     INTEGER NOT NULL,
   NonNullableIntegerInteger  INTEGER NOT NULL,
   NonNullableIntegerShort    INTEGER NOT NULL,
   NonNullableIntegerByte     INTEGER NOT NULL,
   NullableIntegerLong        INTEGER,
   NullableIntegerInteger     INTEGER,
   NullableIntegerShort       INTEGER,
   NullableIntegerByte        INTEGER
)