package org.origin.common.extensions.condition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExpectedValueCondition implements ICondition {

	protected Map<String, Object> expectedValues = new HashMap<String, Object>();

	public ExpectedValueCondition() {
	}

	/**
	 * 
	 * @param argName
	 * @param expectedValue
	 */
	public ExpectedValueCondition(String argName, Object expectedValue) {
		this.expectedValues.put(argName, expectedValue);
	}

	/**
	 * 
	 * @param expectedValues
	 */
	public ExpectedValueCondition(Map<String, Object> expectedValues) {
		this.expectedValues = expectedValues;
	}

	public synchronized Map<String, Object> getExpectedValues() {
		return this.expectedValues;
	}

	/**
	 * 
	 * @param expectedValues
	 */
	public void setExpectedValues(Map<String, Object> expectedValues) {
		if (expectedValues != null) {
			this.expectedValues.putAll(expectedValues);
		}
	}

	/**
	 * 
	 * @param argName
	 * @param expectedValue
	 */
	public void setExpectedValue(String argName, Object expectedValue) {
		this.expectedValues.put(argName, expectedValue);
	}

	@Override
	public boolean evaluate(Object context, Object source, Object target, Map<String, Object> args) {
		if (args != null) {
			boolean hasExpected = false;
			boolean hasUnexpected = false;
			if (this.expectedValues != null) {
				for (Iterator<String> itor = args.keySet().iterator(); itor.hasNext();) {
					Object argName = itor.next();
					Object orgValue = args.get(argName);
					Object expectedValue = this.expectedValues.get(argName);

					if ((orgValue == null && expectedValue == null) || (orgValue != null && orgValue.equals(expectedValue))) {
						hasExpected = true;
					}
					if ((orgValue == null && expectedValue != null) || (orgValue != null && !orgValue.equals(expectedValue))) {
						hasUnexpected = true;
						break;
					}
				}
			}
			if (hasExpected && !hasUnexpected) {
				return true;
			}
		}
		return false;
	}

}
