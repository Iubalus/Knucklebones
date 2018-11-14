package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.dialect.Dialect;
import com.jubalrife.knucklebones.v1.dialect.generic.GenericDialect;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

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
            throw new KnuckleBonesException.CouldNotCreateConnection(e);
        }
    }
}
