package org.orbit.platform.runtime.util;

import org.orbit.platform.runtime.processes.ProcessHandler;
import org.orbit.platform.runtime.processes.ProcessHandlerFilter;
import org.origin.common.extensions.core.IExtension;

public class ProcessHandlerFilterForTypeId implements ProcessHandlerFilter {

	protected String typeId;

	public ProcessHandlerFilterForTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public boolean accept(ProcessHandler processHandler) {
		IExtension currExtension = processHandler.getExtension();
		String currTypeId = currExtension.getTypeId();
		if (this.typeId.equals(currTypeId)) {
			return true;
		}
		return false;
	}

}
