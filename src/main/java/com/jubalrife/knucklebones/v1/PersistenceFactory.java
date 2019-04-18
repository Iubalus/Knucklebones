package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.dialect.Dialect;
import com.jubalrife.knucklebones.v1.dialect.generic.GenericDialect;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PersistenceFactory {
    private final DataSource dataSource;
    private final Dialect dialect;
    private final DAOFactory factory;

    /**
     * Creates a Persistence Factory backed by the provided datasource. A {@link GenericDialect} is used by default.
     *
     * @param dataSource Backing datasource this will be used to create {@link java.sql.Connection}
     *                   to back new {@link Persistence} when {@link PersistenceFactory#create()} is called
     */
    public PersistenceFactory(DataSource dataSource) {
        this(dataSource, new GenericDialect());
    }

    /**
     * Creates a Persistence Factory backed by the provided datasource and {@link Dialect}.
     *
     * @param dataSource Backing datasource this will be used to create {@link java.sql.Connection}
     *                   to back new {@link Persistence} when {@link PersistenceFactory#create()} is called
     * @param dialect    used when generating sql
     */
    public PersistenceFactory(DataSource dataSource, Dialect dialect) {
        this.dataSource = dataSource;
        this.dialect = dialect;
        this.factory = new DAOFactory();
    }

    /**
     * Creates a {@link Persistence} from the underlying {@link DataSource}.
     * Calling this method will create a active connection from the data source.
     *
     * @return a new {@link Persistence}
     */
    public Persistence create() {
        try {
            return new Persistence(new PersistenceContext(dataSource.getConnection(), dialect, new SupportedTypesRegistered(), factory));
        } catch (SQLException e) {
            throw new KnuckleBonesException.CouldNotCreateConnection(e);
        }
    }
}
