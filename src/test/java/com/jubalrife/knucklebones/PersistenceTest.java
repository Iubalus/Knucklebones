package com.jubalrife.knucklebones;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.annotation.GeneratedValue;
import com.jubalrife.knucklebones.annotation.Id;
import com.jubalrife.knucklebones.annotation.Table;
import jdk.nashorn.internal.parser.DateParser;
import org.junit.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PersistenceTest extends WithInMemoryDB {

    @Test
    public void canFillDAOWithSimpleValues() {
        List<TableADAO> found = new Persistence(getConnection()).createNativeQuery("SELECT * FROM TableA WHERE ColumnA = 1", TableADAO.class).findResults();
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(1));
        assertThat(found.get(0).columnB, is("Hello World"));
    }

    @Test
    public void canFindWithSimpleParametersDAOWithSimpleValues() {
        List<TableADAO> found = new Persistence(getConnection()).createNativeQuery("SELECT * FROM TableA WHERE ColumnA = :a ", TableADAO.class).setParameter("a", 1).findResults();
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(1));
        assertThat(found.get(0).columnB, is("Hello World"));

        found = new Persistence(getConnection()).createNativeQuery("SELECT * FROM TableA WHERE ColumnB = :a ", TableADAO.class).setParameter("a", "Good Bye, Moon").findResults();
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(2));
        assertThat(found.get(0).columnB, is("Good Bye, Moon"));
    }

    @Test
    public void insertedItemCanBeFound() {
        TableADAO o = new TableADAO();
        o.columnA = 3;
        o.columnB = "Text";

        Persistence persistence = new Persistence(getConnection());
        persistence.insert(o);

        List<TableADAO> found = persistence.createNativeQuery("SELECT * FROM TableA WHERE ColumnA = 3", TableADAO.class).findResults();
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(3));
        assertThat(found.get(0).columnB, is("Text"));
    }

    @Test
    public void insertedItemHonorsGeneratedValues() {
        GeneratedColumns o = new GeneratedColumns();
        Persistence persistence = new Persistence(getConnection());
        o = persistence.insert(o);

        assertThat(o.id, is(1));
        assertThat(o.defaultValue, is(5));
    }

    @Test
    public void testFindBasedOnIdValueOnId() {
        TableAWithId dao = new TableAWithId();
        dao.columnA = 1;

        Persistence persistence = new Persistence(getConnection());
        dao = persistence.find(dao);

        assertThat(dao.columnA, is(1));
        assertThat(dao.columnB, is("Hello World"));
    }

    @Test
    public void testDateInsertAndRetieval() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Term term = new Term();
        term.startDate = format.parse("2018-10-01");
        term.endDate = format.parse("2018-12-15");

        Persistence persistence = new Persistence(getConnection());
        Term inserted = persistence.insert(term);


        Term found = persistence.find(inserted);
        assertNotNull(found.termId);
        assertThat(term.startDate, is(found.startDate));
        assertThat(term.endDate, is(found.endDate));
    }

    @Table(name = "TableA")
    public static class TableADAO {
        public Integer columnA;
        public String columnB;
    }

    @Table(name = "TableA")
    public static class TableAWithId {
        @Id
        public Integer columnA;
        public String columnB;
    }

    public static class GeneratedColumns {
        @Id
        @GeneratedValue
        public Integer id;
        @GeneratedValue
        public Integer defaultValue;
    }

    public static class Term {
        @Id
        @GeneratedValue
        public Integer termId;

        public Date startDate;

        public Date endDate;
    }
}