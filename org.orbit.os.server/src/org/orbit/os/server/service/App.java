package org.orbit.os.server.service;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.osgi.DependencySet;

public class App {

	public enum RUNTIME_STATE {
		STARTED("STARTED"), //
		STOPPED("STOPPED"), //
		NOT_READY("NOT_READY");

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

		public boolean isNotReady() {
			return ("NOT_READY").equalsIgnoreCase(this.state) ? true : false;
		}
	}

	protected DependencySet dependencySet;
	protected List<AppBundle> appBundles = new ArrayList<AppBundle>();
	protected App.RUNTIME_STATE runtimeState;

	public App() {
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

}
