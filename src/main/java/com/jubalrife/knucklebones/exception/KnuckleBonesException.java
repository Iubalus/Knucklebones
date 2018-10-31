package com.jubalrife.knucklebones.exception;

public class KnuckleBonesException extends RuntimeException {

    public KnuckleBonesException(String message, Throwable cause) {
        super(message, cause);
    }

    public KnuckleBonesException(String message) {
        super(message);
    }
}
