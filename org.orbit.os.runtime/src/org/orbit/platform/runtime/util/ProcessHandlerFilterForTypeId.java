package org.orbit.platform.runtime.util;

import org.orbit.platform.runtime.processes.ProcessHandler;
import org.orbit.platform.runtime.processes.ProcessHandlerFilter;
import org.orbit.platform.sdk.extension.IProgramExtension;

public class ProcessHandlerFilterForTypeId implements ProcessHandlerFilter {

	protected String typeId;

	public ProcessHandlerFilterForTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public boolean accept(ProcessHandler processHandler) {
		IProgramExtension currExtension = processHandler.getExtension();
		String currTypeId = currExtension.getTypeId();
		if (this.typeId.equals(currTypeId)) {
			return true;
		}
		return false;
	}

}
