package org.orbit.service.program;

import java.util.HashMap;
import java.util.Map;

import org.orbit.service.program.impl.ProgramTypeProcessorProxy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ProgramTypeProcessorTracker {

	protected ServiceTracker<ProgramTypeProcessor, ProgramTypeProcessor> serviceTracker;
	protected Map<String, ProgramTypeProcessor> typeIdToProcessorMap = new HashMap<String, ProgramTypeProcessor>();

	/**
	 * 
	 * @param programTypeId
	 * @return
	 */
	public ProgramTypeProcessor getProcessor(String programTypeId) {
		ProgramTypeProcessor processor = this.typeIdToProcessorMap.get(programTypeId);
		return processor;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.typeIdToProcessorMap.clear();

		this.serviceTracker = new ServiceTracker<ProgramTypeProcessor, ProgramTypeProcessor>(bundleContext, ProgramTypeProcessor.class, new ServiceTrackerCustomizer<ProgramTypeProcessor, ProgramTypeProcessor>() {
			@Override
			public ProgramTypeProcessor addingService(ServiceReference<ProgramTypeProcessor> reference) {
				ProgramTypeProcessor processor = bundleContext.getService(reference);
				processorAdded(bundleContext, reference);
				notifyProcessorAdded(processor, 1000);
				return processor;
			}

			@Override
			public void modifiedService(ServiceReference<ProgramTypeProcessor> reference, ProgramTypeProcessor processor) {
			}

			@Override
			public void removedService(ServiceReference<ProgramTypeProcessor> reference, ProgramTypeProcessor processor) {
				processorRemoved(bundleContext, reference);
				notifyProcessorRemoved(processor, 1000);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(final BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}

		this.typeIdToProcessorMap.clear();
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void processorAdded(BundleContext context, ServiceReference<ProgramTypeProcessor> reference) {
		String programTypeId = (String) reference.getProperty(ProgramService.PROP_PROGRAM_TYPE_ID);
		if (programTypeId != null) {
			ProgramTypeProcessorProxy proxy = new ProgramTypeProcessorProxy(context, reference, programTypeId);
			this.typeIdToProcessorMap.put(programTypeId, proxy);
		}
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 */
	protected void processorRemoved(BundleContext context, ServiceReference<ProgramTypeProcessor> reference) {
		String programTypeId = (String) reference.getProperty(ProgramService.PROP_PROGRAM_TYPE_ID);
		if (programTypeId != null) {
			this.typeIdToProcessorMap.remove(programTypeId);
		}
	}

	protected void notifyProcessorAdded(final ProgramTypeProcessor processor, final long delay) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (delay > 0) {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				notifyProcessorAdded(processor);
			}
		});
		t.start();
	}

	protected void notifyProcessorRemoved(final ProgramTypeProcessor processor, final long delay) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (delay > 0) {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				notifyProcessorRemoved(processor);
			}
		});
		t.start();
	}

	protected void notifyProcessorAdded(ProgramTypeProcessor processor) {

	}

	protected void notifyProcessorRemoved(ProgramTypeProcessor processor) {

	}

}
