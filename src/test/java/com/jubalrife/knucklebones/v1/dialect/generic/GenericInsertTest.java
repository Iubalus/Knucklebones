package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.DAOFactory;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GenericInsertTest {
    @Test
    public void givenCompoundId_generatesAppropriateInsertQuery() {
        String update = new GenericInsert().createQuery(new DAOFactory().create(HasCompoundId.class));
        assertThat(update, is("INSERT INTO Example(id, id2, value1, value2) VALUES (?, ?, ?, ?)"));
    }

    @Test
    public void givenSingleId_generatesAppropriateInsertQuery() {
        String update = new GenericInsert().createQuery(new DAOFactory().create(HasSingleId.class));
        assertThat(update, is("INSERT INTO Example(id, value1, value2) VALUES (?, ?, ?)"));
    }

    @Test
    public void givenGeneratedId_generatesAppropriateInsertQuery() {
        String update = new GenericInsert().createQuery(new DAOFactory().create(HasGeneratedId.class));
        assertThat(update, is("INSERT INTO Example(value) VALUES (?)"));
    }


}