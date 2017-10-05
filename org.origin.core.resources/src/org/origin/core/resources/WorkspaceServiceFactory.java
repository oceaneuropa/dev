package org.origin.core.resources;

import java.io.File;

public class WorkspaceManagerFactory {

	private static WorkspaceManagerFactory INSTANCE = new WorkspaceManagerFactory();

	public static WorkspaceManagerFactory getInstance() {
		return INSTANCE;
	}

	public WorkspaceManager createWorkspaceManagerForDatabase(String id) {
		return null;
	}

	public WorkspaceManager createWorkspaceManagerForFileSystem(File workspacesFolder) {
		return null;
	}

}
