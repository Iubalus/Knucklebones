package com.jubalrife.knucklebones;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.annotation.Column;
import com.jubalrife.knucklebones.annotation.GeneratedValue;
import com.jubalrife.knucklebones.annotation.Id;
import com.jubalrife.knucklebones.annotation.Table;
import com.jubalrife.knucklebones.exception.KnuckleBonesException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DAOTest extends WithInMemoryDB {

    @Before
    public void createTestTable() throws Exception {
        getPersistence().getConnection().prepareStatement("CREATE TABLE Test (A INT)").executeUpdate();
        getPersistence().getConnection().prepareStatement("INSERT INTO Test (A) VALUES(1)").executeUpdate();
    }

    @After
    public void tearDown() throws Exception {
        getPersistence().getConnection().prepareStatement("DROP TABLE Test").executeUpdate();
    }

    @Test
    public void givenAClassWithNoTableNameExpectTheClassName() {
        assertThat(new DAO<>(A.class).getTableName(), is(A.class.getSimpleName()));
        assertThat(new DAO<>(A.class).getColumns(), is(Collections.emptyList()));
    }

    @Test
    public void givenAClassWithATableAnnotationExpectTheNameFromTheAnnotation() {
        assertThat(new DAO<>(B.class).getTableName(), is("TableName"));
        assertThat(new DAO<>(B.class).getColumns(), is(Collections.emptyList()));
    }

    @Test
    public void givenAClassWithASingleFieldWithNoColumnAnnotationExpectNameToBeFieldName() throws NoSuchFieldException {
        DAO<C> dao = new DAO<>(C.class);

        List<DAOColumnField> columns = dao.getColumns();
        assertThat(columns.size(), is(1));
        assertThat(columns.get(0).getName(), is("a"));
        assertThat(columns.get(0).isId(), is(false));
        assertThat(columns.get(0).isGenerated(), is(false));
    }

    @Test
    public void givenAClassWithASingleFieldWithColumnAnnotationExpectNameFromAnnotation() throws NoSuchFieldException {
        DAO<D> dao = new DAO<>(D.class);

        List<DAOColumnField> columns = dao.getColumns();
        assertThat(columns.size(), is(1));
        assertThat(columns.get(0).getName(), is("B"));
        assertThat(columns.get(0).isId(), is(false));
        assertThat(columns.get(0).isGenerated(), is(false));
    }

    @Test
    public void givenAClassWithASingleFieldWithColumnIdAnnotationExpectNameFromAnnotationAndIsId() throws NoSuchFieldException {
        DAO<E> dao = new DAO<>(E.class);

        List<DAOColumnField> columns = dao.getColumns();
        assertThat(columns.size(), is(1));
        assertThat(columns.get(0).getName(), is("B"));
        assertThat(columns.get(0).isId(), is(true));
        assertThat(columns.get(0).isGenerated(), is(false));
    }

    @Test
    public void givenAClassWithASingleFieldWithColumnGeneratedValueAnnotationExpectNameFromAnnotationAndIsGenerated() throws NoSuchFieldException {
        DAO<F> dao = new DAO<>(F.class);

        List<DAOColumnField> columns = dao.getColumns();
        assertThat(columns.size(), is(1));
        assertThat(columns.get(0).getName(), is("B"));
        assertThat(columns.get(0).isId(), is(false));
        assertThat(columns.get(0).isGenerated(), is(true));
    }

    @Test(expected = KnuckleBonesException.CouldNotConstruct.class)
    public void givenObjectThatCannotBeConstructed_expectException() throws Exception {
        new DAO<>(HasInvalidConstructor.class).newInstance();
    }

    @Test(expected = KnuckleBonesException.PropertyInaccessible.class)
    public void givenObjectWithInaccessibleProperties_expectException() throws Exception {
        try (ResultSet results = getPersistence()
                .getConnection()
                .prepareStatement("SELECT A FROM Test")
                .executeQuery()) {
            List<HasInaccessibleProperties> a = new DAO<>(HasInaccessibleProperties.class).fillFromResultSet(
                    results,
                    getPersistence().getSupportedTypesRegistered()
            );
            System.out.println(a);
        }
    }

    @Test
    public void givenSimpleFillablePojo_expectFilled() throws Exception {
        try (ResultSet results = getPersistence()
                .getConnection()
                .prepareStatement("SELECT A FROM Test")
                .executeQuery()) {
            List<Fillable> result = new DAO<>(Fillable.class).fillFromResultSet(
                    results,
                    getPersistence().getSupportedTypesRegistered()
            );
            assertThat(result.size(), is(1));
            assertThat(result.get(0).a, is(1));
        }
    }

    static class HasInvalidConstructor {
        private HasInvalidConstructor() {}
    }

    static class HasInaccessibleProperties {
        private Integer a;
    }

    static class Fillable {
        public Integer a;
    }

    static class A {}

    @Table(name = "TableName")
    static class B {}

    static class C {
        public String a;
    }
    static class D {
        @Column(name = "B")
        public String a;
    }

    static class E {
        @Id
        @Column(name = "B")
        public String a;
    }

    static class F {
        @GeneratedValue
        @Column(name = "B")
        public String a;
    }
}