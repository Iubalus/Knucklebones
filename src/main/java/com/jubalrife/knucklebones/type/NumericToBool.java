package com.jubalrife.knucklebones.type;

import com.jubalrife.knucklebones.SupportedTypes;
import com.jubalrife.knucklebones.exception.KnuckleBonesException;
import com.jubalrife.knucklebones.exception.KnuckleBonesException.UnableToMapNullIntoAPrimitiveValue;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NumericToBool implements SupportedTypes.Extractor {

    @Override
    public Object extract(Integer columnIndex, ResultSet results) throws SQLException {
        boolean result = results.getBoolean(columnIndex);
        if (results.wasNull()) {
            throw new UnableToMapNullIntoAPrimitiveValue(columnIndex);
        }
        return result;
    }
}
