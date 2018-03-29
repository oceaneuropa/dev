package org.orbit.platform.sdk.util;

import org.orbit.platform.sdk.IProcess;
import org.origin.common.extensions.core.IExtension;

public class IProcessFilterByProgramExtension implements IProcessFilter {

	protected IExtension extension;

	public IProcessFilterByProgramExtension(IExtension extension) {
		this.extension = extension;
	}

	@Override
	public boolean accept(IProcess process) {
		IExtension extensionOfProcess = process.getAdapter(IExtension.class);
		if (this.extension.equals(extensionOfProcess)) {
			return true;
		}
		return false;
	}

}
