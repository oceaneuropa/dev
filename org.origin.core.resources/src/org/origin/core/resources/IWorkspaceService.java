package org.origin.core.resources;

import java.io.IOException;
import java.util.List;

import org.osgi.framework.BundleContext;

public interface IWorkspaceService {

	void start(BundleContext context) throws IOException;

	void stop(BundleContext context) throws IOException;

	List<String> getWorkspaces() throws IOException;

	IWorkspace open(String name, String password) throws IOException;

	IWorkspace create(String name, String password) throws IOException;

	boolean changePassword(String name, String oldPassword, String newPassword) throws IOException;

	boolean delete(String name, String password) throws IOException;

}
