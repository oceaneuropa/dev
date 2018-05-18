package org.origin.common.osgi;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleReference;

public abstract class AbstractBundleActivator implements BundleActivator {

	protected static BundleContext bundleContext;

	static BundleContext getContext() {
		return bundleContext;
	}

	protected Bundle bundle;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		AbstractBundleActivator.bundleContext = bundleContext;
		this.bundle = bundleContext.getBundle();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		AbstractBundleActivator.bundleContext = null;
	}

	/**
	 * Returns the bundle associated with this plug-in.
	 * 
	 * @return the associated bundle
	 */
	public final Bundle getBundle() {
		if (this.bundle != null) {
			return this.bundle;
		}
		ClassLoader cl = getClass().getClassLoader();
		if (cl instanceof BundleReference) {
			return ((BundleReference) cl).getBundle();
		}
		return null;
	}

	public String getBundleName() {
		Bundle bundle = getBundle();
		if (bundle != null) {
			String name = bundle.getSymbolicName();
			long bundleId = bundle.getBundleId();
			return (name != null) ? name : String.valueOf(bundleId);
		}
		return null;
	}

	public String getUniqueIdentifier() {
		return getBundleName();
	}

	@Override
	public String toString() {
		String name = getBundleName();
		if (name != null) {
			return name;
		}
		return super.toString();
	}

}
