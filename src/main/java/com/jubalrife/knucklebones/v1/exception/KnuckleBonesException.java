package com.jubalrife.knucklebones.v1.exception;

import com.jubalrife.knucklebones.v1.annotation.Id;

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

        public CouldNotFetchData() {
            super("Failed to fetch data");
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

    public static class CouldNotCreateConnection extends KnuckleBonesException {

        public CouldNotCreateConnection(Throwable cause) {
            super("Unable to create connection:", cause);
        }
    }

    public static class CouldNotCreateATransaction extends KnuckleBonesException {

        public CouldNotCreateATransaction(Throwable cause) {
            super("Unable to open a transaction:", cause);
        }
    }

    public static class CouldNotCommitATransaction extends KnuckleBonesException {

        public CouldNotCommitATransaction(Throwable cause) {
            super("Unable to commit a transaction:", cause);
        }
    }

    public static class CouldNotRollbackATransaction extends KnuckleBonesException {

        public CouldNotRollbackATransaction(Throwable cause) {
            super("Unable to commit a transaction:", cause);
        }
    }

    public static class UnableToMapNullIntoAPrimitiveValue extends KnuckleBonesException{
        public UnableToMapNullIntoAPrimitiveValue(int columnIndex){
            super("Unable to map a null value into a primitive type. Null value was retrieved from column " + columnIndex);
        }
    }

    public static class ExpectedSingeResult extends KnuckleBonesException{
        public ExpectedSingeResult(int found) {
            super("One recorded expected found " + found);
        }
    }
}
