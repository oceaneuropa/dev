package org.orbit.platform.sdk.util;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.IProcessFilter;
import org.orbit.platform.sdk.extension.IProgramExtension;

public class IProcessFilterByTypeId implements IProcessFilter {

	protected String typeId;

	public IProcessFilterByTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public boolean accept(IProcess process) {
		IProgramExtension extension = process.getAdapter(IProgramExtension.class);
		String currTypeId = extension.getTypeId();
		if (this.typeId.equals(currTypeId)) {
			return true;
		}
		return false;
	}

}
