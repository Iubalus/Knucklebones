package com.jubalrife.knucklebones.type;

import com.jubalrife.knucklebones.SupportedTypes;
import com.jubalrife.knucklebones.exception.KnuckleBonesException;
import com.jubalrife.knucklebones.exception.KnuckleBonesException.UnableToMapNullIntoAPrimitiveValue;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LongPrimitiveExtractor implements SupportedTypes.Extractor {

    @Override
    public Object extract(Integer columnIndex, ResultSet results) throws SQLException {
        long result = results.getLong(columnIndex);
        if (results.wasNull()) {
            throw new UnableToMapNullIntoAPrimitiveValue(columnIndex);
        }

        return result;
    }

}
