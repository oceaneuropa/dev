package org.origin.common.rest.editpolicy;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.rest.model.Request;

public abstract class AbstractWSEditPolicy implements WSEditPolicy {

	protected Map<Class<?>, Object> servicesMap = new HashMap<Class<?>, Object>();

	public AbstractWSEditPolicy() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S getService(Class<S> clazz) {
		S service = null;
		Object object = this.servicesMap.get(clazz);
		if (object != null && clazz.isAssignableFrom(object.getClass())) {
			service = (S) object;
		}
		return service;
	}

	@Override
	public void setService(Class<?> clazz, Object service) {
		if (service != null) {
			this.servicesMap.put(clazz, service);
		} else {
			this.servicesMap.remove(clazz);
		}
	}

	@Override
	public abstract WSCommand getCommand(Request request);

}
