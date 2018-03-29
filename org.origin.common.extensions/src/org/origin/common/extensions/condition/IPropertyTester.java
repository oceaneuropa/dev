package org.origin.common.extensions.condition;

import java.util.Map;

public interface IPropertyTester {

	public static final String TYPE_ID = "origin.common.IPropertyTester";

	/**
	 * 
	 * @param context
	 * @param source
	 * @param target
	 * @param args
	 * @return
	 */
	boolean accept(Object context, Object source, Object target, Map<String, Object> args);

}
