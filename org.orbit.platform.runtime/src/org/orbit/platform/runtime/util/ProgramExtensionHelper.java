package org.orbit.platform.runtime.util;

import org.orbit.platform.sdk.IPlatformContext;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.Parameter;
import org.origin.common.extensions.condition.ICondition;
import org.origin.common.extensions.core.IExtension;

public class ProgramExtensionHelper {

	public static ProgramExtensionHelper INSTANCE = new ProgramExtensionHelper();

	/**
	 * 
	 * @param extension
	 * @param clazz
	 * @return
	 */
	public String getName(IExtension extension, Class<?> clazz) {
		String name = null;
		if (extension != null && clazz != null) {
			InterfaceDescription desc = extension.getInterfaceDescription(clazz);
			if (desc != null) {
				name = desc.getName();
			}
		}
		return name;
	}

	/**
	 * 
	 * @param extension
	 * @param clazz
	 * @return
	 */
	public String getName(IExtension extension, Object object) {
		String name = null;
		if (extension != null && object != null) {
			InterfaceDescription desc = extension.getInterfaceDescription(object);
			if (desc != null) {
				name = desc.getName();
			}
		}
		return name;
	}

	/**
	 * 
	 * @param extension
	 * @param clazz
	 * @return
	 */
	public boolean isSingleton(IExtension extension, Class<?> clazz) {
		boolean isSingleton = false;
		if (extension != null && clazz != null) {
			InterfaceDescription desc = extension.getInterfaceDescription(clazz);
			if (desc != null) {
				isSingleton = desc.isSingleton();
			}
		}
		return isSingleton;
	}

	/**
	 * 
	 * @param extension
	 * @param object
	 * @return
	 */
	public boolean isSingleton(IExtension extension, Object object) {
		boolean isSingleton = false;
		if (extension != null && object != null) {
			InterfaceDescription desc = extension.getInterfaceDescription(object);
			if (desc != null) {
				isSingleton = desc.isSingleton();
			}
		}
		return isSingleton;
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 */
	public String toParametersString(Parameter[] parameters) {
		String string = "";
		if (parameters != null) {
			for (int index = 0; index < parameters.length; index++) {
				Parameter parameter = parameters[index];
				if (index > 0) {
					string = string + ", ";
				}
				string = string + parameter.getLabel();
				// string += "\r";
			}
		}
		return string;
	}

}

// /**
// *
// * @param extension
// * @param clazz
// * @return
// */
// public boolean isAutoStart(IProgramExtension extension, Class<?> clazz) {
// boolean isAutoStart = false;
// if (extension != null && clazz != null) {
// InterfaceDescription desc = extension.getInterfaceDescription(clazz);
// if (desc != null) {
// isAutoStart = desc.isAutoStart();
// }
// }
// return isAutoStart;
// }

// /**
// *
// * @param extension
// * @param object
// * @return
// */
// public boolean isAutoStart(Object context, IProgramExtension extension, Object object) {
// boolean isAutoStart = false;
// if (extension != null && object != null) {
// InterfaceDescription desc = extension.getInterfaceDescription(object);
// if (desc != null) {
// isAutoStart = desc.isAutoStart();
//
// if (!isAutoStart) {
// ICondition triggerCondition = desc.getTriggerCondition();
// if (triggerCondition != null) {
// boolean succeed = triggerCondition.evaluate(context, object, null, null);
// if (succeed) {
// isAutoStart = true;
// }
// }
// }
// }
// }
// return isAutoStart;
// }
