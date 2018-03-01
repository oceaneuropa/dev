package org.orbit.platform.sdk.util;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.IProcessFilter;
import org.orbit.platform.sdk.extension.IProgramExtension;

public class IProcessFilterByTypeIdAndId implements IProcessFilter {

	protected String typeId;
	protected String extensionId;

	/**
	 * 
	 * @param typeId
	 * @param extensionId
	 */
	public IProcessFilterByTypeIdAndId(String typeId, String extensionId) {
		this.typeId = typeId;
		this.extensionId = extensionId;
	}

	@Override
	public boolean accept(IProcess process) {
		IProgramExtension extension = process.getAdapter(IProgramExtension.class);
		String currTypeId = extension.getTypeId();
		String currExtensionId = extension.getId();
		if (this.typeId.equals(currTypeId) && this.extensionId.equals(currExtensionId)) {
			return true;
		}
		return false;
	}

}
