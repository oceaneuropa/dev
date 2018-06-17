package org.orbit.platform.webconsole.common;

import java.util.HashMap;
import java.util.Map;

public class ServiceContext {

	private final Map<Class<?>, Object> registry = new HashMap<>();

	public void set(Object obj) {
		this.registry.put(obj.getClass(), obj);
	}

	public <T> void set(Class<T> clazz, T obj) {
		this.registry.put(clazz, obj);
	}

	public <T> void set(Class<T> clazz, ServiceResolver<T> resolver) {
		this.registry.put(clazz, resolver);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) {
		T t = null;
		Object object = this.registry.get(clazz);
		if (object instanceof ServiceResolver) {
			object = ((ServiceResolver<T>) object).resolve();
		}
		if (object != null && clazz.isAssignableFrom(object.getClass())) {
			t = (T) object;
		}
		return t;
	}

	public void clear() {
		this.registry.clear();
	}

}
