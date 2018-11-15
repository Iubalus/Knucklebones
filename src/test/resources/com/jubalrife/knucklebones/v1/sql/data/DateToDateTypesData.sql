INSERT INTO DateToDateTypes(
  TestCaseName,
  DateToUtilDate,
  DateToSQLDate,
  DateToTimestamp,
  TimestampToSQLDate,
  TimestampToUtilDate,
  TimestampToTimestamp
) VALUES ('Null Case',NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO DateToDateTypes(
  TestCaseName,
  DateToUtilDate,
  DateToSQLDate,
  DateToTimestamp,
  TimestampToSQLDate,
  TimestampToUtilDate,
  TimestampToTimestamp
) VALUES
('Dates With Values','2018-11-14','2018-11-14','2018-11-14','2018-11-14 12:31:32','2018-11-14 12:31:32','2018-11-14 12:31:32')