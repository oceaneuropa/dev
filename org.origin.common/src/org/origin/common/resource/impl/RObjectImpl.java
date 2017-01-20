package org.origin.common.resource.impl;

import java.util.Iterator;
import java.util.Map.Entry;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.resource.RObject;
import org.origin.common.resource.Resource;

public abstract class RObjectImpl implements RObject, Cloneable {

	protected Resource resource;
	protected RObject container;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	@Override
	public Resource eResource() {
		if (this.resource != null) {
			return this.resource;
		}
		if (this.container != null) {
			return this.container.eResource();
		}
		return null;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public RObject eContainer() {
		return this.container;
	}

	public void setContainer(RObject container) {
		this.container = container;

		// Now the resource of this RObject is determined by the resource of the container.
		// The resource referenced by this RObject may become stale and so should be removed.
		this.resource = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		try {
			RObjectImpl clone = (RObjectImpl) super.clone();

			if (this.eContainer() != null) {
				clone.setContainer(this.eContainer());
			} else {
				clone.setResource(this.eResource());
			}

			for (Iterator<Entry<Class<?>, Object>> itor = this.adaptorSupport.iterator(); itor.hasNext();) {
				Entry<Class<?>, Object> entry = itor.next();
				Class<Object> clazz = (Class<Object>) entry.getKey();
				Object value = entry.getValue();
				clone.adapt(clazz, value);
			}

			return clone;
		} catch (CloneNotSupportedException e) {
			return null; // won't happen
		}
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
	public <T> void adapt(Class<T> clazz, T object) {
		if (Resource.class.isAssignableFrom(clazz) && object instanceof Resource) {
			this.resource = (Resource) object;
		}
		this.adaptorSupport.adapt(clazz, object);
	}

}
