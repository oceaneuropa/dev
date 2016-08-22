package org.nb.home.model.runtime.impl;

import org.nb.home.model.runtime.Workspace;
import org.nb.home.model.runtime.config.WorkspaceConfig;
import org.origin.common.adapter.AdaptorSupport;

public class WorkspaceImpl implements Workspace {

	protected WorkspaceConfig workspaceConfig;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	public WorkspaceConfig getWorkspaceConfig() {
		return this.workspaceConfig;
	}

	public void setWorkspaceConfig(WorkspaceConfig workspaceConfig) {
		this.workspaceConfig = workspaceConfig;
	}

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
		if (WorkspaceConfig.class.isAssignableFrom(clazz) && object instanceof WorkspaceConfig) {
			this.workspaceConfig = (WorkspaceConfig) object;
		}
		this.adaptorSupport.adapt(clazz, object);
	}

}
