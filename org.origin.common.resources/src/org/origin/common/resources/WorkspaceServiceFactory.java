package org.origin.common.resources;

import java.io.File;
import java.util.Map;

import org.origin.common.resources.impl.database.WorkspaceServiceDatabaseImpl;
import org.origin.common.resources.impl.local.WorkspaceServiceLocalImpl;

public class WorkspaceServiceFactory {

	private static WorkspaceServiceFactory INSTANCE = new WorkspaceServiceFactory();

	public static WorkspaceServiceFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public IWorkspaceService createWorkspaceService(Map<Object, Object> properties) {
		return new WorkspaceServiceDatabaseImpl(properties);
	}

	// /**
	// *
	// * @param fileSystem
	// * @return
	// */
	// public IWorkspaceService createWorkspaceService(FileSystem fileSystem) {
	// return new WorkspaceServiceFileSystemImpl(fileSystem);
	// }

	/**
	 * 
	 * @param workspacesFolder
	 * @return
	 */
	public IWorkspaceService createWorkspaceService(File workspacesFolder) {
		return new WorkspaceServiceLocalImpl(workspacesFolder);
	}

}
