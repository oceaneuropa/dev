package org.origin.common.resource;

import org.origin.common.adapter.IAdaptable;

public interface ResourceObject extends IAdaptable {

	public Resource eResource();

	public ResourceObject eContainer();

	public void setContainer(ResourceObject parent);

}
