package org.origin.common.extensions.condition;

import java.util.Map;

public class ConditionFactory {

	protected static ConditionFactory INSTANCE = new ConditionFactory();

	public static ConditionFactory getInstance() {
		return ConditionFactory.INSTANCE;
	}

	/**
	 * 
	 * @param operator
	 * @return
	 */
	public ICondition newConditions(Conditions.OPERATOR operator) {
		return new Conditions(operator);
	}

	/**
	 * 
	 * @param propertyTesterId
	 * @return
	 */
	public ICondition newPropertyTesterCondition(String propertyTesterId) {
		return new PropertyTesterCondition(propertyTesterId);
	}

	/**
	 * 
	 * @param expectedClass
	 * @return
	 */
	public ICondition newInstanceOfCondition(Class<?> expectedClass) {
		return new InstanceOfCondition(expectedClass);
	}

	/**
	 * 
	 * @param expectedClassName
	 * @return
	 */
	public ICondition newInstanceOfCondition(String expectedClassName) {
		return new InstanceOfCondition(expectedClassName);
	}

	/**
	 * 
	 * @param argName
	 * @param expectedValue
	 * @return
	 */
	public ICondition newExpectedValueCondition(String argName, Object expectedValue) {
		return new ExpectedValueCondition(argName, expectedValue);
	}

	/**
	 * 
	 * @param expectedValues
	 * @return
	 */
	public ICondition newExpectedValueCondition(Map<String, Object> expectedValues) {
		return new ExpectedValueCondition(expectedValues);
	}

}
