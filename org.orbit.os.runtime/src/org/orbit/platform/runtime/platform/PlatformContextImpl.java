package org.orbit.platform.runtime.platform;

import java.util.Map;

import org.orbit.platform.sdk.IPlatform;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.origin.common.adapter.AdaptorSupport;
import org.osgi.framework.BundleContext;

public class PlatformContextImpl implements IPlatformContext {

	protected IPlatform platform;
	protected BundleContext bundleContext;
	protected Map<String, Object> properties;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	public PlatformContextImpl() {
	}

	@Override
	public IPlatform getPlatform() {
		return this.platform;
	}

	public void setPlatform(IPlatform platform) {
		this.platform = platform;
	}

	@Override
	public IProcess[] getProcesses() {
		return null;
	}

	@Override
	public BundleContext getBundleContext() {
		return this.bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

}