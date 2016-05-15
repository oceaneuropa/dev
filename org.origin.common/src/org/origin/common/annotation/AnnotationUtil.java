package org.origin.common.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 
 * @see org.kaazing.k3po.driver.internal.netty.channel.Utils
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 * 
 */
public class AnnotationUtil {

	/**
	 * 
	 * @param annotated
	 * @param annotationClass
	 * @return
	 */
	public static Method[] getAnnotationMethods(Object annotated, Class<?> annotationClass) {
		List<Method> matchedMethods = new ArrayList<Method>();
		Method[] methods = annotated.getClass().getDeclaredMethods();
		for (Method method : methods) {
			boolean matchMethod = false;
			Annotation[] annotations = method.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotationClass.equals(annotation.annotationType())) {
					matchMethod = true;
					break;
				}
			}
			if (matchMethod) {
				matchedMethods.add(method);
			}
		}
		return matchedMethods.toArray(new Method[matchedMethods.size()]);
	}

	/**
	 * 
	 * @param target
	 * @param injectClass
	 * @param injectValue
	 */
	public static <T> void methodInject(Object target, Class<T> injectClass, T injectValue) {
		doMethodInject(target, injectClass, injectValue);
	}

	/**
	 * 
	 * @param target
	 * @param injectables
	 */
	public static void methodInject(Object target, Map<Class<?>, Object> injectables) {
		for (Map.Entry<Class<?>, Object> entry : injectables.entrySet()) {
			Class<?> injectClass = entry.getKey();
			Object injectValue = entry.getValue();
			doMethodInject(target, injectClass, injectValue);
		}
	}

	/**
	 * 
	 * @param target
	 * @param injectClass
	 * @param injectValue
	 */
	private static void doMethodInject(Object target, Class<?> injectClass, Object injectValue) {
		Class<?> targetClass = target.getClass();
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (methodName.startsWith("set") && methodName.length() > "set".length() && parameterTypes.length == 1) {
				Resource annotation = method.getAnnotation(Resource.class);
				if (annotation != null) {
					Class<?> resourceType = annotation.type();
					if (resourceType == Object.class) {
						resourceType = parameterTypes[0];
					}
					if (resourceType == injectClass) {
						try {
							method.invoke(target, injectValue);
						} catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

}
