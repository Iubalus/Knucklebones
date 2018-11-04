package com.jubalrife.knucklebones;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.exception.KnuckleBonesException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

public class DAOFillerTest extends WithInMemoryDB {

    @Before
    public void createTestTable() throws Exception {
        getConnection().prepareStatement("CREATE TABLE Test (A INT)").executeUpdate();
        getConnection().prepareStatement("INSERT INTO Test (A) VALUES(1)").executeUpdate();
    }

    @After
    public void tearDown() throws Exception {
        getConnection().prepareStatement("DROP TABLE Test").executeUpdate();
    }

    @Test
    public void givenSimpleFillablePojo_expectFilled() throws Exception {
        try (ResultSet results = getConnection().prepareStatement("SELECT A FROM Test").executeQuery()) {
            List<Fillable> result = DAOFiller.fillFromResultSet(Fillable.class, results);
            assertThat(result.size(), is(1));
            assertThat(result.get(0).a, is(1));
        }
    }

    @Test(expected = DAOFiller.CouldNotConstruct.class)
    public void givenObjectThatCannotBeConstructed_expectException() throws Exception {
        try (ResultSet results = getConnection().prepareStatement("SELECT A FROM Test").executeQuery()) {
            List<HasPrivateConstructor> result = DAOFiller.fillFromResultSet(HasPrivateConstructor.class, results);
            assertThat(result.size(), is(1));
            assertThat(result.get(0).a, is(1));
        }
    }

    @Test(expected = KnuckleBonesException.PropertyInaccessible.class)
    public void givenObjectWithInaccessibleProperties_expectException() throws Exception {
        try (ResultSet results = getConnection().prepareStatement("SELECT A FROM Test").executeQuery()) {
            List<HasInaccessibleProperties> result = DAOFiller.fillFromResultSet(HasInaccessibleProperties.class, results);
            assertThat(result.size(), is(1));
            assertThat(result.get(0).a, nullValue());
        }
    }

    static class HasPrivateConstructor {
        public Integer a;

        private HasPrivateConstructor(Integer a) {
            this.a = a;
        }
    }

    static class HasInaccessibleProperties{
        private Integer a;
    }

    static class Fillable {
        public Integer a;
    }

}