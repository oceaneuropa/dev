package org.origin.common.resource;

import org.origin.common.adapter.IAdaptable;

public interface RObject extends IAdaptable {

	public Resource eResource();

	public RObject eContainer();

	public void setContainer(RObject parent);

}
