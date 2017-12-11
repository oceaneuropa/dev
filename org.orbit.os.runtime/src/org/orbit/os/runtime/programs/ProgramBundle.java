package org.orbit.os.runtime.programs;

import org.origin.common.osgi.Dependency;

public class ProgramBundle {

	public enum RUNTIME_STATE {
		STARTED("STARTED"), // program bundle is started
		START_FAILED("START_FAILED"), // program bundle start failed
		STOPPED("STOPPED"), // program bundle is stopped
		STOP_FAILED("STOP_FAILED"), // program bundle stop failed
		DAMAGED("DAMAGED"); // osgi bundle is not physically available

		protected String state;

		RUNTIME_STATE(String state) {
			this.state = state;
		}

		public boolean isStarted() {
			return ("STARTED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStartFailed() {
			return ("START_FAILED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStopped() {
			return ("STOPPED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStopFailed() {
			return ("STOP_FAILED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isDamaged() {
			return ("DAMAGED").equalsIgnoreCase(this.state) ? true : false;
		}
	}

	protected boolean debug = false;
	protected boolean isApplication;
	protected boolean isModule;
	protected ProgramBundle.RUNTIME_STATE runtimeState = ProgramBundle.RUNTIME_STATE.STOPPED;
	protected String bundleName;
	protected String bundleVersion;
	protected Dependency dependency;
	protected Object bundle;

	/**
	 * 
	 * @param bundleName
	 * @param bundleVersion
	 */
	public ProgramBundle(String bundleName, String bundleVersion) {
		this.bundleName = bundleName;
		this.bundleVersion = bundleVersion;
	}

	public boolean isApplication() {
		return isApplication;
	}

	public void setIsApplication(boolean isApplication) {
		this.isApplication = isApplication;
	}

	public boolean isModule() {
		return isModule;
	}

	public void setIsModule(boolean isModule) {
		this.isModule = isModule;
	}

	public ProgramBundle.RUNTIME_STATE getRuntimeState() {
		return this.runtimeState;
	}

	public void setRuntimeState(ProgramBundle.RUNTIME_STATE runtimeState) {
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

	@SuppressWarnings("unchecked")
	public <T> T getBundle(Class<T> bundleType) {
		if (this.bundle != null && bundleType.isAssignableFrom(this.bundle.getClass())) {
			return (T) this.bundle;
		}
		return null;
	}

	public <T> void setBundle(T bundle) {
		if (debug) {
			System.out.println(getClass().getName() + ".setBundle()");
		}

		Object oldBundle = this.bundle;
		this.bundle = bundle;

		// Let the dependency to hold a reference to the Bundle.
		this.dependency.setData(this.bundle);

		if ((oldBundle == null && this.bundle != null) || (oldBundle != null && !oldBundle.equals(this.bundle))) {
			// ProgramBundle's referencing to OSGi Bundle is changed.
			// Notify dependency as either resolved (Bundle is available) or unresolved (Bundle is null).
			Dependency.STATE newState = isReady() ? Dependency.STATE.RESOLVED : Dependency.STATE.UNRESOLVED;
			this.dependency.setState(newState);
		}
	}

	public String getSimpleName() {
		return getClass().getSimpleName() + " " + this.bundleName + " (" + this.bundleVersion + ")";
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [state=" + runtimeState + ", bundleName=" + bundleName + ", bundleVersion=" + bundleVersion + ", dependency=" + dependency + ", bundle=" + bundle + "]";
	}

}
