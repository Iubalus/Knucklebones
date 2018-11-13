package com.jubalrife.knucklebones.type;

import com.jubalrife.knucklebones.SupportedTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteExtractor implements SupportedTypes.Extractor {
    @Override
    public Object extract(Integer columnIndex, ResultSet results) throws SQLException {
        byte result = results.getByte(columnIndex);
        if (results.wasNull()) {
            return null;
        }

        return result;
    }
}
