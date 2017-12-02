package org.orbit.os.server.apps;

import java.util.ArrayList;
import java.util.List;

import org.orbit.app.AppManifest;
import org.origin.common.osgi.DependencySet;

public class App {

	public enum RUNTIME_STATE {
		STARTED("STARTED"), // all app bundles are started
		START_IMPERFECT("START_IMPERFECT"), // not all app bundles are started (some of app bundles are stopped).
		START_FAILED("START_FAILED"), // one or more app bundles start failed
		STOPPED("STOPPED"), // all app bundles are stopped
		STOP_IMPERFECT("STOP_IMPERFECT"), // not all app bundles are stopped (some of app bundles are still started).
		STOP_FAILED("STOP_FAILED"), // one or more app bundle stop failed
		DAMAGED("DAMAGED"); // one or more app bundle's osgi bundle is not physically available

		protected String state;

		RUNTIME_STATE(String state) {
			this.state = state;
		}

		public boolean isStarted() {
			return ("STARTED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStartImperfect() {
			return ("START_IMPERFECT").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStartFailed() {
			return ("START_FAILED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStopped() {
			return ("STOPPED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStopImperfect() {
			return ("STOP_IMPERFECT").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStopFailed() {
			return ("STOP_FAILED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isDamaged() {
			return ("DAMAGED").equalsIgnoreCase(this.state) ? true : false;
		}
	}

	protected boolean debug = false;
	protected AppManifest appManifest;
	protected App.RUNTIME_STATE runtimeState = App.RUNTIME_STATE.STOPPED;
	protected DependencySet dependencySet;
	protected List<AppBundle> appBundles = new ArrayList<AppBundle>();

	// protected Dependency dependency;
	// protected Object bundle;

	public App(AppManifest appManifest) {
		this.appManifest = appManifest;
	}

	public AppManifest getAppManifest() {
		return this.appManifest;
	}

	public App.RUNTIME_STATE getRuntimeState() {
		return this.runtimeState;
	}

	public void setRuntimeState(App.RUNTIME_STATE runtimeState) {
		this.runtimeState = runtimeState;
	}

	public DependencySet getDependencySet() {
		return this.dependencySet;
	}

	public void setDependencySet(DependencySet dependencySet) {
		this.dependencySet = dependencySet;
	}

	// public Dependency getDependency() {
	// return this.dependency;
	// }
	//
	// public void setDependency(Dependency dependency) {
	// this.dependency = dependency;
	// }
	//
	// public boolean isReady() {
	// return (this.bundle != null) ? true : false;
	// }
	//
	// @SuppressWarnings("unchecked")
	// public <T> T getBundle(Class<T> bundleType) {
	// if (this.bundle != null && bundleType.isAssignableFrom(this.bundle.getClass())) {
	// return (T) this.bundle;
	// }
	// return null;
	// }
	//
	// public <T> void setBundle(T bundle) {
	// if (debug) {
	// System.out.println(getClass().getName() + ".setBundle()");
	// }
	//
	// Object oldBundle = this.bundle;
	// this.bundle = bundle;
	//
	// // Let the dependency to hold a reference to the Bundle.
	// this.dependency.setData(this.bundle);
	//
	// if ((oldBundle == null && this.bundle != null) || (oldBundle != null && !oldBundle.equals(this.bundle))) {
	// // AppModule's referencing to OSGi Bundle is changed.
	// // Notify dependency as either resolved (Bundle is available) or unresolved (Bundle is null).
	// Dependency.STATE newState = isReady() ? Dependency.STATE.RESOLVED : Dependency.STATE.UNRESOLVED;
	// this.dependency.setState(newState);
	// }
	// }

	public List<AppBundle> getAppBundles() {
		return this.appBundles;
	}

	public boolean addAppBundle(AppBundle appBundle) {
		if (appBundle != null && !this.appBundles.contains(appBundle)) {
			return this.appBundles.add(appBundle);
		}
		return false;
	}

	public boolean removeAppBundle(AppBundle appBundle) {
		if (appBundle != null && this.appBundles.contains(appBundle)) {
			return this.appBundles.remove(appBundle);
		}
		return false;
	}

	public String getSimpleName() {
		return this.appManifest.getId() + " (" + this.appManifest.getVersion() + ")";
	}

	@Override
	public String toString() {
		return "App [id=" + this.appManifest.getId() + ", version=" + this.appManifest.getVersion() + "state=" + runtimeState + ", dependencySet=" + dependencySet + "]";
	}

}
