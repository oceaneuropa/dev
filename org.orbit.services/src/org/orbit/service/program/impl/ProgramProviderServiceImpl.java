package org.orbit.service.program.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.orbit.service.program.ProgramProvider;
import org.orbit.service.program.ProgramService;
import org.orbit.service.program.ProgramTypeProcessor;
import org.orbit.service.program.ProgramTypeProcessorTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ProgramProviderServiceImpl implements ProgramService {

	private static Object lock = new Object[0];
	private static ProgramProviderServiceImpl instance = null;

	public static ProgramProviderServiceImpl getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ProgramProviderServiceImpl();
				}
			}
		}
		return instance;
	}

	protected BundleContext context;
	protected ProgramTypeProcessorTracker processorTracker;
	protected Map<String, ProgramTypeProcessorImpl> processorsStartedByThisMap = new HashMap<String, ProgramTypeProcessorImpl>();
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param context
	 */
	public void start(BundleContext context) {
		this.context = context;

		// clear data
		this.processorsStartedByThisMap.clear();

		// Start tracking ProgramProviderProcessor services
		this.processorTracker = new ProgramTypeProcessorTracker();
		this.processorTracker.start(context);

		// Register this as a ProgramProviderService service.
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = context.registerService(ProgramService.class, this, props);
	}

	/**
	 * 
	 * @param context
	 */
	public void stop(BundleContext context) {
		// Unregister this as a ProgramProviderService service.
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		// Stop tracking ProgramProviderProcessor services
		if (this.processorTracker != null) {
			this.processorTracker.stop(context);
			this.processorTracker = null;
		}

		// Stop the processors that were started by this class.
		for (Iterator<String> itor = this.processorsStartedByThisMap.keySet().iterator(); itor.hasNext();) {
			String typeId = itor.next();
			ProgramTypeProcessorImpl processor = this.processorsStartedByThisMap.get(typeId);
			processor.stop(context);
		}

		// clear data
		this.processorsStartedByThisMap.clear();

		this.context = null;
	}

	public synchronized ProgramTypeProcessor getProcessor(String programTypeId) {
		ProgramTypeProcessor processor = null;

		if (this.processorTracker != null) {
			// Get process from tracker
			processor = this.processorTracker.getProcessor(programTypeId);

			if (processor == null) {
				if (this.context != null) {
					// Start a processor for the typeId
					ProgramTypeProcessorImpl newProcessor = new ProgramTypeProcessorImpl(programTypeId);
					newProcessor.start(this.context);
					this.processorsStartedByThisMap.put(programTypeId, newProcessor);

					// Get process from tracker again
					processor = this.processorTracker.getProcessor(programTypeId);
				}
			}
		}
		return processor;
	}

	/**
	 * 
	 * @param typeId
	 * @return
	 */
	public ProgramProvider[] getProgramProviders(String typeId) {
		ProgramProvider[] providers = null;
		ProgramTypeProcessor processor = getProcessor(typeId);
		if (processor != null) {
			providers = processor.getProgramProviders();
		}
		if (providers == null) {
			providers = EMPTY_ARRAY;
		}
		return providers;
	}

	/**
	 * 
	 * @param programTypeId
	 * @param programProviderId
	 * @return
	 */
	public ProgramProvider getProgramProvider(String programTypeId, String programProviderId) {
		ProgramProvider provider = null;
		ProgramTypeProcessor processor = getProcessor(programTypeId);
		if (processor != null) {
			provider = processor.getProgramProvider(programProviderId);
		}
		return provider;
	}

}
