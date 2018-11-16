package com.jubalrife.knucklebones.v1.type;

import com.jubalrife.knucklebones.v1.SupportedTypes;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatExtractor implements SupportedTypes.Extractor {
    @Override
    public Object extract(Integer columnIndex, ResultSet results) throws SQLException {
        float result = results.getFloat(columnIndex);
        if (results.wasNull()) {
            return null;
        }
        return result;
    }
}
