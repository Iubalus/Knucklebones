package com.jubalrife.knucklebones.exception;

import java.lang.reflect.Field;

public class KnuckleBonesException extends RuntimeException {

    public KnuckleBonesException(String message, Throwable cause) {
        super(message, cause);
    }

    public KnuckleBonesException(String message) {
        super(message);
    }

    public static class PropertyInaccessible extends KnuckleBonesException {
        public PropertyInaccessible(Field field, Class<?> type, Throwable cause) {
            super(String.format("Could not access property %s of %s", field.getName(), type.getName()), cause);
        }
    }
    public static class CouldNotFetchData extends KnuckleBonesException {
        public CouldNotFetchData(Throwable cause) {
            super("Failed to fetch data", cause);
        }
    }
}
