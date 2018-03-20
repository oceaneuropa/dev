package org.orbit.platform.sdk.condition;

import java.util.Map;

public interface ICondition {

	/**
	 * 
	 * @param context
	 * @param source
	 * @param target
	 * @param args
	 * @return
	 */
	boolean evaluate(Object context, Object source, Object target, Map<String, Object> args);

}
