/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk.extension.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.impl.ProgramExtensionDescriptiveImpl;
import org.osgi.framework.BundleContext;

public abstract class ProgramExtensions {

	protected BundleContext context;
	protected List<ProgramExtension> programExtensionDescs = new ArrayList<ProgramExtension>();
	protected List<IProgramExtension> extensions = new ArrayList<IProgramExtension>();
	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	/**
	 * Register program extensions.
	 * 
	 * @param context
	 */
	public synchronized void start(BundleContext context) {
		if (this.isStarted.get()) {
			System.err.println(getClass().getName() + " is already started.");
			return;
		}
		this.isStarted.set(true);
		this.context = context;

		createExtensions();

		// // Register IProgramExtensions
		// for (final ProgramExtension programExtensionDesc : this.programExtensionDescs) {
		// IProgramExtension extension = new ProgramExtensionDescriptiveImpl(programExtensionDesc);
		// extension.adapt(ProgramExtension.class, programExtensionDesc);
		//
		// ProgramExtensionRegistry.getInstance().register(context, extension);
		// this.extensions.add(extension);
		// }
	}

	/**
	 * Unregister program extensions.
	 * 
	 * @param context
	 */
	public synchronized void stop(BundleContext context) {
		if (!isStarted()) {
			System.err.println(getClass().getName() + " is not started.");
		}
		if (!this.isStarted.compareAndSet(true, false)) {
			System.err.println(getClass().getName() + " is already stopped.");
			return;
		}

		// Unregister IProgramExtensions
		for (IProgramExtension extension : this.extensions) {
			ProgramExtensionRegistry.getInstance().unregister(context, extension);
		}

		// clear up data
		this.programExtensionDescs.clear();
		this.extensions.clear();

		this.context = null;
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
		return this.programExtensionDescs;
	}

	/**
	 * 
	 * @param programExtensionDesc
	 */
	public synchronized void addExtension(ProgramExtension programExtensionDesc) {
		if (programExtensionDesc == null) {
			return;
		}
		if (programExtensionDesc != null && this.programExtensionDescs.contains(programExtensionDesc)) {
			return;
		}

		String typeId = programExtensionDesc.getTypeId();
		String id = programExtensionDesc.getId();
		if (typeId == null) {
			throw new IllegalArgumentException("typeId is null.");
		}
		if (id == null) {
			throw new IllegalArgumentException("id is null.");
		}

		ProgramExtension programExtensionDescToRemove = null;
		for (final ProgramExtension currProgramExtensionDesc : this.programExtensionDescs) {
			String currTypeId = currProgramExtensionDesc.getTypeId();
			String currId = currProgramExtensionDesc.getId();
			if (typeId.equals(currTypeId) && id.equals(currId)) {
				programExtensionDescToRemove = currProgramExtensionDesc;
				break;
			}
		}

		if (programExtensionDescToRemove != null) {
			removeExtension(programExtensionDescToRemove);
		}

		this.programExtensionDescs.add(programExtensionDesc);

		// Register IProgramExtension dynamically
		IProgramExtension extension = new ProgramExtensionDescriptiveImpl(programExtensionDesc);
		this.extensions.add(extension);
		ProgramExtensionRegistry.getInstance().register(this.context, extension);
	}

	/**
	 * 
	 * @param programExtensionDesc
	 */
	public synchronized void removeExtension(ProgramExtension programExtensionDesc) {
		if (programExtensionDesc == null) {
			return;
		}
		if (programExtensionDesc != null && this.programExtensionDescs.contains(programExtensionDesc)) {
			this.programExtensionDescs.remove(programExtensionDesc);
		}

		// Unregister IProgramExtension dynamically
		IProgramExtension extensionToRemove = null;
		for (IProgramExtension extension : this.extensions) {
			ProgramExtension currProgramExtensionDesc = extension.getAdapter(ProgramExtension.class);
			if (programExtensionDesc.equals(currProgramExtensionDesc)) {
				extensionToRemove = extension;
				break;
			}
		}
		if (extensionToRemove != null) {
			this.extensions.remove(extensionToRemove);
			ProgramExtensionRegistry.getInstance().unregister(this.context, extensionToRemove);
		}
	}

}
