package org.origin.common.osgi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dependency {

	public enum STATE {
		RESOLVED("RESOLVED"), //
		UNRESOLVED("UNRESOLVED");

		protected String state;

		STATE(String state) {
			this.state = state;
		}

		public String getState() {
			return this.state;
		}

		public boolean isResolved() {
			return ("RESOLVED".equalsIgnoreCase(this.state)) ? true : false;
		}

		public boolean isUnresolved() {
			return ("UNRESOLVED".equalsIgnoreCase(this.state)) ? true : false;
		}
	}

	protected String name;
	protected Class<?> type;
	protected Dependency.STATE state = Dependency.STATE.UNRESOLVED;
	protected Object data;
	protected Map<Object, Object> dataMap = new HashMap<Object, Object>();
	protected List<DependencyListener> listeners = new ArrayList<DependencyListener>();

	/**
	 * 
	 * @param name
	 *            name of the dependency
	 * @param type
	 *            type of the dependency
	 */
	public Dependency(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public Class<?> getType() {
		return this.type;
	}

	public Dependency.STATE getState() {
		return this.state;
	}

	public void setState(Dependency.STATE state) {
		Dependency.STATE oldState = this.state;
		this.state = state;

		if ((oldState == null && this.state != null) || (oldState != null && !oldState.equals(this.state))) {
			notifyListeners(new DependencyEvent(this, oldState, this.state));
		}
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

	public void addListener(DependencyListener listener) {
		if (listener != null && !this.listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}

	public void removeListener(DependencyListener listener) {
		if (listener != null && this.listeners.contains(listener)) {
			this.listeners.remove(listener);
		}
	}

	protected void notifyListeners(DependencyEvent event) {
		for (DependencyListener listener : this.listeners) {
			listener.onDependencyChange(event);
		}
	}

}
