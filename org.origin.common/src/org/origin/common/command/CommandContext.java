package org.origin.common.command;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;

public class CommandContext implements IAdaptable {

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		adaptorSupport.adapt(clazz, object);
	}

}
