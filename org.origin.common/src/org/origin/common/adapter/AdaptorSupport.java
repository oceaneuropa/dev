package org.origin.common.adapter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AdaptorSupport implements IAdaptable {

	protected ConcurrentMap<Class<?>, Object> objectsMap;

	public AdaptorSupport() {
		objectsMap = new ConcurrentHashMap<Class<?>, Object>();
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		objectsMap.put(clazz, object);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		Object object = objectsMap.get(adapter);
		if (object != null && adapter.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		return null;
	}

}
