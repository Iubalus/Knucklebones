package com.jubalrife.knucklebones.sql;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Runner {

    public void run(Connection c) throws IOException, SQLException {
        Path baseDirectory = getBaseDirectory();

        for (Path path : findPatchesToApply(baseDirectory.resolve("schema"))) {
            try (Statement statement = c.createStatement()) {
                statement.executeUpdate(new String(Files.readAllBytes(path)));
            }
        }

        for (Path path : findPatchesToApply(baseDirectory.resolve("data"))) {
            try (Statement statement = c.createStatement()) {
                statement.executeUpdate(new String(Files.readAllBytes(path)));
            }
        }
    }

    private List<Path> findPatchesToApply(Path sqlDirectory) throws IOException {
        SQLFileCollector visitor = new SQLFileCollector();
        Files.walkFileTree(sqlDirectory, visitor);
        List<Path> toRun = visitor.getToRun();
        toRun.sort(Comparator.naturalOrder());
        return toRun;
    }

    private Path getBaseDirectory() {
        Path sqlDirectory;
        try {
            URL anchorLocation = this.getClass().getResource("_.txt");
            sqlDirectory = Paths.get(anchorLocation.toURI()).getParent();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate the resources stored in com.jubalrife.knucklebones.sql");
        }
        return sqlDirectory;
    }

    private static class SQLFileCollector extends SimpleFileVisitor<Path> {
        List<Path> toRun = new ArrayList<>();

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (file.toFile().getName().endsWith(".sql")) {
                toRun.add(file);
            }
            return super.visitFile(file, attrs);
        }

        public List<Path> getToRun() {
            return toRun;
        }
    }
}
