package com.jubalrife.knucklebones.v1.type;

import com.jubalrife.knucklebones.v1.SupportedTypes;
import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DoublePrimitiveExtractor implements SupportedTypes.Extractor {
    @Override
    public Object extract(Integer columnIndex, ResultSet results) throws SQLException {
        double result = results.getDouble(columnIndex);
        if(results.wasNull()){
            throw new KnuckleBonesException.UnableToMapNullIntoAPrimitiveValue(columnIndex);
        }
        return result;
    }
}
