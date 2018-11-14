package com.jubalrife.knucklebones.v1.type;

import com.jubalrife.knucklebones.v1.SupportedTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NumericToBoolean implements SupportedTypes.Extractor {

    @Override
    public Object extract(Integer columnIndex, ResultSet results) throws SQLException {
        boolean result = results.getBoolean(columnIndex);
        if (results.wasNull()) {
            return null;
        }
        return result;
    }
}
