/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.origin.common.adapter;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AdaptorSupport implements IAdaptable {

	protected Map<Class<?>, Object> objectsMap;

	public AdaptorSupport() {
		this.objectsMap = new LinkedHashMap<Class<?>, Object>();
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.objectsMap.put(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		if (classes != null) {
			for (Class<T> clazz : classes) {
				this.objectsMap.put(clazz, object);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		Object object = this.objectsMap.get(adapter);
		if (object != null && adapter.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		return null;
	}

	public Iterator<Entry<Class<?>, Object>> iterator() {
		return this.objectsMap.entrySet().iterator();
	}

}

// this.objectsMap = new ConcurrentHashMap<Class<?>, Object>();