/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk.extension.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IProgramExtensionProcessor;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.orbit.platform.sdk.extension.util.ProgramExtensionProcessorTracker;
import org.orbit.platform.sdk.extension.util.ProgramExtensionTypeTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ProgramExtensionServiceImpl implements IProgramExtensionService {

	private static Object lock = new Object[0];
	private static ProgramExtensionServiceImpl instance = null;
	private static String[] EMPTY_EXTENSION_TYPE_IDS = new String[0];

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
	protected ProgramExtensionTypeTracker extensionTypeTracker;
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

		// Start tracking all IProgramExtension services for getting all unique extension type ids.
		this.extensionTypeTracker = new ProgramExtensionTypeTracker();
		this.extensionTypeTracker.start(context);

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

		// Stop tracking all IProgramExtension services for getting all unique extension type ids.
		if (this.extensionTypeTracker != null) {
			this.extensionTypeTracker.stop(context);
			this.extensionTypeTracker = null;
		}

		// clear up data
		this.processorsStartedByThisMap.clear();

		this.context = null;
	}

	@Override
	public String[] getExtensionTypeIds() {
		String[] extensionTypeIds = null;
		if (this.extensionTypeTracker != null) {
			Set<String> extensionTypeIdSet = this.extensionTypeTracker.getExtensionTypeIds();
			if (extensionTypeIdSet != null) {
				extensionTypeIds = extensionTypeIdSet.toArray(new String[extensionTypeIdSet.size()]);
			}
		}
		if (extensionTypeIds == null) {
			extensionTypeIds = EMPTY_EXTENSION_TYPE_IDS;
		}
		return extensionTypeIds;
	}

	/**
	 * 
	 * @param typeId
	 * @return
	 */
	@Override
	public IProgramExtension[] getExtensions() {
		List<IProgramExtension> programExtensions = new ArrayList<IProgramExtension>();
		for (String typeId : getExtensionTypeIds()) {
			IProgramExtensionProcessor processor = getProcessor(typeId);
			if (processor != null) {
				IProgramExtension[] currExtensions = processor.getExtensions();
				if (currExtensions != null) {
					for (IProgramExtension currExtension : currExtensions) {
						programExtensions.add(currExtension);
					}
				}
			}
		}
		return programExtensions.toArray(new IProgramExtension[programExtensions.size()]);
	}

	/**
	 * 
	 * @param typeId
	 * @return
	 */
	@Override
	public IProgramExtension[] getExtensions(String typeId) {
		typeId = checkTypeId(typeId);

		IProgramExtension[] extensions = null;
		IProgramExtensionProcessor processor = getProcessor(typeId);
		if (processor != null) {
			extensions = processor.getExtensions();
		}
		if (extensions == null) {
			extensions = EMPTY_ARRAY;
		}
		return extensions;
	}

	/**
	 * 
	 * @param typeId
	 * @param extensionId
	 * @return
	 */
	@Override
	public IProgramExtension getExtension(String typeId, String extensionId) {
		typeId = checkTypeId(typeId);

		IProgramExtension programExtension = null;
		IProgramExtensionProcessor processor = getProcessor(typeId);
		if (processor != null) {
			programExtension = processor.getExtension(extensionId);
		}
		return programExtension;
	}

	/**
	 * 
	 * @param typeId
	 * @return
	 */
	protected synchronized IProgramExtensionProcessor getProcessor(String typeId) {
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
