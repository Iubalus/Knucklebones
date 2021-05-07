package com.jubalrife.knucklebones.v1;

import com.WithInMemoryDB;
import com.jubalrife.knucklebones.v1.annotation.Column;
import com.jubalrife.knucklebones.v1.annotation.GeneratedValue;
import com.jubalrife.knucklebones.v1.annotation.Id;
import com.jubalrife.knucklebones.v1.annotation.Table;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Test(expected = KnuckleBonesException.CouldNotFetchData.class)
    public void deleteSucceedsWhenIdIsPresent() {
        TableAWithId id = new TableAWithId();
        id.columnA = 1;

        Persistence persistence = getPersistence();
        int deleted = persistence.delete(id);

        assertThat(deleted, is(1));
        persistence.find(id);
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
                .findResults();
        assertThat(singleResult, is(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testUncheckQueryWithMultipleRowMultipleValues() {
        List<Object[]> singleResult = getPersistence()
                .createNativeQuery("SELECT 1,2,3 UNION SELECT 4,5,6 UNION SELECT 7,8,9")
                .findResults();

        assertThat(singleResult.get(0), is(new Object[]{1, 2, 3}));
        assertThat(singleResult.get(1), is(new Object[]{4, 5, 6}));
        assertThat(singleResult.get(2), is(new Object[]{7, 8, 9}));
    }

    @Test
    public void testInsertUpdateWithMulipleColumns() {
        TableWithMultipleColumns row = new TableWithMultipleColumns();
        row.columnA = 1;
        row.columnB = 1;
        row.columnC = 1;
        row.columnD = 1;
        row.columnE = 1;

        getPersistence().insert(row);
        TableWithMultipleColumns found = getPersistence().find(row);

        found.columnB = 2;
        found.columnC = 2;
        found.columnD = 2;
        found.columnE = 2;
        getPersistence().update(found);
        TableWithMultipleColumns finalResult = getPersistence().find(row);
        assertThat(finalResult.columnA, is(1));
        assertThat(finalResult.columnB, is(2));
        assertThat(finalResult.columnC, is(2));
        assertThat(finalResult.columnD, is(2));
        assertThat(finalResult.columnE, is(2));

    }

    @Test
    public void bulkInsert() {
        ArrayList<TableWithMultipleColumns> toSave = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            toSave.add(new TableWithMultipleColumns());
        }

        getPersistence().insert(toSave);

        Long count = getPersistence().createNativeQuery("SELECT COUNT(*) FROM TableWithMultipleColumns").findSingleResult();
        assertThat(count, is(100L));

    }

    @Test
    public void inTransaction_canPerformActionInTransaction() {
        ErrorHandlerSpy handled = new ErrorHandlerSpy();

        PersistenceFactory factory = new PersistenceFactory(JdbcConnectionPool.create("jdbc:h2:mem:inTransactionTest", "sa", "sa"));
        Persistence.inTransaction(factory, p -> p.createNativeQuery("CREATE TABLE T1 (Example INT)").executeUpdate(), handled);
        assertThat(handled.getHandled().isEmpty(), is(true));

        factory.inTransaction(p -> p.createNativeQuery("CREATE TABLE T2 (Example INT)").executeUpdate(), handled);
        assertThat(handled.getHandled().isEmpty(), is(true));

        try (Persistence per = factory.create()) {
            per.inTransaction(p -> p.createNativeQuery("CREATE TABLE T3 (Example INT)").executeUpdate(), handled);
        }
        assertThat(handled.getHandled().isEmpty(), is(true));

        //tables exist outside of the transaction
        try (Persistence per = factory.create()) {
            assertThat(per.createNativeQuery("SELECT COUNT(*) FROM T1").findSingleResult(), is(0L));
            assertThat(per.createNativeQuery("SELECT COUNT(*) FROM T2").findSingleResult(), is(0L));
            assertThat(per.createNativeQuery("SELECT COUNT(*) FROM T3").findSingleResult(), is(0L));
        }
    }

    /**
     * I wrote this test to determine that transactions were rolling back appropriately and discovered
     * that the h2 database might not actually support transactions in the way I expected. I will do
     * further investigation to determine if there is anything I can do about this.
     *
     * So far as I know, the persistenceContext.getConnection().setAutoCommit(false); in {@link PersistenceBase}
     * should begin a transaction in normal database connections. It just doesn't appear that h2 honors that.
     *
     * I am going to keep this test for when I figure out a workaround (I may have to use a different db driver)
     */
    @Test
    @Ignore
    public void inTransaction_doesRollbackOnError() {
        ErrorHandlerSpy handled = new ErrorHandlerSpy();

        PersistenceFactory factory = new PersistenceFactory(JdbcConnectionPool.create("jdbc:h2:mem:inTransactionTest", "sa", "sa"));
        Persistence.inTransaction(
                factory,
                p -> {
                    p.createNativeQuery("CREATE TABLE T1 (Example INT)").executeUpdate();
                    p.createNativeQuery("INSERT INTO T1 (Example) VALUES (1),(2)").executeUpdate();
                },
                handled
        );

        try (Persistence per = factory.create()) {
            assertThat(per.createNativeQuery("SELECT COUNT(*) FROM T1").findSingleResult(), is(2L));
        }

        Persistence.inTransaction(
                factory,
                p -> {
                    p.createNativeQuery("TRUNCATE TABLE T1").executeUpdate();
                    throw new RuntimeException("Fail");
                },
                handled
        );
        assertThat(handled.getHandled().size(), is(1));

        factory.inTransaction(
                p -> {
                    p.createNativeQuery("TRUNCATE TABLE T1").executeUpdate();
                    throw new RuntimeException("Fail");
                },
                handled
        );
        assertThat(handled.getHandled().size(), is(2));

        try (Persistence per = factory.create()) {
            per.inTransaction(
                    p -> {
                        p.createNativeQuery("TRUNCATE TABLE T1").executeUpdate();
                        throw new RuntimeException("Fail");
                    },
                    handled
            );
        }
        assertThat(handled.getHandled().size(), is(3));

        try (Persistence per = factory.create()) {
            assertThat(per.createNativeQuery("SELECT COUNT(*) FROM T1").findSingleResult(), is(2L));
        }
    }

    @Test
    public void inTransaction_expectErrorsWhenRestrictedFunctionsAreCalled() {
        ErrorHandlerSpy handled = new ErrorHandlerSpy();

        PersistenceFactory factory = new PersistenceFactory(JdbcConnectionPool.create("jdbc:h2:mem:inTransactionTest", "sa", "sa"));
        Persistence.inTransaction(factory, Persistence::begin, handled);
        Persistence.inTransaction(factory, Persistence::rollback, handled);
        Persistence.inTransaction(factory, Persistence::commit, handled);
        Persistence.inTransaction(factory, Persistence::getConnection, handled);
        Persistence.inTransaction(factory, Persistence::close, handled);
        Persistence.inTransaction(factory, (p) -> p.inTransaction(p2 -> {}, e2 -> {}), handled);
        assertThat(handled.getHandled().size(), is(6));

        factory.inTransaction(Persistence::begin, handled);
        factory.inTransaction(Persistence::rollback, handled);
        factory.inTransaction(Persistence::commit, handled);
        factory.inTransaction(Persistence::getConnection, handled);
        factory.inTransaction(Persistence::close, handled);
        factory.inTransaction((p) -> p.inTransaction(p2 -> {}, e2 -> {}), handled);
        assertThat(handled.getHandled().size(), is(12));

        try (Persistence per = factory.create()) {
            per.inTransaction(Persistence::begin, handled);
            per.inTransaction(Persistence::rollback, handled);
            per.inTransaction(Persistence::commit, handled);
            per.inTransaction(Persistence::getConnection, handled);
            per.inTransaction(Persistence::close, handled);
            per.inTransaction((p) -> p.inTransaction(p2 -> {}, e2 -> {}), handled);
        }

        assertThat(handled.getHandled().size(), is(18));
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

    public static class TableWithMultipleColumns {
        @Id
        @Column(name = "COLUMN_A")
        public Integer columnA;
        @Column(name = "COLUMN_B")
        public Integer columnB;
        @Column(name = "COLUMN_C")
        public Integer columnC;
        @Column(name = "COLUMN_D")
        public Integer columnD;
        @Column(name = "COLUMN_E")
        public Integer columnE;

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

    private static class ErrorHandlerSpy implements Persistence.ErrorHandler {
        private List<Exception> handled = new ArrayList<>();

        @Override
        public void accept(Exception e) {
            handled.add(e);
        }

        public List<Exception> getHandled() {
            return handled;
        }
    }
}