/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk.extension.desc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.impl.ProgramExtensionDescriptiveImpl;
import org.orbit.platform.sdk.extension.util.ProgramExtensionRegistry;
import org.osgi.framework.BundleContext;

public abstract class ProgramExtensions {

	protected BundleContext context;
	protected List<ProgramExtension> extensionDescs = new ArrayList<ProgramExtension>();
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
		this.extensionDescs.clear();
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
		return this.extensionDescs;
	}

	/**
	 * 
	 * @param extensionDesc
	 */
	public synchronized void addExtension(ProgramExtension extensionDesc) {
		if (extensionDesc == null) {
			return;
		}
		if (extensionDesc != null && this.extensionDescs.contains(extensionDesc)) {
			return;
		}

		String typeId = extensionDesc.getTypeId();
		String id = extensionDesc.getId();
		if (typeId == null) {
			throw new IllegalArgumentException("typeId is null.");
		}
		if (id == null) {
			throw new IllegalArgumentException("id is null.");
		}

		ProgramExtension extensionDescToRemove = null;
		for (final ProgramExtension currExtensionDesc : this.extensionDescs) {
			String currTypeId = currExtensionDesc.getTypeId();
			String currId = currExtensionDesc.getId();
			if (typeId.equals(currTypeId) && id.equals(currId)) {
				extensionDescToRemove = currExtensionDesc;
				break;
			}
		}

		if (extensionDescToRemove != null) {
			removeExtension(extensionDescToRemove);
		}

		this.extensionDescs.add(extensionDesc);

		// Register IProgramExtension service
		IProgramExtension extension = new ProgramExtensionDescriptiveImpl(extensionDesc);
		extension.adapt(ProgramExtension.class, extensionDesc); // set (adapt) ProgramExtension in the IProgramExtension

		this.extensions.add(extension);
		ProgramExtensionRegistry.getInstance().register(this.context, extension);
	}

	/**
	 * 
	 * @param extensionDesc
	 */
	public synchronized void removeExtension(ProgramExtension extensionDesc) {
		if (extensionDesc == null) {
			return;
		}
		if (extensionDesc != null && this.extensionDescs.contains(extensionDesc)) {
			this.extensionDescs.remove(extensionDesc);
		}

		// Unregister IProgramExtension service
		IProgramExtension extensionToRemove = null;
		for (IProgramExtension currExtension : this.extensions) {

			ProgramExtension currExtensionDesc = currExtension.getAdapter(ProgramExtension.class); // get ProgramExtension from the IProgramExtension
			if (extensionDesc.equals(currExtensionDesc)) {
				extensionToRemove = currExtension;
				break;
			}
		}
		if (extensionToRemove != null) {
			this.extensions.remove(extensionToRemove);
			ProgramExtensionRegistry.getInstance().unregister(this.context, extensionToRemove);
		}
	}

}

// // Register IProgramExtensions
// for (final ProgramExtension programExtensionDesc : this.programExtensionDescs) {
// IProgramExtension extension = new ProgramExtensionDescriptiveImpl(programExtensionDesc);
// extension.adapt(ProgramExtension.class, programExtensionDesc);
//
// ProgramExtensionRegistry.getInstance().register(context, extension);
// this.extensions.add(extension);
// }
