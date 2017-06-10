package org.origin.common.osgi;

public class DependencyEvent {

	protected Dependency dependency;
	protected Dependency.STATE oldState;
	protected Dependency.STATE newState;

	public DependencyEvent() {
	}

	/**
	 * 
	 * @param dependency
	 * @param oldState
	 * @param newState
	 */
	public DependencyEvent(Dependency dependency, Dependency.STATE oldState, Dependency.STATE newState) {
		this.dependency = dependency;
		this.oldState = oldState;
		this.newState = newState;
	}

	public Dependency getDependency() {
		return dependency;
	}

	public void setDependency(Dependency dependency) {
		this.dependency = dependency;
	}

	public Dependency.STATE getOldState() {
		return oldState;
	}

	public void setOldState(Dependency.STATE oldState) {
		this.oldState = oldState;
	}

	public Dependency.STATE getNewState() {
		return newState;
	}

	public void setNewState(Dependency.STATE newState) {
		this.newState = newState;
	}

}
