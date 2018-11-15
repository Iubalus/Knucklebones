CREATE TABLE DateToDateTypes(
  TestCaseName        VARCHAR(500) PRIMARY KEY,

  DateToUtilDate       DATE,
  DateToSQLDate        DATE,
  DateToTimestamp      DATE,
  TimestampToSQLDate   TIMESTAMP,
  TimestampToUtilDate  TIMESTAMP,
  TimestampToTimestamp TIMESTAMP,
)