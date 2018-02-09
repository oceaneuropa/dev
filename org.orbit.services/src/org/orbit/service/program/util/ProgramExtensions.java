package org.orbit.service.program.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.service.program.IProgramExtension;
import org.orbit.service.program.impl.ProgramExtensionDescriptiveImpl;
import org.osgi.framework.BundleContext;

public abstract class ProgramExtensions {

	protected List<ProgramExtension> extensionModels = new ArrayList<ProgramExtension>();
	protected List<IProgramExtension> extensions = new ArrayList<IProgramExtension>();
	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	/**
	 * Register program extensions.
	 * 
	 * @param context
	 */
	public void start(BundleContext context) {
		if (this.isStarted.get()) {
			System.err.println(getClass().getName() + " is already started.");
			return;
		}
		this.isStarted.set(true);

		createExtensions();

		for (final ProgramExtension extensionModel : this.extensionModels) {
			IProgramExtension extension = new ProgramExtensionDescriptiveImpl(extensionModel);
			this.extensions.add(extension);
		}

		// Register the ProgramExtesion services.
		for (IProgramExtension extension : this.extensions) {
			// extension.register(context);
			ProgramExtensionRegistry.getInstance().register(context, extension);
		}
	}

	/**
	 * Unregister program extensions.
	 * 
	 * @param context
	 */
	public void stop(BundleContext context) {
		if (!isStarted()) {
			System.err.println(getClass().getName() + " is not started.");
		}
		if (!this.isStarted.compareAndSet(true, false)) {
			System.err.println(getClass().getName() + " is already stopped.");
			return;
		}

		// Unregister the ProgramExtesion services.
		for (IProgramExtension extension : this.extensions) {
			// extension.unregister(context);
			ProgramExtensionRegistry.getInstance().unregister(context, extension);
		}

		// clear up data
		this.extensionModels.clear();
		this.extensions.clear();
	}

	public synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	public void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException(getClass().getSimpleName() + " is not started.");
		}
	}

	public abstract void createExtensions();

	/**
	 * 
	 * @return
	 */
	public List<ProgramExtension> getExtensionDescriptions() {
		return this.extensionModels;
	}

	/**
	 * 
	 * @param extensionModel
	 */
	public void add(ProgramExtension extensionModel) {
		if (extensionModel != null && this.extensionModels.contains(extensionModel)) {
			return;
		}

		String typeId = extensionModel.getTypeId();
		String id = extensionModel.getId();
		if (typeId == null) {
			throw new IllegalArgumentException("typeId is null.");
		}
		if (id == null) {
			throw new IllegalArgumentException("id is null.");
		}

		ProgramExtension extensionModelToRemove = null;
		for (final ProgramExtension currExtensionModel : this.extensionModels) {
			String currTypeId = currExtensionModel.getTypeId();
			String currId = currExtensionModel.getId();
			if (typeId.equals(currTypeId) && id.equals(currId)) {
				extensionModelToRemove = currExtensionModel;
				break;
			}
		}
		if (extensionModelToRemove != null) {
			this.extensionModels.remove(extensionModelToRemove);
		}

		this.extensionModels.add(extensionModel);
	}

}
