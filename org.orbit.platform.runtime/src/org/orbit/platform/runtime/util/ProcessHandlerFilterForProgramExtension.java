package org.orbit.platform.runtime.util;

import org.orbit.platform.runtime.processes.ProcessHandler;
import org.orbit.platform.runtime.processes.ProcessHandlerFilter;
import org.origin.common.extensions.core.IExtension;

public class ProcessHandlerFilterForProgramExtension implements ProcessHandlerFilter {

	protected IExtension extension;

	public ProcessHandlerFilterForProgramExtension(IExtension extension) {
		this.extension = extension;
	}

	@Override
	public boolean accept(ProcessHandler processHandler) {
		IExtension currExtension = processHandler.getExtension();
		if (this.extension.equals(currExtension)) {
			return true;
		}
		return false;
	}

}
