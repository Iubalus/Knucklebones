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

    public static class TableADAO {
        public Integer columnA;
        public String columnB;
    }
}