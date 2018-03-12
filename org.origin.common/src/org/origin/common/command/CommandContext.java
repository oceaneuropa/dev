package org.origin.common.command;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;

public class CommandContext implements IAdaptable {

	protected Map<String, Object> map = new HashMap<String, Object>();
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	public void put(String key, Object value) {
		this.map.put(key, value);
	}

	public Object get(String key) {
		return this.map.get(key);
	}

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		adaptorSupport.adapt(clazz, object);
	}

}
