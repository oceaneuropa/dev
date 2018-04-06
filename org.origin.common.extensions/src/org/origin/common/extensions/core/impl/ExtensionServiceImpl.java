package org.origin.common.extensions.core.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.extensions.util.ExtensionTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ExtensionServiceImpl implements IExtensionService {

	private static Object lock = new Object[0];
	private static ExtensionServiceImpl instance = null;

	public static ExtensionServiceImpl getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ExtensionServiceImpl();
				}
			}
		}
		return instance;
	}

	protected BundleContext context;
	protected ServiceRegistration<?> serviceRegistration;
	protected ExtensionTracker extensionTracker;

	/**
	 * 
	 * @param context
	 */
	public void start(BundleContext context) {
		this.context = context;

		// Start tracking ProgramExtension services
		this.extensionTracker = new ExtensionTracker();
		this.extensionTracker.start(context);

		// Register as ProgramExtensionService service.
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = context.registerService(IExtensionService.class, this, props);
	}

	/**
	 * 
	 * @param context
	 */
	public void stop(BundleContext context) {
		// Unregister the ProgramExtensionService service.
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		// Stop tracking ProgramExtension services
		if (this.extensionTracker != null) {
			this.extensionTracker.stop(context);
			this.extensionTracker = null;
		}

		this.context = null;
	}

	@Override
	public String[] getExtensionTypeIds() {
		// realm = checkRealm(realm);

		return this.extensionTracker.getExtensionTypeIds();
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public IExtension[] getExtensions() {
		List<IExtension> allExtensions = new ArrayList<IExtension>();
		for (String typeId : getExtensionTypeIds()) {
			IExtension[] currExtensions = getExtensions(typeId);
			if (currExtensions != null) {
				for (IExtension currExtension : currExtensions) {
					allExtensions.add(currExtension);
				}
			}
		}
		return allExtensions.toArray(new IExtension[allExtensions.size()]);
	}

	/**
	 * 
	 * @param typeId
	 * @return
	 */
	@Override
	public IExtension[] getExtensions(String typeId) {
		// realm = checkRealm(realm);
		typeId = checkTypeId(typeId);

		return this.extensionTracker.getExtensions(typeId);
	}

	/**
	 * 
	 * @param typeId
	 * @param extensionId
	 * @return
	 */
	@Override
	public IExtension getExtension(String typeId, String extensionId) {
		// realm = checkRealm(realm);
		typeId = checkTypeId(typeId);

		return this.extensionTracker.getExtension(typeId, extensionId);
	}

	// protected String checkRealm(String realm) {
	// if (realm == null) {
	// realm = "default";
	// }
	// return realm;
	// }

	protected String checkTypeId(String typeId) {
		if (typeId == null) {
			throw new IllegalArgumentException("typeId is null");
		}
		return typeId;
	}

	protected String checkExtensionId(String extensionId) {
		if (extensionId == null) {
			throw new IllegalArgumentException("extensionId is null");
		}
		return extensionId;
	}

}

// /**
// *
// * @param realm
// * @return
// */
// @Override
// public IExtension[] getExtensions(String realm) {
// realm = checkRealm(realm);
//
// return this.extensionTracker.getExtensions(realm);
// }
