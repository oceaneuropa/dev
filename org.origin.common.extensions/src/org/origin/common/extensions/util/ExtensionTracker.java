package org.origin.common.extensions.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.extensions.core.impl.ExtensionProxy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ExtensionTracker {

	protected ServiceTracker<IExtension, IExtension> serviceTracker;
	protected ExtensionFilter filter;
	protected ExtensionListenerSupport listenerSupport = new ExtensionListenerSupport();
	protected Map<String, Map<String, List<IExtension>>> realmToExtensionsMap = new HashMap<String, Map<String, List<IExtension>>>();

	public ExtensionTracker() {
	}

	public ExtensionFilter getFilter() {
		return this.filter;
	}

	public void setFilter(ExtensionFilter filter) {
		this.filter = filter;
	}

	protected String checkRealm(String realm) {
		if (realm == null) {
			realm = "default";
		}
		return realm;
	}

	protected String checkExtensionTypeId(String extensionTypeId) {
		if (extensionTypeId == null) {
			throw new IllegalArgumentException("extensionTypeId property is not set.");
		}
		return extensionTypeId;
	}

	protected String checkExtensionId(String extensionId) {
		if (extensionId == null) {
			throw new IllegalArgumentException("extensionId property is not set.");
		}
		return extensionId;
	}

	/**
	 * 
	 * @param realm
	 * @return
	 */
	public String[] getExtensionTypeIds(String realm) {
		realm = checkRealm(realm);
		List<String> typeIds = new ArrayList<String>();
		Map<String, List<IExtension>> extensionsMap = this.realmToExtensionsMap.get(realm);
		if (extensionsMap != null) {
			for (Iterator<String> itor = extensionsMap.keySet().iterator(); itor.hasNext();) {
				String typeId = itor.next();
				if (!typeIds.contains(typeId)) {
					typeIds.add(typeId);
				}
			}
		}
		return typeIds.toArray(new String[typeIds.size()]);
	}

	/**
	 * 
	 * @param realm
	 * @return
	 */
	public IExtension[] getExtensions(String realm) {
		realm = checkRealm(realm);

		List<IExtension> resultExtensions = new ArrayList<IExtension>();
		if (realm != null) {
			Map<String, List<IExtension>> extensionsMap = this.realmToExtensionsMap.get(realm);

			for (Iterator<String> itor = extensionsMap.keySet().iterator(); itor.hasNext();) {
				String typeId = itor.next();
				List<IExtension> currExtensions = extensionsMap.get(typeId);
				if (currExtensions != null && !currExtensions.isEmpty()) {
					resultExtensions.addAll(currExtensions);
				}
			}
		}
		return resultExtensions.toArray(new IExtension[resultExtensions.size()]);
	}

	/**
	 * 
	 * @param realm
	 * @param typeId
	 * @return
	 */
	public IExtension[] getExtensions(String realm, String typeId) {
		realm = checkRealm(realm);
		typeId = checkExtensionTypeId(typeId);

		List<IExtension> resultExtensions = new ArrayList<IExtension>();
		if (realm != null && typeId != null) {
			Map<String, List<IExtension>> extensionsMap = this.realmToExtensionsMap.get(realm);

			for (Iterator<String> itor = extensionsMap.keySet().iterator(); itor.hasNext();) {
				String currTypeId = itor.next();
				if (typeId.equals(currTypeId)) {
					List<IExtension> currExtensions = extensionsMap.get(typeId);
					if (currExtensions != null && !currExtensions.isEmpty()) {
						resultExtensions.addAll(currExtensions);
					}
				}
			}
		}
		return resultExtensions.toArray(new IExtension[resultExtensions.size()]);
	}

	/**
	 * 
	 * @param realm
	 * @param typeId
	 * @param extensionId
	 * @return
	 */
	public IExtension getExtension(String realm, String typeId, String extensionId) {
		realm = checkRealm(realm);
		typeId = checkExtensionTypeId(typeId);
		extensionId = checkExtensionId(extensionId);

		if (realm != null && typeId != null && extensionId != null) {
			Map<String, List<IExtension>> extensionsMap = this.realmToExtensionsMap.get(realm);

			for (Iterator<String> itor = extensionsMap.keySet().iterator(); itor.hasNext();) {
				String currTypeId = itor.next();

				if (typeId.equals(currTypeId)) {
					List<IExtension> currExtensions = extensionsMap.get(typeId);

					if (currExtensions != null) {
						for (IExtension currExtension : currExtensions) {
							String currExtensionId = currExtension.getId();
							if (extensionId.equals(currExtensionId)) {
								return currExtension;
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param context
	 */
	public void start(final BundleContext context) {
		// clean up data
		this.realmToExtensionsMap.clear();

		// Start tracking IProgramExtension services
		this.serviceTracker = new ServiceTracker<IExtension, IExtension>(context, IExtension.class, new ServiceTrackerCustomizer<IExtension, IExtension>() {
			@Override
			public IExtension addingService(ServiceReference<IExtension> reference) {
				IExtension programExtension = context.getService(reference);
				if (programExtension != null) {
					serviceAdded(context, reference);
				}
				return programExtension;
			}

			@Override
			public void modifiedService(ServiceReference<IExtension> reference, IExtension programExtension) {
			}

			@Override
			public void removedService(ServiceReference<IExtension> reference, IExtension programExtension) {
				serviceRemoved(context, reference);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * 
	 * @param context
	 */
	public void stop(BundleContext context) {
		// Stop tracking IProgramExtension services
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}

		// clean up data
		this.realmToExtensionsMap.clear();
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void serviceAdded(BundleContext context, ServiceReference<IExtension> reference) {
		String realm = (String) reference.getProperty(IExtensionService.PROP_REALM);
		String typeId = (String) reference.getProperty(IExtensionService.PROP_EXTENSION_TYPE_ID);
		String extensionId = (String) reference.getProperty(IExtensionService.PROP_EXTENSION_ID);

		realm = checkRealm(realm);
		typeId = checkExtensionTypeId(typeId);
		extensionId = checkExtensionId(extensionId);

		boolean isAccepted = false;
		if (this.filter == null || this.filter.accept(reference)) {
			isAccepted = true;
		}

		if (isAccepted) {
			final ExtensionProxy proxy = new ExtensionProxy(context, reference, realm, typeId, extensionId);

			Map<String, List<IExtension>> extensionsMap = this.realmToExtensionsMap.get(realm);
			if (extensionsMap == null) {
				extensionsMap = new TreeMap<String, List<IExtension>>();
				this.realmToExtensionsMap.put(realm, extensionsMap);
			}
			List<IExtension> extensions = extensionsMap.get(typeId);
			if (extensions == null) {
				extensions = new ArrayList<IExtension>();
				extensionsMap.put(typeId, extensions);
			}
			if (!extensions.contains(proxy)) {
				extensions.add(proxy);
			}

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					notifyExtensionAdded(proxy);
				}
			}).start();
		}
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void serviceRemoved(BundleContext context, ServiceReference<IExtension> reference) {
		String realm = (String) reference.getProperty(IExtensionService.PROP_REALM);
		String typeId = (String) reference.getProperty(IExtensionService.PROP_EXTENSION_TYPE_ID);
		String extensionId = (String) reference.getProperty(IExtensionService.PROP_EXTENSION_ID);

		realm = checkRealm(realm);
		typeId = checkExtensionTypeId(typeId);
		extensionId = checkExtensionId(extensionId);

		boolean isAccepted = false;
		if (this.filter == null || this.filter.accept(reference)) {
			isAccepted = true;
		}

		if (isAccepted) {
			Map<String, List<IExtension>> extensionsMap = this.realmToExtensionsMap.get(realm);
			if (extensionsMap != null) {
				List<IExtension> extensions = extensionsMap.get(typeId);
				if (extensions != null) {
					IExtension extensionToRemove = null;
					for (IExtension currExtension : extensions) {
						String currExtensionId = currExtension.getId();
						if (extensionId.equals(currExtensionId)) {
							extensionToRemove = currExtension;
							break;
						}
					}
					if (extensionToRemove != null) {
						extensions.remove(extensionToRemove);
						if (extensions.isEmpty()) {
							extensionsMap.remove(typeId);
						}

						final IExtension removedExtension = extensionToRemove;
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								notifyExtensionRemoved(removedExtension);
							}
						}).start();
					}
				}
			}
		}
	}

	public void addListener(ExtensionListener listener) {
		this.listenerSupport.addListener(listener);
	}

	public void removeListener(ExtensionListener listener) {
		this.listenerSupport.removeListener(listener);
	}

	public void notifyExtensionAdded(IExtension extension) {
		this.listenerSupport.notifyExtensionAdded(extension);
	}

	public void notifyExtensionRemoved(IExtension extension) {
		this.listenerSupport.notifyExtensionRemoved(extension);
	}

}
