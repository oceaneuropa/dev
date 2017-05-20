package org.origin.common.annotation;

/**
 * In order for the dependency annotations to be detected, classes which declares fields with @Dependency annotation or methods
 * with @DependencyFullfilled or @DependencyUnfullfilled should implement the Annotated interface. Then when bundle activator is started, register the
 * class instance as Annotated service. When bundle activator is stopped, unregister Annotated service.
 * 
 */
public interface Annotated {

}
