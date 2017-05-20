package org.origin.common.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Dependency {

	String name() default "";

	Class type() default java.lang.Object.class;

}
