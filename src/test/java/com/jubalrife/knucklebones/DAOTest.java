package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.annotation.Column;
import com.jubalrife.knucklebones.annotation.GeneratedValue;
import com.jubalrife.knucklebones.annotation.Id;
import com.jubalrife.knucklebones.annotation.Table;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DAOTest {
    @Test
    public void givenAClassWithNoTableNameExpectTheClassName() {
        assertThat(new DAO(A.class).getTableName(), is(A.class.getSimpleName()));
        assertThat(new DAO(A.class).getColumns(), is(Collections.emptyList()));
    }

    @Test
    public void givenAClassWithATableAnnotationExpectTheNameFromTheAnnotation() {
        assertThat(new DAO(B.class).getTableName(), is("TableName"));
        assertThat(new DAO(B.class).getColumns(), is(Collections.emptyList()));
    }

    @Test
    public void givenAClassWithASingleFieldWithNoColumnAnnotationExpectNameToBeFieldName() throws NoSuchFieldException {
        DAO dao = new DAO(C.class);

        List<ColumnField> columns = dao.getColumns();
        assertThat(columns.size(), is(1));
        assertThat(columns.get(0).getName(), is("a"));
        assertThat(columns.get(0).isId(), is(false));
        assertThat(columns.get(0).isGenerated(), is(false));
    }

    @Test
    public void givenAClassWithASingleFieldWithColumnAnnotationExpectNameFromAnnotation() throws NoSuchFieldException {
        DAO dao = new DAO(D.class);

        List<ColumnField> columns = dao.getColumns();
        assertThat(columns.size(), is(1));
        assertThat(columns.get(0).getName(), is("B"));
        assertThat(columns.get(0).isId(), is(false));
        assertThat(columns.get(0).isGenerated(), is(false));
    }

    @Test
    public void givenAClassWithASingleFieldWithColumnIdAnnotationExpectNameFromAnnotationAndIsId() throws NoSuchFieldException {
        DAO dao = new DAO(E.class);

        List<ColumnField> columns = dao.getColumns();
        assertThat(columns.size(), is(1));
        assertThat(columns.get(0).getName(), is("B"));
        assertThat(columns.get(0).isId(), is(true));
        assertThat(columns.get(0).isGenerated(), is(false));
    }

    @Test
    public void givenAClassWithASingleFieldWithColumnGeneratedValueAnnotationExpectNameFromAnnotationAndIsGenerated() throws NoSuchFieldException {
        DAO dao = new DAO(F.class);

        List<ColumnField> columns = dao.getColumns();
        assertThat(columns.size(), is(1));
        assertThat(columns.get(0).getName(), is("B"));
        assertThat(columns.get(0).isId(), is(false));
        assertThat(columns.get(0).isGenerated(), is(true));
    }

    public static class A {}

    @Table(name = "TableName")
    public static class B {}

    public static class C {
        public String a;
    }
    public static class D {
        @Column(name = "B")
        public String a;
    }

    public static class E {
        @Id
        @Column(name = "B")
        public String a;
    }

    public static class F {

        @GeneratedValue
        @Column(name = "B")
        public String a;
    }
}