package org.orbit.platform.runtime.programs;

import java.util.ArrayList;
import java.util.List;

import org.orbit.os.api.apps.ProgramManifest;
import org.origin.common.osgi.DependencySet;

public class Program {

	public enum RUNTIME_STATE {
		STARTED("STARTED"), // all program bundles are started
		START_IMPERFECT("START_IMPERFECT"), // not all program bundles are started (some of program bundles are stopped).
		START_FAILED("START_FAILED"), // one or more program bundles start failed
		STOPPED("STOPPED"), // all program bundles are stopped
		STOP_IMPERFECT("STOP_IMPERFECT"), // not all program bundles are stopped (some of program bundles are still started).
		STOP_FAILED("STOP_FAILED"), // one or more program bundle stop failed
		DAMAGED("DAMAGED"); // one or more program bundle's osgi bundle is not physically available

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

	protected ProgramManifest programManifest;
	protected DependencySet dependencySet;
	protected List<ProgramBundle> programBundles = new ArrayList<ProgramBundle>();
	protected Program.RUNTIME_STATE runtimeState = Program.RUNTIME_STATE.STOPPED;

	public Program(ProgramManifest programManifest) {
		this.programManifest = programManifest;
	}

	public ProgramManifest getProgramManifest() {
		return this.programManifest;
	}

	public RUNTIME_STATE getRuntimeState() {
		return this.runtimeState;
	}

	public void setRuntimeState(Program.RUNTIME_STATE runtimeState) {
		this.runtimeState = runtimeState;
	}

	public DependencySet getDependencySet() {
		return this.dependencySet;
	}

	public void setDependencySet(DependencySet dependencySet) {
		this.dependencySet = dependencySet;
	}

	public List<ProgramBundle> getProgramBundles() {
		return this.programBundles;
	}

	public boolean addProgramBundle(ProgramBundle programBundle) {
		if (programBundle != null && !this.programBundles.contains(programBundle)) {
			return this.programBundles.add(programBundle);
		}
		return false;
	}

	public boolean removeProgramBundle(ProgramBundle programBundle) {
		if (programBundle != null && this.programBundles.contains(programBundle)) {
			return this.programBundles.remove(programBundle);
		}
		return false;
	}

	public String getSimpleName() {
		return this.programManifest.getId() + " (" + this.programManifest.getVersion() + ")";
	}

	@Override
	public String toString() {
		return "Program [id=" + this.programManifest.getId() + ", version=" + this.programManifest.getVersion() + "state=" + runtimeState + ", dependencySet=" + dependencySet + "]";
	}

}

// protected Dependency dependency;
// protected Object bundle;

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
