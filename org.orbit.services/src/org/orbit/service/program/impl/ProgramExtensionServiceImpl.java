package org.orbit.service.program.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.orbit.service.program.IProgramExtension;
import org.orbit.service.program.IProgramExtensionProcessor;
import org.orbit.service.program.IProgramExtensionService;
import org.orbit.service.program.util.ProgramExtensionProcessorTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ProgramExtensionServiceImpl implements IProgramExtensionService {

	private static Object lock = new Object[0];
	private static ProgramExtensionServiceImpl instance = null;

	public static ProgramExtensionServiceImpl getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ProgramExtensionServiceImpl();
				}
			}
		}
		return instance;
	}

	protected BundleContext context;
	protected ProgramExtensionProcessorTracker processorTracker;
	protected Map<String, ProgramExtensionProcessorImpl> processorsStartedByThisMap = new HashMap<String, ProgramExtensionProcessorImpl>();
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param context
	 */
	public void start(BundleContext context) {
		this.context = context;

		// clear up data
		this.processorsStartedByThisMap.clear();

		// Start tracking ProgramExtensionProcessor services
		this.processorTracker = new ProgramExtensionProcessorTracker();
		this.processorTracker.start(context);

		// Register as ProgramExtensionService service.
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = context.registerService(IProgramExtensionService.class, this, props);
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

		// Stop tracking ProgramExtensionProcessor services
		if (this.processorTracker != null) {
			this.processorTracker.stop(context);
			this.processorTracker = null;
		}

		// Stop the processors that were started by this class.
		for (Iterator<String> itor = this.processorsStartedByThisMap.keySet().iterator(); itor.hasNext();) {
			String typeId = itor.next();
			ProgramExtensionProcessorImpl processor = this.processorsStartedByThisMap.get(typeId);
			processor.stop(context);
		}

		// clear up data
		this.processorsStartedByThisMap.clear();

		this.context = null;
	}

	public synchronized IProgramExtensionProcessor getProcessor(String typeId) {
		IProgramExtensionProcessor processor = null;

		if (this.processorTracker != null) {
			// Get process from tracker
			processor = this.processorTracker.getProcessor(typeId);

			if (processor == null) {
				if (this.context != null) {
					// Start a processor for the typeId
					ProgramExtensionProcessorImpl newProcessor = new ProgramExtensionProcessorImpl(typeId);
					newProcessor.start(this.context);
					this.processorsStartedByThisMap.put(typeId, newProcessor);

					// Get process from tracker again
					processor = this.processorTracker.getProcessor(typeId);
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
	public IProgramExtension[] getProgramExtensions(String typeId) {
		IProgramExtension[] programExtensions = null;
		IProgramExtensionProcessor processor = getProcessor(typeId);
		if (processor != null) {
			programExtensions = processor.getProgramExtensions();
		}
		if (programExtensions == null) {
			programExtensions = EMPTY_ARRAY;
		}
		return programExtensions;
	}

	/**
	 * 
	 * @param typeId
	 * @param extensionId
	 * @return
	 */
	public IProgramExtension getProgramExtension(String typeId, String extensionId) {
		IProgramExtension programExtension = null;
		IProgramExtensionProcessor processor = getProcessor(typeId);
		if (processor != null) {
			programExtension = processor.getProgramExtension(extensionId);
		}
		return programExtension;
	}

}
