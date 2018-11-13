package com.jubalrife.knucklebones.type;

import com.jubalrife.knucklebones.SupportedTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LongExtractor implements SupportedTypes.Extractor {
    @Override
    public Object extract(Integer columnIndex, ResultSet results) throws SQLException {
        long result = results.getLong(columnIndex);
        if (results.wasNull()) {
            return null;
        }

        return result;
    }
}
