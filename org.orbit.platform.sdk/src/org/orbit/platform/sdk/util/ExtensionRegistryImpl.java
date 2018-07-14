package org.orbit.platform.sdk.util;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.util.ExtensionListener;
import org.origin.common.extensions.util.ExtensionTracker;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtensionRegistryImpl implements ExtensionRegistry, ExtensionListener {

	protected static Logger LOG = LoggerFactory.getLogger(ExtensionRegistryImpl.class);

	protected static IExtension[] EMPTY_EXTENSIONS = new IExtension[0];

	public static ExtensionRegistryImpl INSTANCE = new ExtensionRegistryImpl();

	protected BundleContext bundleContext;
	protected ExtensionTracker extensionTracker;
	protected Map<String, Map<String, IExtension>> typeIdToExtensionsMap = new HashMap<String, Map<String, IExtension>>();

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		// Start tracking extensions
		this.extensionTracker = new ExtensionTracker();
		this.extensionTracker.addListener(this);
		this.extensionTracker.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// Stop tracking extensions
		if (this.extensionTracker != null) {
			this.extensionTracker.stop(bundleContext);
			this.extensionTracker.removeListener(this);
			this.extensionTracker = null;
		}

		this.typeIdToExtensionsMap.clear();

		this.bundleContext = null;
	}

	protected String getTypeId(IExtension extension) {
		String typeId = null;
		if (extension != null) {
			typeId = extension.getTypeId();
		}
		if (typeId == null) {
			typeId = "";
		}
		return typeId;
	}

	protected String getId(IExtension extension) {
		String id = null;
		if (extension != null) {
			id = extension.getId();
		}
		if (id == null) {
			id = "";
		}
		return id;
	}

	@Override
	public synchronized void extensionAdded(IExtension extension) {
		if (extension == null) {
			return;
		}
		String typeId = getTypeId(extension);
		String id = getId(extension);

		Map<String, IExtension> idToExtensionMap = this.typeIdToExtensionsMap.get(typeId);
		if (idToExtensionMap == null) {
			idToExtensionMap = new HashMap<String, IExtension>();
			this.typeIdToExtensionsMap.put(typeId, idToExtensionMap);
		}

		idToExtensionMap.put(id, extension);
	}

	@Override
	public synchronized void extensionRemoved(IExtension extension) {
		if (extension == null) {
			return;
		}

		String typeId = getTypeId(extension);
		String id = getId(extension);

		Map<String, IExtension> idToExtensionMap = this.typeIdToExtensionsMap.get(typeId);
		if (idToExtensionMap != null) {
			idToExtensionMap.remove(id);
		}
	}

	/**
	 * 
	 * @param typeId
	 * @return
	 */
	public synchronized IExtension[] getExtensions(String typeId) {
		IExtension[] extensions = null;
		if (typeId != null) {
			Map<String, IExtension> idToExtensionMap = this.typeIdToExtensionsMap.get(typeId);
			if (idToExtensionMap != null) {
				idToExtensionMap.values().toArray(new IExtension[idToExtensionMap.size()]);
			}
		}
		if (extensions == null) {
			extensions = EMPTY_EXTENSIONS;
		}
		return extensions;
	}

	/**
	 * 
	 * @param typeId
	 * @param id
	 * @return
	 */
	public synchronized IExtension getExtension(String typeId, String id) {
		IExtension extension = null;
		if (typeId != null && id != null) {
			Map<String, IExtension> idToExtensionMap = this.typeIdToExtensionsMap.get(typeId);
			if (idToExtensionMap != null) {
				extension = idToExtensionMap.get(id);
			}
		}
		return extension;
	}

}
