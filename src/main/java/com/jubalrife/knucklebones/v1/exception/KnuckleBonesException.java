package com.jubalrife.knucklebones.v1.exception;

import com.jubalrife.knucklebones.v1.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class KnuckleBonesException extends RuntimeException {

    /**
     * By default, this will suppress the stacktrace coming from knucklebones and the supporting data layer below. The message will still be carried through.
     * The intent of this is to help reduce noise as users are troubleshooting or developing.
     * <p>
     * When true, all com.jubalrife.knucklebones and above stacktrace elements will be removed from the stacktrace
     * <p>
     * When false, the entire stacktrace will be displayed.
     */
    public static boolean SHALLOW_STACKTRACE = true;

    public KnuckleBonesException(String message, Throwable cause) {
        super(message, cause);
        trimStacktrace();
    }

    public KnuckleBonesException(String message) {
        super(message);
        trimStacktrace();
    }

    private void trimStacktrace() {
        if (SHALLOW_STACKTRACE) {
            setStackTrace(filterToShallow(getStackTrace()));
            if (getCause() != null) {
                getCause().setStackTrace(new StackTraceElement[]{});
            }
        }
    }

    private StackTraceElement[] filterToShallow(StackTraceElement[] stackTrace) {
        boolean[] keep = new boolean[stackTrace.length];
        int keepCount = 0;
        boolean throwAway = true;
        for (int i = 0; i < stackTrace.length; i++) {
            boolean isKnucklebones = stackTrace[i].getClassName().startsWith("com.jubalrife.knucklebones");
            throwAway &= !isKnucklebones;
            if (throwAway) {
                continue;//throw away the stacktrace above first knucklebones element
            }
            keep[i] = !isKnucklebones;//keep non-knucklebones trace entries after encountering first knucklebones element

            if (keep[i]) {
                keepCount++;
            }
        }
        int fill = 0;
        StackTraceElement[] trimmed = new StackTraceElement[keepCount];
        for (int i = 0; i < stackTrace.length; i++) {
            if (keep[i]) {
                trimmed[fill++] = stackTrace[i];
            }
        }
        return trimmed;
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

    public static class UnableToMapNullIntoAPrimitiveValue extends KnuckleBonesException {
        public UnableToMapNullIntoAPrimitiveValue(int columnIndex) {
            super("Unable to map a null value into a primitive type. Null value was retrieved from column " + columnIndex);
        }
    }

    public static class ExpectedSingeResult extends KnuckleBonesException {
        public ExpectedSingeResult(int found) {
            super("One recorded expected found " + found);
        }
    }

    public static class ParameterNotSet extends KnuckleBonesException {
        public ParameterNotSet(String parameterName, String rawSql) {
            super(String.format("Unable to set parameter [%s] in query [%s]", parameterName, rawSql));
        }
    }

    public static class ListParameterWasEmpty extends KnuckleBonesException {
        public ListParameterWasEmpty(String parameterName) {
            super(String.format("The parameter: [%s] was an empty Collection and could not be expanded.", parameterName));
        }
    }
}
