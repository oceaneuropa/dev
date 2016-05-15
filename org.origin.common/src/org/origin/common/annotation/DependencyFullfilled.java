package org.origin.common.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Method annotated with DependencyFullfilled is expected to have no parameters.
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface DependencyFullfilled {

}
