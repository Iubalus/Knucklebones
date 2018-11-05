package com.jubalrife.knucklebones.exception;

import com.jubalrife.knucklebones.annotation.Id;

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

    public static class CouldNotUpdateData extends KnuckleBonesException {
        public CouldNotUpdateData(Throwable cause) {
            super("Failed to update data", cause);
        }
    }

    public static class OperationRequiresIdOnAtLeastOneField extends KnuckleBonesException {
        public OperationRequiresIdOnAtLeastOneField(Class<?> type) {
            super(String.format("Update requires at least one %s on class %s", Id.class.getName(), type.getName()));
        }
    }

    public static class CouldNotConstruct extends KnuckleBonesException {
        public CouldNotConstruct(Class<?> type, Throwable cause) {
            super(String.format("Could not construct instance of %s", type.getName()), cause);
        }
    }
}
