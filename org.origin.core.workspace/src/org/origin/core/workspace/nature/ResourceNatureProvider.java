package org.origin.core.workspace.nature;

import org.origin.core.workspace.IResource;

public interface ResourceNatureProvider<NATURE extends ResourceNature<RES>, RES extends IResource> {

	/**
	 * 
	 * @return
	 */
	public ResourceNature<RES> newInstance();

}
