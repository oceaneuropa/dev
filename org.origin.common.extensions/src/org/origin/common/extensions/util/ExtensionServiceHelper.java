package org.origin.common.extensions.util;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.extensions.Activator;
import org.origin.common.extensions.core.IExtensionService;

public class ExtensionServiceHelper {

	public static ExtensionServiceHelper INSTANCE = new ExtensionServiceHelper();

	/**
	 * 
	 * @param context
	 * @return
	 */
	public IExtensionService getExtensionService(Object context) {
		IExtensionService extensionService = null;

		if (context != null) {
			if (extensionService == null) {
				if (context instanceof IExtensionService) {
					extensionService = (IExtensionService) context;
				}
			}

			if (extensionService == null) {
				if (context instanceof IAdaptable) {
					extensionService = ((IAdaptable) context).getAdapter(IExtensionService.class);
				}
			}

			if (extensionService == null) {
				if (context instanceof ExtensionServiceAware) {
					extensionService = ((ExtensionServiceAware) context).getExtensionService();
				}
			}
		}

		if (extensionService == null) {
			extensionService = Activator.getDefault().getExtensionService();
		}

		return extensionService;
	}

}
