package com.jubalrife.knucklebones;

import com.jubalrife.knucklebones.dialect.Dialect;
import com.jubalrife.knucklebones.dialect.generic.GenericDialect;
import com.jubalrife.knucklebones.exception.KnuckleBonesException.CouldNotCreateConnection;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PersistenceFactory {
    private final DataSource dataSource;
    private final Dialect dialect;

    public PersistenceFactory(DataSource dataSource) {
        this.dataSource = dataSource;
        this.dialect = new GenericDialect();
    }

    public PersistenceFactory(DataSource dataSource, Dialect dialect) {
        this.dataSource = dataSource;
        this.dialect = dialect;
    }

    public Persistence create() {
        try {
            return new Persistence(dataSource.getConnection(), dialect);
        } catch (SQLException e) {
            throw new CouldNotCreateConnection(e);
        }
    }
}
