package org.origin.common.extensions.condition;

import java.util.Map;

import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.extensions.util.ExtensionServiceHelper;

public class PropertyTesterCondition implements ICondition {

	// protected String realm;
	protected String propertyTesterId;

	/**
	 * 
	 * @param realm
	 */
	public PropertyTesterCondition() {
	}

	/**
	 * 
	 * @param propertyTesterId
	 */
	public PropertyTesterCondition(String propertyTesterId) {
		// this.realm = realm;
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
		IExtensionService extensionService = ExtensionServiceHelper.INSTANCE.getExtensionService(context);
		if (extensionService != null) {
			IExtension extensions = extensionService.getExtension(IPropertyTester.TYPE_ID, this.propertyTesterId);
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
