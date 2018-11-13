package com.jubalrife.knucklebones.type;

import com.jubalrife.knucklebones.SupportedTypes;
import com.jubalrife.knucklebones.exception.KnuckleBonesException.UnableToMapNullIntoAPrimitiveValue;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BytePrimitiveExtractor implements SupportedTypes.Extractor {
    @Override
    public Object extract(Integer columnIndex, ResultSet results) throws SQLException {
        byte result = results.getByte(columnIndex);
        if (results.wasNull()) {
            throw new UnableToMapNullIntoAPrimitiveValue(columnIndex);
        }

        return result;
    }
}
