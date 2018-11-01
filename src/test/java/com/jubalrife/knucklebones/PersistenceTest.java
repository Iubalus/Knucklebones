package com.jubalrife.knucklebones;

import com.WithInMemoryDB;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PersistenceTest extends WithInMemoryDB {
    @Test
    public void canFillDAOWithSimpleValues() throws Exception {
        getConnection().prepareStatement("CREATE TABLE TableA ( ColumnA INT, ColumnB VARCHAR(50) )").executeUpdate();
        getConnection()
                .prepareStatement("INSERT INTO TableA (ColumnA, ColumnB) VALUES(1, 'Hello World')")
                .executeUpdate();
        List<TableADAO> found = new Persistence(getConnection()).find("SELECT * FROM TableA", TableADAO.class);
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(1));
        assertThat(found.get(0).columnB, is("Hello World"));
    }

    @Test
    public void canFindWithSimpleParmetersDAOWithSimpleValues() throws Exception {
        getConnection().prepareStatement("CREATE TABLE TableB ( ColumnA INT, ColumnB VARCHAR(50) )").executeUpdate();
        getConnection()
                .prepareStatement("INSERT INTO TableB (ColumnA, ColumnB) VALUES(1, 'Hello World')")
                .executeUpdate();
        getConnection()
                .prepareStatement("INSERT INTO TableB (ColumnA, ColumnB) VALUES(2, 'Good Bye, Moon')")
                .executeUpdate();

        List<TableADAO> found = new Persistence(getConnection()).setParameter("a", 1).find("SELECT * FROM TableB WHERE ColumnA = :a ", TableADAO.class);
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(1));
        assertThat(found.get(0).columnB, is("Hello World"));

        found = new Persistence(getConnection()).setParameter("a", "Good Bye, Moon").find("SELECT * FROM TableB WHERE ColumnB = :a ", TableADAO.class);
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(2));
        assertThat(found.get(0).columnB, is("Good Bye, Moon"));
    }


    public static class TableADAO {
        public Integer columnA;
        public String columnB;
    }
}