package com;

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
        connectionPool = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "sa");
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
