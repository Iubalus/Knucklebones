package com.jubalrife.knucklebones.v1;

import com.jubalrife.knucklebones.v1.dialect.Dialect;
import com.jubalrife.knucklebones.v1.dialect.generic.GenericDialect;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Factory used to construct {@link Persistence}.
 */
public class PersistenceFactory {
    private final ConnectionFactory connectionSupplier;
    private final Dialect dialect;
    private final DAOFactory factory = new DAOFactory();
    private final SupportedTypesRegistered supportedTypes = new SupportedTypesRegistered();

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
        this.connectionSupplier = dataSource::getConnection;
        this.dialect = dialect;
    }

    /**
     * Creates a Persistence Factory backed by the provided connection
     *
     * @param connectionFactory a factory for producing connections (fresh connections should be provided every time since {@link Persistence} will close connections supplied in this way)
     */
    public PersistenceFactory(ConnectionFactory connectionFactory) {
        this(connectionFactory, new GenericDialect());
    }

    /**
     * Creates a Persistence Factory backed by the provided connection and {@link Dialect}
     *
     * @param connectionFactory a factory for producing connections (fresh connections should be provided every time since {@link Persistence} will close connections supplied in this way)
     * @param dialect used when generating sql
     */
    public PersistenceFactory(ConnectionFactory connectionFactory, Dialect dialect){
        this.connectionSupplier = connectionFactory;
        this.dialect = dialect;
    }

    /**
     * @return the {@link SupportedTypes} for this PersistenceFactory. A persistence created from this factory will inherit registered supported types.
     */
    public SupportedTypes getSupportedTypes() {
        return supportedTypes;
    }

    /**
     * Creates a {@link Persistence} from the underlying {@link DataSource}.
     * Calling this method will create a active connection from the data source.
     *
     * @return a new {@link Persistence}
     */
    public Persistence create() {
        try {
            return new Persistence(new PersistenceContext(connectionSupplier.create(), dialect, supportedTypes.createCopy(), factory));
        } catch (Exception e) {
            throw new KnuckleBonesException.CouldNotCreateConnection(e);
        }
    }

    public interface ConnectionFactory{
        Connection create() throws Exception;
    }

}
