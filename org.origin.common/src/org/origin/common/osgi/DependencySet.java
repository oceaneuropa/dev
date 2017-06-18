package org.origin.common.osgi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DependencySet {

	public enum STATE {
		ALL_RESOLVED("ALL_RESOLVED"), //
		NOT_ALL_RESOLVED("NOT_ALL_RESOLVED");

		protected String state;

		STATE(String state) {
			this.state = state;
		}

		public String getState() {
			return this.state;
		}

		public boolean isAllResolved() {
			return ("ALL_RESOLVED".equalsIgnoreCase(this.state)) ? true : false;
		}

		public boolean isNotAllResolved() {
			return ("NOT_ALL_RESOLVED".equalsIgnoreCase(this.state)) ? true : false;
		}
	}

	protected DependencySet.STATE state = DependencySet.STATE.NOT_ALL_RESOLVED;
	protected DependencyListener dependencyListener;
	protected Map<Class<?>, List<Dependency>> dependenciesMap = new LinkedHashMap<Class<?>, List<Dependency>>();
	protected List<Dependency> unresolvedDependencies = new ArrayList<Dependency>();
	protected Object data;
	protected Map<Object, Object> dataMap = new HashMap<Object, Object>();
	protected List<DependencySetListener> listeners = new ArrayList<DependencySetListener>();

	public DependencySet() {
		this.dependencyListener = new DependencyListener() {
			@Override
			public void onDependencyChange(DependencyEvent event) {
				handleDependencyChange(event);
			}
		};
	}

	protected void handleDependencyChange(DependencyEvent event) {
		Dependency dependency = event.getDependency();
		Dependency.STATE newState = event.getNewState();

		if (newState.isResolved()) {
			this.unresolvedDependencies.remove(dependency);

		} else if (newState.isUnresolved()) {
			this.unresolvedDependencies.add(dependency);
		}

		update();
	}

	public DependencySet.STATE getState() {
		return this.state;
	}

	protected void setState(DependencySet.STATE state) {
		DependencySet.STATE oldState = this.state;
		this.state = state;

		if ((oldState == null && this.state != null) || (oldState != null && !oldState.equals(this.state))) {
			notifyListeners(new DependencySetEvent(this, oldState, this.state));
		}
	}

	protected synchronized boolean isAllResolved() {
		return this.unresolvedDependencies.isEmpty();
	}

	/**
	 * 
	 * @param dependency
	 */
	public synchronized void addDependency(Dependency dependency) {
		dependency.addListener(this.dependencyListener);

		List<Dependency> dependenciesOfType = this.dependenciesMap.get(dependency.getType());
		if (dependenciesOfType == null) {
			dependenciesOfType = new ArrayList<Dependency>();
			this.dependenciesMap.put(dependency.getType(), dependenciesOfType);
		}
		dependenciesOfType.add(dependency);

		if (dependency.getState().isUnresolved()) {
			this.unresolvedDependencies.add(dependency);
		}

		update();
	}

	public void update() {
		DependencySet.STATE newState = isAllResolved() ? DependencySet.STATE.ALL_RESOLVED : DependencySet.STATE.NOT_ALL_RESOLVED;
		setState(newState);
	}

	public synchronized List<Dependency> getDependencies() {
		List<Dependency> allDependencies = new ArrayList<Dependency>();
		for (Class<?> type : this.dependenciesMap.keySet()) {
			allDependencies.addAll(this.dependenciesMap.get(type));
		}
		return allDependencies;
	}

	public synchronized <T> List<Dependency> getDependencies(Class<T> type) {
		List<Dependency> dependenciesOfType = new ArrayList<Dependency>();
		if (this.dependenciesMap.containsKey(type)) {
			for (Dependency dependency : this.dependenciesMap.get(type)) {
				dependenciesOfType.add((Dependency) dependency);
			}
		}
		return dependenciesOfType;
	}

	public synchronized List<Dependency> getUnresolvedDependencies() {
		return this.unresolvedDependencies;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getData() {
		return this.data;
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(Class<T> clazz) {
		if (this.data != null && clazz.isAssignableFrom(this.data.getClass())) {
			return (T) this.data;
		}
		return null;
	}

	public void setData(Object key, Object data) {
		if (data != null) {
			this.dataMap.put(key, data);
		} else {
			this.dataMap.remove(key);
		}
	}

	public Object getData(Object key) {
		return this.dataMap.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(Object key, Class<T> clazz) {
		Object data = this.dataMap.get(key);
		if (data != null && clazz.isAssignableFrom(data.getClass())) {
			return (T) data;
		}
		return null;
	}

	/**
	 * 
	 * @param listener
	 */
	public void addListener(DependencySetListener listener) {
		if (listener != null && !this.listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}

	/**
	 * 
	 * @param listener
	 */
	public void removeListener(DependencySetListener listener) {
		if (listener != null && this.listeners.contains(listener)) {
			this.listeners.remove(listener);
		}
	}

	protected void notifyListeners(DependencySetEvent event) {
		for (DependencySetListener listener : this.listeners) {
			listener.onDependencySetChange(event);
		}
	}

	public void reset() {
		// Clear dependency set listeners. No more not notification to listeners listening on this DependencySet change event.
		this.listeners.clear();

		// Stop listening to the Dependency change event of each added Dependency.
		for (Class<?> type : this.dependenciesMap.keySet()) {
			List<Dependency> currDependencies = this.dependenciesMap.get(type);
			if (currDependencies != null) {
				for (Dependency currDependency : currDependencies) {
					currDependency.removeListener(this.dependencyListener);
				}
			}
		}

		// Clear dependencies
		this.dependenciesMap.clear();
		this.unresolvedDependencies.clear();

		// Clear data
		this.data = null;
		this.dataMap.clear();

		// Reset to the default not_all_resolved state
		this.state = DependencySet.STATE.NOT_ALL_RESOLVED;
	}

}
