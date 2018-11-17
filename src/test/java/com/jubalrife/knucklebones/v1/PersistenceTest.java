package com.jubalrife.knucklebones.v1;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.v1.annotation.GeneratedValue;
import com.jubalrife.knucklebones.v1.annotation.Id;
import com.jubalrife.knucklebones.v1.annotation.Table;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PersistenceTest extends WithInMemoryDB {

    @Test
    public void canFillDAOWithSimpleValues() {
        List<TableADAO> found = getPersistence().createNativeQuery("SELECT * FROM TableA WHERE ColumnA = 1", TableADAO.class).findResults();
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(1));
        assertThat(found.get(0).columnB, is("Hello World"));
    }

    @Test
    public void canFindWithSimpleParametersDAOWithSimpleValues() {
        List<TableADAO> found = getPersistence().createNativeQuery("SELECT * FROM TableA WHERE ColumnA = :a ", TableADAO.class).setParameter("a", 1).findResults();
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(1));
        assertThat(found.get(0).columnB, is("Hello World"));

        found = getPersistence().createNativeQuery("SELECT * FROM TableA WHERE ColumnB = :a ", TableADAO.class).setParameter("a", "Good Bye, Moon").findResults();
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(2));
        assertThat(found.get(0).columnB, is("Good Bye, Moon"));
    }

    @Test
    public void insertedItemCanBeFound() {
        TableADAO o = new TableADAO();
        o.columnA = 3;
        o.columnB = "Text";

        Persistence persistence = getPersistence();
        persistence.insert(o);

        List<TableADAO> found = persistence.createNativeQuery("SELECT * FROM TableA WHERE ColumnA = 3", TableADAO.class).findResults();
        assertThat(found.size(), is(1));
        assertThat(found.get(0).columnA, is(3));
        assertThat(found.get(0).columnB, is("Text"));
    }

    @Test
    public void insertedItemHonorsGeneratedValues() {
        GeneratedColumns o = new GeneratedColumns();
        Persistence persistence = getPersistence();
        o = persistence.insert(o);

        assertThat(o.id, is(1));
        assertThat(o.defaultValue, is(5));
    }

    @Test
    public void testFindBasedOnIdValueOnId() {
        TableAWithId dao = new TableAWithId();
        dao.columnA = 1;

        Persistence persistence = getPersistence();
        dao = persistence.find(dao);

        assertThat(dao.columnA, is(1));
        assertThat(dao.columnB, is("Hello World"));
    }

    @Test
    public void testDateInsertAndRetrieval() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Term term = new Term();
        term.startDate = format.parse("2018-10-01");
        term.endDate = format.parse("2018-12-15");

        Persistence persistence = getPersistence();
        Term inserted = persistence.insert(term);


        Term found = persistence.find(inserted);
        assertNotNull(found.termId);
        assertThat(term.startDate, is(found.startDate));
        assertThat(term.endDate, is(found.endDate));
    }

    @Test
    public void updateSucceedsWhenIdIsPresent() {
        TableAWithId id = new TableAWithId();
        id.columnA = 1;

        Persistence persistence = getPersistence();
        TableAWithId toUpdate = persistence.find(id);
        toUpdate.columnB = "Updated";

        persistence.update(toUpdate);

        TableAWithId updated = persistence.find(toUpdate);
        assertThat(updated.columnB, is("Updated"));
    }

    @Test
    public void deleteSucceedsWhenIdIsPresent() {
        TableAWithId id = new TableAWithId();
        id.columnA = 1;

        Persistence persistence = getPersistence();
        int deleted = persistence.delete(id);

        assertThat(deleted, is(1));
        assertNull(persistence.find(id));
    }

    @Test(expected = KnuckleBonesException.OperationRequiresIdOnAtLeastOneField.class)
    public void updateFailsWhenIdIsNotPresent() {
        TableADAO id = new TableADAO();
        id.columnA = 1;
        id.columnB = "Updated";

        getPersistence().update(id);
    }

    @Test(expected = KnuckleBonesException.OperationRequiresIdOnAtLeastOneField.class)
    public void findFailsWhenIdIsNotPresent() {
        TableADAO id = new TableADAO();
        id.columnA = 1;

        getPersistence().find(id);
    }

    @Test(expected = KnuckleBonesException.OperationRequiresIdOnAtLeastOneField.class)
    public void deleteFailsWhenIdIsNotPresent() {
        TableADAO id = new TableADAO();
        id.columnA = 1;

        getPersistence().delete(id);
    }

    @Test
    public void testUncheckQueryWithSingleRowSingleValue() {
        Integer singleResult = getPersistence().createNativeQuery("SELECT 1").findSingleResult();
        assertThat(singleResult, is(1));
    }

    @Test
    public void testUncheckQueryWithSingleRowMultipleValues() {
        Object[] singleResult = getPersistence().createNativeQuery("SELECT 1,2,3").findSingleResult();
        assertThat(singleResult, is(new Object[]{1, 2, 3}));
    }

    @Test
    public void testUncheckQueryWithMultipleRowSingleValue() {
        List<Integer> singleResult = getPersistence()
                .createNativeQuery("SELECT 1 UNION SELECT 2 UNION SELECT 3")
                .findResultList();
        assertThat(singleResult, is(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testUncheckQueryWithMultipleRowMultipleValues() {
        List<Object[]> singleResult = getPersistence()
                .createNativeQuery("SELECT 1,2,3 UNION SELECT 4,5,6 UNION SELECT 7,8,9")
                .findResultList();

        assertThat(singleResult.get(0), is(new Object[]{1, 2, 3}));
        assertThat(singleResult.get(1), is(new Object[]{4, 5, 6}));
        assertThat(singleResult.get(2), is(new Object[]{7, 8, 9}));
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