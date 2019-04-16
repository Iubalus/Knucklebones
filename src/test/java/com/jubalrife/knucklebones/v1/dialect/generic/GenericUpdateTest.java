package com.jubalrife.knucklebones.v1.dialect.generic;

import com.jubalrife.knucklebones.v1.DAOFactory;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GenericUpdateTest {

    @Test
    public void givenCompoundId_generatesExpectedUpdateQuery() {
        String update = new GenericUpdate().createQuery(new DAOFactory().create(HasCompoundId.class));
        assertThat(update, is("UPDATE Example SET value1 = ?, value2 = ? WHERE id = ? AND id2 = ?"));
    }

    @Test
    public void givenSingleId_generatesExpectedUpdateQuery() {
        String update = new GenericUpdate().createQuery(new DAOFactory().create(HasSingleId.class));
        assertThat(update, is("UPDATE Example SET value1 = ?, value2 = ? WHERE id = ?"));
    }

    @Test
    public void givenGeneratedId_generatesExpectedUpdateQuery() {
        String update = new GenericUpdate().createQuery(new DAOFactory().create(HasGeneratedId.class));
        assertThat(update, is("UPDATE Example SET value = ? WHERE id = ?"));
    }

}