package org.origin.common.event;

import java.util.HashMap;
import java.util.Map;

public class PropertyChangeEvent {

	protected Object source;
	protected String name;
	protected Object oldValue;
	protected Object newValue;
	protected Map<String, Object> properties = new HashMap<String, Object>();

	public PropertyChangeEvent() {
	}

	/**
	 * 
	 * @param source
	 * @param name
	 * @param oldValue
	 * @param newValue
	 */
	public PropertyChangeEvent(Object source, String name, Object oldValue, Object newValue) {
		this.source = source;
		this.name = name;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	public void put(String propName, Object value) {
		this.properties.put(propName, value);
	}

	public Object get(String propName) {
		return this.properties.get(propName);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String propName, Class<T> clazz) {
		T t = null;
		Object value = this.properties.get(propName);
		if (value != null && clazz.isAssignableFrom(value.getClass())) {
			t = (T) value;
		}
		return t;
	}

	public String[] getPropertyNames() {
		return this.properties.keySet().toArray(new String[this.properties.size()]);
	}

}
