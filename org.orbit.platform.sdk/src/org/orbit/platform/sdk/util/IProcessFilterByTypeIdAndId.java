package org.orbit.platform.sdk.util;

import org.orbit.platform.sdk.IProcess;
import org.origin.common.extensions.core.IExtension;

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
		IExtension extension = process.getAdapter(IExtension.class);
		String currTypeId = extension.getTypeId();
		String currExtensionId = extension.getId();
		if (this.typeId.equals(currTypeId) && this.extensionId.equals(currExtensionId)) {
			return true;
		}
		return false;
	}

}
