package org.orbit.platform.sdk.extension.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.orbit.platform.sdk.extension.impl.ProgramExtensionProxy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ProgramExtensionTracker {

	public static interface ProgramExtensionFilter {
		/**
		 * 
		 * @param reference
		 * @return
		 */
		boolean accept(ServiceReference<IProgramExtension> reference);
	}

	public static interface ProgramExtensionListener {
		/**
		 * 
		 * @param extension
		 */
		void extensionAdded(IProgramExtension extension);

		/**
		 * 
		 * @param extension
		 */
		void extensionRemoved(IProgramExtension extension);
	}

	public static class ProgramExtensionListenerSupport {
		protected List<ProgramExtensionListener> listeners = new ArrayList<ProgramExtensionListener>();

		public void addListener(final ProgramExtensionListener listener) {
			if (listener != null && !this.listeners.contains(listener)) {
				this.listeners.add(listener);
			}
		}

		public void removeListener(final ProgramExtensionListener listener) {
			if (listener != null && this.listeners.contains(listener)) {
				this.listeners.remove(listener);
			}
		}

		public void notifyExtensionAdded(final IProgramExtension extension) {
			for (ProgramExtensionListener listener : this.listeners) {
				try {
					listener.extensionAdded(extension);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public void notifyExtensionRemoved(final IProgramExtension extension) {
			for (ProgramExtensionListener listener : this.listeners) {
				try {
					listener.extensionRemoved(extension);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected ServiceTracker<IProgramExtension, IProgramExtension> serviceTracker;
	protected ProgramExtensionFilter filter;
	protected ProgramExtensionListenerSupport listenerSupport;
	protected Map<String, IProgramExtension> extensionIdToProgramExtensionMap = new HashMap<String, IProgramExtension>();

	public ProgramExtensionTracker() {
		this.listenerSupport = new ProgramExtensionListenerSupport();
	}

	public ProgramExtensionFilter getFilter() {
		return this.filter;
	}

	public void setFilter(ProgramExtensionFilter filter) {
		this.filter = filter;
	}

	public void addListener(ProgramExtensionListener listener) {
		this.listenerSupport.addListener(listener);
	}

	public void removeListener(ProgramExtensionListener listener) {
		this.listenerSupport.removeListener(listener);
	}

	public void notifyExtensionAdded(IProgramExtension extension) {
		this.listenerSupport.notifyExtensionAdded(extension);
	}

	public void notifyExtensionRemoved(IProgramExtension extension) {
		this.listenerSupport.notifyExtensionRemoved(extension);
	}

	public IProgramExtension[] getProgramExtensions() {
		return this.extensionIdToProgramExtensionMap.values().toArray(new IProgramExtension[this.extensionIdToProgramExtensionMap.size()]);
	}

	public IProgramExtension getProgramExtension(String extensionId) {
		IProgramExtension programExtension = null;
		if (extensionId != null) {
			programExtension = this.extensionIdToProgramExtensionMap.get(extensionId);
		}
		return programExtension;
	}

	/**
	 * 
	 * @param context
	 */
	public void start(final BundleContext context) {
		// clean up data
		this.extensionIdToProgramExtensionMap.clear();

		// Start tracking IProgramExtension services
		this.serviceTracker = new ServiceTracker<IProgramExtension, IProgramExtension>(context, IProgramExtension.class, new ServiceTrackerCustomizer<IProgramExtension, IProgramExtension>() {
			@Override
			public IProgramExtension addingService(ServiceReference<IProgramExtension> reference) {
				IProgramExtension programExtension = context.getService(reference);
				if (programExtension != null) {
					serviceAdded(context, reference);
				}
				return programExtension;
			}

			@Override
			public void modifiedService(ServiceReference<IProgramExtension> reference, IProgramExtension programExtension) {
			}

			@Override
			public void removedService(ServiceReference<IProgramExtension> reference, IProgramExtension programExtension) {
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
		this.extensionIdToProgramExtensionMap.clear();
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void serviceAdded(BundleContext context, ServiceReference<IProgramExtension> reference) {
		String typeId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_TYPE_ID);
		String extensionId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_ID);

		boolean isAccepted = false;
		if (this.filter == null || this.filter.accept(reference)) {
			isAccepted = true;
		}

		if (isAccepted) {
			final ProgramExtensionProxy proxy = new ProgramExtensionProxy(context, reference, typeId, extensionId);
			this.extensionIdToProgramExtensionMap.put(extensionId, proxy);

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
	protected void serviceRemoved(BundleContext context, ServiceReference<IProgramExtension> reference) {
		String extensionId = (String) reference.getProperty(IProgramExtensionService.PROP_EXTENSION_ID);

		boolean isAccepted = false;
		if (this.filter == null || this.filter.accept(reference)) {
			isAccepted = true;
		}

		if (isAccepted) {
			final IProgramExtension extension = this.extensionIdToProgramExtensionMap.remove(extensionId);

			if (extension != null) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						notifyExtensionRemoved(extension);
					}
				}).start();
			}
		}
	}

}
