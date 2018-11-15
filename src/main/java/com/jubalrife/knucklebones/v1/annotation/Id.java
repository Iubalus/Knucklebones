package com.jubalrife.knucklebones.v1.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @Id annotation is used to mark a field of a class as part of the candidate key.
 * The annotated field will be used in the where clause of an select, insert, update, or delete statement.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
}
