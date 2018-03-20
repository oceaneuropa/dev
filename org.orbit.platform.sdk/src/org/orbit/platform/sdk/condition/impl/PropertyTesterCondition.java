package org.orbit.platform.sdk.condition.impl;

import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.condition.ICondition;
import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IPropertyTester;

public class PropertyTesterCondition implements ICondition {

	protected String propertyTesterId;

	public PropertyTesterCondition() {
	}

	/**
	 * 
	 * @param propertyTesterId
	 */
	public PropertyTesterCondition(String propertyTesterId) {
		this.propertyTesterId = propertyTesterId;
	}

	public String getPropertyTesterId() {
		return propertyTesterId;
	}

	/**
	 * 
	 * @param propertyTesterId
	 */
	public void setPropertyTesterId(String propertyTesterId) {
		this.propertyTesterId = propertyTesterId;
	}

	@Override
	public boolean evaluate(Object context, Object source, Object target, Map<String, Object> args) {
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			IProgramExtension extensions = platformContext.getPlatform().getExtensionService().getExtension(IPropertyTester.TYPE_ID, this.propertyTesterId);
			if (extensions != null) {
				IPropertyTester propertyTester = extensions.getInterface(IPropertyTester.class);
				if (propertyTester != null && propertyTester.accept(context, source, target, args)) {
					return true;
				}
			}
		}
		return false;
	}

}
