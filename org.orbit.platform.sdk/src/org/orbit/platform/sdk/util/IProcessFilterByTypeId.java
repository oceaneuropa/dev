package org.orbit.platform.sdk.util;

import org.orbit.platform.sdk.IProcess;
import org.origin.common.extensions.core.IExtension;

public class IProcessFilterByTypeId implements IProcessFilter {

	protected String typeId;

	public IProcessFilterByTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public boolean accept(IProcess process) {
		IExtension extension = process.getAdapter(IExtension.class);
		String currTypeId = extension.getTypeId();
		if (this.typeId.equals(currTypeId)) {
			return true;
		}
		return false;
	}

}
