package org.origin.common.cluster.impl;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.cluster.EventContext;

public class EventContextImpl implements EventContext {

	protected Map<String, Object> properties = new HashMap<String, Object>();

	public void set(String propName, Object propValue) {
		this.properties.put(propName, propValue);
	}

	public Object get(String propName) {
		return this.properties.get(propName);
	}

}
