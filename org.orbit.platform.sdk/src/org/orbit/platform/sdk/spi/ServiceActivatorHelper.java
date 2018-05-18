package org.orbit.platform.sdk.spi;

import org.orbit.platform.sdk.IPlatformContext;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.condition.ICondition;

public class ServiceActivatorHelper {

	public static ServiceActivatorHelper INSTANCE = new ServiceActivatorHelper();

	/**
	 * 
	 * @param context
	 * @param desc
	 * @return
	 */
	public boolean isServiceActivatorAutoStart(IPlatformContext context, InterfaceDescription desc) {
		boolean isAutoStart = false;
		if (desc != null) {
			isAutoStart = desc.isAutoStart();
			if (!isAutoStart) {
				ICondition condition = desc.getTriggerCondition();
				if (condition != null) {
					isAutoStart = condition.evaluate(context, desc, null, null);
				}
			}
		}
		return isAutoStart;
	}

}
