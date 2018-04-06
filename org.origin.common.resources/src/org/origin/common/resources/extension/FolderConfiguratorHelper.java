package org.origin.common.resources.extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.condition.ICondition;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.extensions.util.ExtensionServiceHelper;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.IWorkspace;

public class FolderConfiguratorHelper {

	public static FolderConfiguratorHelper INSTANCE = new FolderConfiguratorHelper();

	/**
	 * 
	 * @param context
	 * @param workspace
	 * @param folder
	 * @throws IOException
	 */
	public void preConfigureFolder(Object context, IWorkspace workspace, IFolder folder) throws IOException {
		List<ResourceConfigurator> configurators = getFolderConfigurators(context, folder, "preconfig");
		for (ResourceConfigurator configurator : configurators) {
			configurator.configure(workspace, folder);
		}
	}

	/**
	 * 
	 * @param context
	 * @param folder
	 * @param action
	 * @return
	 */
	public List<ResourceConfigurator> getFolderConfigurators(Object context, IFolder folder, String action) {
		List<ResourceConfigurator> configurators = new ArrayList<ResourceConfigurator>();
		IExtensionService extensionService = ExtensionServiceHelper.INSTANCE.getExtensionService(context);
		if (extensionService != null) {
			IExtension[] extensions = extensionService.getExtensions(ResourceConfigurator.TYPE_ID);
			for (IExtension extension : extensions) {
				if (isFolderConfiguratorTriggered(context, extension, folder, action)) {
					ResourceConfigurator configurator = extension.getInterface(ResourceConfigurator.class);
					if (configurator != null && !configurators.contains(configurator)) {
						configurators.add(configurator);
					}
				}
			}
		}
		return configurators;
	}

	/**
	 * 
	 * @param context
	 * @param extension
	 * @param folder
	 * @param action
	 * @return
	 */
	public boolean isFolderConfiguratorTriggered(Object context, IExtension extension, IFolder folder, String action) {
		boolean isTriggered = false;
		if (extension != null) {
			InterfaceDescription desc = extension.getInterfaceDescription(ResourceConfigurator.class);
			if (desc != null) {
				ICondition condition = desc.getTriggerCondition();
				if (condition != null) {
					Map<String, Object> args = new HashMap<String, Object>();
					args.put("action", action);
					isTriggered = condition.evaluate(context, folder, null, args);
				}
			}
		}
		return isTriggered;
	}

}
