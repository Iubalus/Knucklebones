package com.jubalrife.knucklebones.v1.type;

import com.jubalrife.knucklebones.v1.SupportedTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimestampExtractor implements SupportedTypes.Extractor {
    @Override
    public Object extract(Integer columnIndex, ResultSet results) throws SQLException {
        return results.getTimestamp(columnIndex);
    }
}
