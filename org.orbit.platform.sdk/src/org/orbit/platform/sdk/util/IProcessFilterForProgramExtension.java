package org.orbit.platform.sdk.util;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.IProcessFilter;
import org.orbit.platform.sdk.extension.IProgramExtension;

public class IProcessFilterForProgramExtension implements IProcessFilter {

	protected IProgramExtension extension;

	public IProcessFilterForProgramExtension(IProgramExtension extension) {
		this.extension = extension;
	}

	@Override
	public boolean accept(IProcess process) {
		IProgramExtension extensionOfProcess = process.getAdapter(IProgramExtension.class);
		if (this.extension.equals(extensionOfProcess)) {
			return true;
		}
		return false;
	}

}
