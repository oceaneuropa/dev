package org.orbit.platform.runtime.util;

import org.orbit.platform.runtime.processes.ProcessHandler;
import org.orbit.platform.runtime.processes.ProcessHandlerFilter;
import org.orbit.platform.sdk.extension.IProgramExtension;

public class ProcessHandlerFilterForProgramExtension implements ProcessHandlerFilter {

	protected IProgramExtension extension;

	public ProcessHandlerFilterForProgramExtension(IProgramExtension extension) {
		this.extension = extension;
	}

	@Override
	public boolean accept(ProcessHandler processHandler) {
		IProgramExtension currExtension = processHandler.getExtension();
		if (this.extension.equals(currExtension)) {
			return true;
		}
		return false;
	}

}
