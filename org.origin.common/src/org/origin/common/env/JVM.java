package org.origin.common.env;

/**
 * @see org.apache.hadoop.util.Shell
 *
 */
public class JVM {

	private static boolean IS_JAVA7_OR_ABOVE = System.getProperty("java.version").substring(0, 3).compareTo("1.7") >= 0;
	private static boolean IS_JAVA8_OR_ABOVE = System.getProperty("java.version").substring(0, 3).compareTo("1.8") >= 0;

	public static boolean isJava7OrAbove() {
		return IS_JAVA7_OR_ABOVE;
	}

	public static boolean isJava8OrAbove() {
		return IS_JAVA8_OR_ABOVE;
	}

}
