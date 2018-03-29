package org.origin.common.extensions.condition;

import java.util.Map;

public class InstanceOfCondition implements ICondition {

	protected Class<?> expectedClass;
	protected String expectedClassName;

	public InstanceOfCondition() {
	}

	public InstanceOfCondition(Class<?> expectedClass) {
		this.expectedClass = expectedClass;
	}

	public InstanceOfCondition(String expectedClassName) {
		this.expectedClassName = expectedClassName;
	}

	public String getExpectedClassName() {
		return this.expectedClassName;
	}

	public void setExpectedClassName(String expectedClassName) {
		this.expectedClassName = expectedClassName;
	}

	public void setExpectedClass(Class<?> expectedClass) {
		this.expectedClass = expectedClass;
	}

	public Class<?> getExpectedClass() {
		return this.expectedClass;
	}

	@Override
	public boolean evaluate(Object context, Object source, Object target, Map<String, Object> args) {
		Class<?> expectedClass = getExpectedClass();
		if (expectedClass == null) {
			String expectedClassName = getExpectedClassName();
			if (expectedClassName != null) {
				try {
					expectedClass = Class.forName(expectedClassName);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		if (source != null && expectedClass != null && expectedClass.isAssignableFrom(source.getClass())) {
			return true;
		}
		return false;
	}

}
