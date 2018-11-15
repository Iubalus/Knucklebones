package com.jubalrife.knucklebones.v1.type;

import com.jubalrife.knucklebones.v1.SupportedTypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class JavaUtilDateExtractor implements SupportedTypes.Extractor {
    @Override
    public Object extract(Integer columnIndex, ResultSet results) throws SQLException {
        Timestamp timestamp = results.getTimestamp(columnIndex);
        if (timestamp == null) return null;
        return new Date(timestamp.getTime());
    }
}
