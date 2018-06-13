package org.origin.common.extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.core.IExtensionRegistry;
import org.origin.common.extensions.core.impl.ExtensionDescriptiveImpl;
import org.osgi.framework.BundleContext;

public abstract class ProgramExtensions {

	protected String bundleId;
	protected String realm = "default";
	protected BundleContext bundleContext;
	protected List<Extension> extensionDescs = new ArrayList<Extension>();
	protected List<IExtension> extensions = new ArrayList<IExtension>();
	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	public ProgramExtensions() {
	}

	public String getBundleId() {
		return this.bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getRealm() {
		return this.realm;
	}

	protected String checkRealm(String realm) {
		if (realm == null) {
			realm = "default";
		}
		return realm;
	}

	/**
	 * Register program extensions.
	 * 
	 * @param bundleContext
	 */
	public synchronized void start(BundleContext bundleContext) {
		if (this.isStarted.get()) {
			System.err.println(getClass().getName() + " is already started.");
			return;
		}
		this.isStarted.set(true);
		this.bundleContext = bundleContext;

		createExtensions();
	}

	/**
	 * Unregister program extensions.
	 * 
	 * @param bundleContext
	 */
	public synchronized void stop(BundleContext bundleContext) {
		if (!isStarted()) {
			System.err.println(getClass().getName() + " is not started.");
		}
		if (!this.isStarted.compareAndSet(true, false)) {
			System.err.println(getClass().getName() + " is already stopped.");
			return;
		}

		// Unregister IProgramExtensions
		for (IExtension extension : this.extensions) {
			IExtensionRegistry.getInstance().unregister(bundleContext, extension);
		}

		// clear up data
		this.extensionDescs.clear();
		this.extensions.clear();

		this.bundleContext = null;
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
	public List<Extension> getExtensionDescriptions() {
		return this.extensionDescs;
	}

	/**
	 * 
	 * @param extensionDesc
	 */
	public synchronized void addExtension(Extension extensionDesc) {
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

		Extension extensionDescToRemove = null;
		for (final Extension currExtensionDesc : this.extensionDescs) {
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
		IExtension extension = new ExtensionDescriptiveImpl(extensionDesc);
		extension.adapt(Extension.class, extensionDesc); // set (adapt) ProgramExtension in the IProgramExtension

		if (this.bundleContext != null) {
			extension.adapt(BundleContext.class, this.bundleContext);
		}

		this.extensions.add(extension);
		IExtensionRegistry.getInstance().register(this.bundleContext, extension);
	}

	/**
	 * 
	 * @param extensionDesc
	 */
	public synchronized void removeExtension(Extension extensionDesc) {
		if (extensionDesc == null) {
			return;
		}
		if (extensionDesc != null && this.extensionDescs.contains(extensionDesc)) {
			this.extensionDescs.remove(extensionDesc);
		}

		// Unregister IProgramExtension service
		IExtension extensionToRemove = null;
		for (IExtension currExtension : this.extensions) {
			Extension currExtensionDesc = currExtension.getAdapter(Extension.class); // get ProgramExtension from the IProgramExtension
			if (extensionDesc.equals(currExtensionDesc)) {
				extensionToRemove = currExtension;
				break;
			}
		}
		if (extensionToRemove != null) {
			this.extensions.remove(extensionToRemove);
			IExtensionRegistry.getInstance().unregister(this.bundleContext, extensionToRemove);
		}
	}

}
