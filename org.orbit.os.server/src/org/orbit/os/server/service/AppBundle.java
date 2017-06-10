package org.orbit.os.server.service;

import org.origin.common.osgi.BundleUtil;
import org.origin.common.osgi.Dependency;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class AppBundle {

	public enum RUNTIME_STATE {
		STARTED("STARTED"), //
		STOPPED("STOPPED");

		protected String state;

		RUNTIME_STATE(String state) {
			this.state = state;
		}

		public boolean isStarted() {
			return ("STARTED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStopped() {
			return ("STOPPED").equalsIgnoreCase(this.state) ? true : false;
		}
	}

	protected boolean debug = true;
	protected AppBundle.RUNTIME_STATE runtimeState = AppBundle.RUNTIME_STATE.STOPPED;
	protected String bundleName;
	protected String bundleVersion;
	protected Dependency dependency;
	protected Bundle bundle;

	/**
	 * 
	 * @param bundleName
	 * @param bundleVersion
	 */
	public AppBundle(String bundleName, String bundleVersion) {
		this.bundleName = bundleName;
		this.bundleVersion = bundleVersion;
	}

	public AppBundle.RUNTIME_STATE getRuntimeState() {
		return this.runtimeState;
	}

	public void setRuntimeState(AppBundle.RUNTIME_STATE runtimeState) {
		this.runtimeState = runtimeState;
	}

	public String getBundleName() {
		return this.bundleName;
	}

	public String getBundleVersion() {
		return this.bundleVersion;
	}

	public Dependency getDependency() {
		return this.dependency;
	}

	public void setDependency(Dependency dependency) {
		this.dependency = dependency;
	}

	public boolean isReady() {
		return (this.bundle != null) ? true : false;
	}

	public Bundle getBundle() {
		return this.bundle;
	}

	public void setBundle(Bundle bundle) {
		if (debug) {
			System.out.println(getClass().getName() + ".setBundle()");
			BundleUtil.debugBundle(bundle);
		}

		Bundle oldBundle = this.bundle;
		this.bundle = bundle;

		// Let the dependency to hold a reference to the Bundle.
		this.dependency.setData(this.bundle);

		if ((oldBundle == null && this.bundle != null) || (oldBundle != null && !oldBundle.equals(this.bundle))) {
			// AppBundle's referencing to the Bundle is changed.
			// Notify dependency as either resolved (Bundle is available) or unresolved (Bundle is null).
			Dependency.STATE newState = isReady() ? Dependency.STATE.RESOLVED : Dependency.STATE.UNRESOLVED;
			this.dependency.setState(newState);
		}
	}

	/**
	 * Start app bundle.
	 * 
	 * @throws AppException
	 */
	public void start() throws AppException {
		if (debug) {
			System.out.println(getClass().getName() + ".start()");
		}
		if (!isReady()) {
			String text = "AppBundle " + this.bundleName + "(" + this.bundleVersion + ")";
			throw new AppException(text + " is not ready.");
		}
		try {
			this.bundle.start(Bundle.START_TRANSIENT);

		} catch (BundleException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e);
		}
	}

	/**
	 * Stop app bundle.
	 * 
	 * https://stackoverflow.com/questions/36940647/what-are-the-available-options-for-org-osgi-framework-bundle-stopint
	 * 
	 * Bundle.STOP_TRANSIENT: Does not modify the autostart flag of the bundle
	 */
	public void stop() throws AppException {
		if (debug) {
			System.out.println(getClass().getName() + ".stop()");
		}
		if (!isReady()) {
			String text = "AppBundle " + this.bundleName + "(" + this.bundleVersion + ")";
			throw new AppException(text + " is not ready.");
		}

		if (this.bundle != null) {
			try {
				// Stop the bundle only when it is either starting or is started.
				if (BundleUtil.isBundleStarting(this.bundle) || BundleUtil.isBundleActive(this.bundle)) {
					// Modifies the autostart flag of the bundle, too, so the Bundle might not be started after a framework restart.
					// Bundle will not auto start next time when stopped this way.
					this.bundle.stop();
				}

			} catch (BundleException e) {
				e.printStackTrace();
				throw new AppException(e.getMessage(), e);
			}
		}
	}

	@Override
	public String toString() {
		return "AppBundle [state=" + runtimeState + ", bundleName=" + bundleName + ", bundleVersion=" + bundleVersion + ", dependency=" + dependency + ", bundle=" + bundle + "]";
	}

}
