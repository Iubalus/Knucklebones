package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.DAOFactory;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GenericDeleteTest {

    @Test
    public void givenCompoundId_buildsExpectedDeleteQuery() {
        SQLWithParameters query = new GenericDelete().createQuery(
                new DAOFactory().create(HasCompoundId.class),
                new HasCompoundId()
        );
        assertThat(query.getSql(), is("DELETE FROM Example WHERE id = ? AND id2 = ?"));
    }

    @Test
    public void givenSingleId_buildsExpectedDeleteQuery() {
        SQLWithParameters query = new GenericDelete().createQuery(
                new DAOFactory().create(HasSingleId.class),
                new HasSingleId()
        );
        assertThat(query.getSql(), is("DELETE FROM Example WHERE id = ?"));
    }

    @Test
    public void givenGeneratedId_buildsExpectedDeleteQuery() {
        SQLWithParameters query = new GenericDelete().createQuery(
                new DAOFactory().create(HasGeneratedId.class),
                new HasGeneratedId()
        );
        assertThat(query.getSql(), is("DELETE FROM Example WHERE id = ?"));
    }
}