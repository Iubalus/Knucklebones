package com;

import com.jubalrife.knucklebones.v1.Persistence;
import com.jubalrife.knucklebones.v1.PersistenceFactory;
import com.jubalrife.knucklebones.v1.sql.Runner;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.sql.Connection;

public class WithInMemoryDB {

    private static JdbcConnectionPool connectionPool;
    private static PersistenceFactory factory;
    private Persistence connection;

    @BeforeClass
    public static final void _setupConnectionPool() throws Exception {
        if (connectionPool != null) return;

        connectionPool = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa");
        factory = new PersistenceFactory(connectionPool);

        try (Connection connection = connectionPool.getConnection()) {
            new Runner().run(connection);
        }
    }

    @Before
    public final void _before() throws Exception {
        connection = factory.create();
        connection.begin();
    }

    @After
    public final void _after() throws Exception {
        connection.rollback();
        connection.close();
    }

    public Persistence getPersistence() {
        return connection;
    }
}
