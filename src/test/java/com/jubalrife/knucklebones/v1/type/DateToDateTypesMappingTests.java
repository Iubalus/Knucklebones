package com.jubalrife.knucklebones.v1.type;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.v1.annotation.Id;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DateToDateTypesMappingTests extends WithInMemoryDB {

    @Test
    public void givenNullDatesExpectNullValuesToBeMapped() {
        DateToDateTypes id = new DateToDateTypes();
        id.testCaseName = "Null Case";

        DateToDateTypes result = getPersistence().find(id);
        assertThat(result.testCaseName, is("Null Case"));
        assertThat(result.dateToUtilDate, nullValue());
        assertThat(result.dateToSQLDate, nullValue());
        assertThat(result.dateToTimestamp, nullValue());
        assertThat(result.timestampToSQLDate, nullValue());
        assertThat(result.timestampToUtilDate, nullValue());
        assertThat(result.timestampToTimestamp, nullValue());
    }

    @Test
    public void givenDatesWithValuesExpectValuesToBeMapped() throws ParseException {
        DateToDateTypes id = new DateToDateTypes();
        id.testCaseName = "Dates With Values";

        java.util.Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse("2018-11-14");
        java.util.Date expectedDateWithTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-11-14 12:31:32");

        DateToDateTypes result = getPersistence().find(id);
        assertThat(result.testCaseName, is("Dates With Values"));

        assertThat(result.dateToUtilDate, is(expectedDate));
        assertThat(result.dateToSQLDate, is(expectedDate));
        assertThat(result.dateToTimestamp, is(new Timestamp(expectedDate.getTime())));

        assertThat(result.timestampToSQLDate, is(expectedDate));
        assertThat(result.timestampToUtilDate, is(expectedDateWithTime));
        assertThat(result.timestampToTimestamp, is(new Timestamp(expectedDateWithTime.getTime())));
    }

    @Test
    public void givenInsertedRecordDatesExpectValuesToBeUnchangedOnFetch() throws ParseException {
        java.util.Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse("2018-11-14");
        java.util.Date expectedDateWithTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-11-14 12:31:32");

        DateToDateTypes id = new DateToDateTypes();
        id.testCaseName = "Inserted Value";
        id.dateToSQLDate = new java.sql.Date(expectedDate.getTime());
        id.dateToUtilDate = expectedDate;
        id.dateToTimestamp = new Timestamp(expectedDate.getTime());

        id.timestampToSQLDate = new java.sql.Date(expectedDate.getTime());
        id.timestampToUtilDate = expectedDateWithTime;
        id.timestampToTimestamp = new Timestamp(expectedDateWithTime.getTime());


        getPersistence().insert(id);

        DateToDateTypes result = getPersistence().find(id);
        assertThat(result.testCaseName, is(id.testCaseName));

        assertThat(result.dateToUtilDate, is(id.dateToUtilDate));
        assertThat(result.dateToSQLDate, is(id.dateToSQLDate));
        assertThat(result.dateToTimestamp, is(id.dateToTimestamp));

        assertThat(result.timestampToSQLDate.getTime(), is(id.timestampToSQLDate.getTime()));
        assertThat(result.timestampToUtilDate, is(id.timestampToUtilDate));
        assertThat(result.timestampToTimestamp, is(id.timestampToTimestamp));
    }

    public static class DateToDateTypes {
        @Id
        public String testCaseName;

        public java.util.Date dateToUtilDate;
        public java.sql.Date dateToSQLDate;
        public java.sql.Timestamp dateToTimestamp;

        public java.sql.Date timestampToSQLDate;
        public java.util.Date timestampToUtilDate;
        public java.sql.Timestamp timestampToTimestamp;
    }
}
