package org.origin.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ExceptionUtil {

	/**
	 * 
	 * @param object
	 * @param exceptionClass
	 * @param nullErrorMessage
	 * @throws RuntimeException
	 */
	public static void checkNotNull(Object object, Class<? extends RuntimeException> exceptionClass, String nullErrorMessage) throws RuntimeException {
		if (object == null) {
			if (exceptionClass == null) {
				exceptionClass = RuntimeException.class;
			}
			if (nullErrorMessage == null) {
				nullErrorMessage = exceptionClass.getName() + " is null.";
			}
			try {
				Constructor<? extends RuntimeException> exceptionConstructor = exceptionClass.getConstructor(String.class);
				RuntimeException runtimeException = exceptionConstructor.newInstance(nullErrorMessage);
				if (runtimeException != null) {
					throw runtimeException;
				}

			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}

			throw new RuntimeException(nullErrorMessage);
		}
	}

	/**
	 * 
	 * @param value
	 * @param exceptionClass
	 * @param nullErrorMessage
	 * @throws RuntimeException
	 */
	public static void checkNotNullAndNotEmpty(String value, Class<? extends RuntimeException> exceptionClass, String nullErrorMessage, String emptyErrorMessage) throws RuntimeException {
		checkNotNull(value, exceptionClass, nullErrorMessage);

		if (value.isEmpty()) {
			if (exceptionClass == null) {
				exceptionClass = RuntimeException.class;
			}
			if (emptyErrorMessage == null) {
				emptyErrorMessage = exceptionClass.getName() + " is empty.";
			}
			try {
				Constructor<? extends RuntimeException> exceptionConstructor = exceptionClass.getConstructor(String.class);
				RuntimeException runtimeException = exceptionConstructor.newInstance(emptyErrorMessage);
				if (runtimeException != null) {
					throw runtimeException;
				}

			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}

			throw new RuntimeException(nullErrorMessage);
		}
	}

}
