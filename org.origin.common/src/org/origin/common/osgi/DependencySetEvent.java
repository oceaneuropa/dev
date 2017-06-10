package org.origin.common.osgi;

public class DependencySetEvent {

	protected DependencySet dependencySet;
	protected DependencySet.STATE oldState;
	protected DependencySet.STATE newState;

	public DependencySetEvent() {
	}

	/**
	 * 
	 * @param dependencySet
	 * @param oldState
	 * @param newState
	 */
	public DependencySetEvent(DependencySet dependencySet, DependencySet.STATE oldState, DependencySet.STATE newState) {
		this.dependencySet = dependencySet;
		this.oldState = oldState;
		this.newState = newState;
	}

	public DependencySet getDependencySet() {
		return dependencySet;
	}

	public void setDependencySet(DependencySet dependencySet) {
		this.dependencySet = dependencySet;
	}

	public DependencySet.STATE getOldState() {
		return oldState;
	}

	public void setOldState(DependencySet.STATE oldState) {
		this.oldState = oldState;
	}

	public DependencySet.STATE getNewState() {
		return newState;
	}

	public void setNewState(DependencySet.STATE newState) {
		this.newState = newState;
	}

}
