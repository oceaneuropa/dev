package org.origin.common.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Dependency {

	String name() default "";

	Class type() default java.lang.Object.class;

}
