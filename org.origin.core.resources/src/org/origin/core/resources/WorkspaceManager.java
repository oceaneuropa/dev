package org.origin.core.resources;

import java.io.IOException;
import java.util.List;

import org.osgi.framework.BundleContext;

public interface WorkspaceManager {

	void start(BundleContext context);

	void stop(BundleContext context);

	List<String> getWorkspaces() throws IOException;

	IWorkspace openWorkspace(String name, String password) throws IOException;

	IWorkspace createWorkspace(String name, String password) throws IOException;

	boolean changeWorkspacePassword(String name, String oldPassword, String newPassword) throws IOException;

	boolean deleteWorkspace(String name, String password) throws IOException;

}
