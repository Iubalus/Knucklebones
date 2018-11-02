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
        List<TableADAO> found = new Persistence(getConnection()).find("SELECT * FROM TableA WHERE ColumnA = 1", TableADAO.class);
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(1));
        assertThat(found.get(0).columnB, is("Hello World"));
    }

    @Test
    public void canFindWithSimpleParmetersDAOWithSimpleValues() throws Exception {
        List<TableADAO> found = new Persistence(getConnection()).setParameter("a", 1).find("SELECT * FROM TableA WHERE ColumnA = :a ", TableADAO.class);
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(1));
        assertThat(found.get(0).columnB, is("Hello World"));

        found = new Persistence(getConnection()).setParameter("a", "Good Bye, Moon").find("SELECT * FROM TableA WHERE ColumnB = :a ", TableADAO.class);
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(2));
        assertThat(found.get(0).columnB, is("Good Bye, Moon"));
    }


    public static class TableADAO {
        public Integer columnA;
        public String columnB;
    }
}