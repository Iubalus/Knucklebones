package com;

import com.jubalrife.knucklebones.sql.Runner;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.sql.Connection;

public class WithInMemoryDB {

    private static Connection connection;
    private static JdbcConnectionPool connectionPool;

    @BeforeClass
    public static final void _setupConnectionPool() throws Exception {
        if (connectionPool != null) return;

        connectionPool = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa");
        try (Connection connection = connectionPool.getConnection()) {
            new Runner().run(connection);
        }
    }

    @Before
    public final void _before() throws Exception {
        connection = connectionPool.getConnection();
    }

    @After
    public final void _after() throws Exception {
        connection.close();
    }

    public static Connection getConnection() {
        return connection;
    }
}
