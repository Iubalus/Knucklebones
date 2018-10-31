package com;

import com.WithInMemoryDB;
import org.junit.Test;

import java.sql.ResultSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HelloWorld extends WithInMemoryDB {

    @Test
    public void db_helloWorld() throws Exception {
        getConnection().prepareStatement("CREATE TABLE Example ( Message VARCHAR(12))").execute();
        getConnection().prepareStatement("INSERT INTO Example (Message) VALUES('Hello World!')").execute();
        try (ResultSet results = getConnection().prepareStatement("SELECT * FROM Example").executeQuery()) {
            while(results.next()){
                assertThat(results.getObject("Message"), is("Hello World!"));
            }
        }
    }
}
