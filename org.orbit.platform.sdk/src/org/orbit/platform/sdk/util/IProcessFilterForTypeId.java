package org.orbit.platform.sdk.util;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.IProcessFilter;
import org.orbit.platform.sdk.extension.IProgramExtension;

public class IProcessFilterForTypeId implements IProcessFilter {

	protected String typeId;

	public IProcessFilterForTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public boolean accept(IProcess process) {
		IProgramExtension extensionOfProcess = process.getAdapter(IProgramExtension.class);
		String currTypeId = extensionOfProcess.getTypeId();
		if (this.typeId.equals(currTypeId)) {
			return true;
		}
		return false;
	}

}
