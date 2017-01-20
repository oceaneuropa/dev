package org.origin.common.resource;

import org.origin.common.adapter.IAdaptable;

public interface RObject extends IAdaptable {

	Resource eResource();

	RObject eContainer();

	void setContainer(RObject parent);

}
