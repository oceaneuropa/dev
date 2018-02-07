package org.orbit.service.program;

import java.util.HashMap;
import java.util.Map;

import org.orbit.service.program.impl.ProgramProviderProxy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ProgramProviderTracker {

	protected String programTypeId;
	protected ServiceTracker<ProgramProvider, ProgramProvider> serviceTracker;
	protected Map<String, ProgramProvider> programProviderMap = new HashMap<String, ProgramProvider>();

	/**
	 * 
	 * @param programTypeId
	 */
	public ProgramProviderTracker(String programTypeId) {
		this.programTypeId = programTypeId;
	}

	public String getProgramTypeId() {
		return this.programTypeId;
	}

	public ProgramProvider[] getProgramProviders() {
		return this.programProviderMap.values().toArray(new ProgramProvider[this.programProviderMap.size()]);
	}

	public ProgramProvider getProgramProvider(String providerId) {
		ProgramProvider provider = null;
		if (providerId != null) {
			provider = this.programProviderMap.get(providerId);
		}
		return provider;
	}

	/**
	 * 
	 * @param context
	 */
	public void start(final BundleContext context) {
		// reset data
		this.programProviderMap.clear();

		// Start tracking ProgramProvider services
		this.serviceTracker = new ServiceTracker<ProgramProvider, ProgramProvider>(context, ProgramProvider.class, new ServiceTrackerCustomizer<ProgramProvider, ProgramProvider>() {
			@Override
			public ProgramProvider addingService(ServiceReference<ProgramProvider> reference) {
				ProgramProvider programProvider = context.getService(reference);
				if (programProvider != null) {
					providerAdded(context, reference);
				}
				return programProvider;
			}

			@Override
			public void modifiedService(ServiceReference<ProgramProvider> reference, ProgramProvider programProvider) {
			}

			@Override
			public void removedService(ServiceReference<ProgramProvider> reference, ProgramProvider programProvider) {
				providerRemoved(context, reference);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * 
	 * @param context
	 */
	public void stop(BundleContext context) {
		// Stop tracking ProgramProvider services
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}

		// reset data
		this.programProviderMap.clear();
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void providerAdded(BundleContext context, ServiceReference<ProgramProvider> reference) {
		String programTypeId = (String) reference.getProperty(ProgramService.PROP_PROGRAM_TYPE_ID);
		String programProviderId = (String) reference.getProperty(ProgramService.PROP_PROGRAM_PROVIDER_ID);
		if (this.programTypeId.equals(programTypeId) && programProviderId != null) {
			ProgramProviderProxy proxy = new ProgramProviderProxy(context, reference, programTypeId, programProviderId);
			this.programProviderMap.put(programProviderId, proxy);
		}
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void providerRemoved(BundleContext context, ServiceReference<ProgramProvider> reference) {
		String programTypeId = (String) reference.getProperty(ProgramService.PROP_PROGRAM_TYPE_ID);
		String programProviderId = (String) reference.getProperty(ProgramService.PROP_PROGRAM_PROVIDER_ID);
		if (this.programTypeId.equals(programTypeId) && programProviderId != null) {
			this.programProviderMap.remove(programProviderId);
		}
	}

}
