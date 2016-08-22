package org.origin.common.resource;

import org.origin.common.adapter.AdaptorSupport;

public abstract class AbstractResourceObject implements ResourceObject {

	protected Resource resource;
	protected ResourceObject container;
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
	public ResourceObject eContainer() {
		return this.container;
	}

	public void setContainer(ResourceObject container) {
		this.container = container;
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
