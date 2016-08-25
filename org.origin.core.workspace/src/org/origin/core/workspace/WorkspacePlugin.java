package org.origin.core.workspace;

import java.io.File;
import java.nio.file.Path;

import org.origin.common.env.SetupUtil;
import org.origin.core.workspace.impl.WorkspaceManagementImpl;
import org.origin.core.workspace.internal.resource.FolderDescriptionResourceFactory;
import org.origin.core.workspace.internal.resource.ProjectDescriptionResourceFactory;
import org.origin.core.workspace.internal.resource.WorkspaceDescriptionResourceFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class WorkspacePlugin implements BundleActivator {

	/**
	 * The single instance of this plug-in runtime class.
	 */
	private static WorkspacePlugin plugin;
	private static BundleContext context;
	private static WorkspaceManagement workspaceManagement;

	public static BundleContext getContext() {
		return context;
	}

	/**
	 * Returns the Resources plug-in.
	 *
	 * @return the single instance of this plug-in runtime class
	 */
	public static WorkspacePlugin getPlugin() {
		return plugin;
	}

	public static WorkspaceManagement getWorkspaceManagement() {
		return workspaceManagement;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		WorkspaceDescriptionResourceFactory.register();
		ProjectDescriptionResourceFactory.register();
		FolderDescriptionResourceFactory.register();

		Path homePath = SetupUtil.getOriginHome(bundleContext);
		Path workspacesPath = homePath.resolve("workspaces");
		File workspacesDir = workspacesPath.toFile();
		if (!workspacesDir.exists()) {
			workspacesDir.mkdirs();
		}

		WorkspacePlugin.workspaceManagement = new WorkspaceManagementImpl(workspacesDir);
		WorkspacePlugin.context = bundleContext;
		WorkspacePlugin.plugin = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		WorkspacePlugin.plugin = null;
		WorkspacePlugin.context = null;
		WorkspacePlugin.workspaceManagement = null;

		WorkspaceDescriptionResourceFactory.unregister();
		ProjectDescriptionResourceFactory.unregister();
		FolderDescriptionResourceFactory.unregister();
	}

}
