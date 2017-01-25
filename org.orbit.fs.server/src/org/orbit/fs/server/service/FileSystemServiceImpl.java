package org.orbit.fs.server.service;

import java.util.Hashtable;

import org.orbit.fs.api.FilePath;
import org.origin.common.adapter.AdaptorSupport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public abstract class FileSystemServiceImpl implements FileSystemService {

	protected static final FilePath[] EMPTY_PATHS = new FilePath[0];

	protected FileSystemServiceConfiguration config;
	protected ServiceRegistration<?> serviceReg;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	@Override
	public void setConfig(FileSystemServiceConfiguration config) {
		this.config = config;
	}

	@Override
	public FileSystemServiceConfiguration getConfig() {
		return this.config;
	}

	@Override
	public void start() {
		register();
	}

	@Override
	public void stop() {
		unregister();
	}

	/**
	 * Register as a service
	 */
	public void register() {
		BundleContext bundleContext = getAdapter(BundleContext.class);
		if (bundleContext != null) {
			Hashtable<String, Object> props = new Hashtable<String, Object>();
			this.serviceReg = bundleContext.registerService(FileSystemService.class, this, props);
		}
	}

	/**
	 * Unregister the service
	 */
	public void unregister() {
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}
