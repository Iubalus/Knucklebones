package com.jubalrife.knucklebones.type;

import com.jubalrife.knucklebones.SupportedTypes;
import com.jubalrife.knucklebones.exception.KnuckleBonesException.UnableToMapNullIntoAPrimitiveValue;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortExtractor implements SupportedTypes.Extractor {

    @Override
    public Object extract(Integer columnIndex, ResultSet results) throws SQLException {
        short result = results.getShort(columnIndex);
        if (results.wasNull()) {
            return null;
        }

        return result;
    }

}
