package org.nb.mgm.client.cli;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.IProjectHome;
import org.nb.mgm.client.api.IProjectNode;
import org.nb.mgm.client.api.Management;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ProjectNodeCommand implements Annotated {

	protected static String[] PROJECT_NODE_TITLES = new String[] { "Project", "ProjectHome", "ID", "Name", "Description" };

	protected BundleContext bundleContext;
	protected ServiceRegistration<?> registration;

	@Dependency
	protected Management management;

	/**
	 * 
	 * @param bundleContext
	 */
	public ProjectNodeCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("ProjectNodeCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "nb");
		props.put("osgi.command.function", new String[] { "lprojectnodes", "createprojectnode", "updateprojectnode", "deleteprojectnode" });
		this.registration = this.bundleContext.registerService(ProjectNodeCommand.class.getName(), this, props);

		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("ProjectNodeCommand.stop()");

		if (this.registration != null) {
			this.registration.unregister();
			this.registration = null;
		}

		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void managementSet() {
	}

	@DependencyUnfullfilled
	public void managementUnset() {
	}

	/**
	 * List ProjectNodes.
	 * 
	 * Command: lprojectnodes
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @throws ClientException
	 */
	@Descriptor("List ProjectNodes")
	public void lprojectnodes( //
			// parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // optional
			@Descriptor("ProjectHome ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId, // optional
			@Descriptor("ProjectNode ID") @Parameter(names = { "-projectnodeid", "--projectNodeId" }, absentValue = "") String projectNodeId // optional
	) throws ClientException {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		List<IProjectNode> projectNodes = new ArrayList<IProjectNode>();

		List<IProject> projects = this.management.getProjects();
		for (IProject project : projects) {
			String currProjectId = project.getId();
			if ("".equals(projectId) || currProjectId.equals(projectId)) {

				List<IProjectHome> currProjectHomes = project.getProjectHomes();
				for (IProjectHome currProjectHome : currProjectHomes) {
					String currProjectHomeId = currProjectHome.getId();

					if ("".equals(projectHomeId) || currProjectHomeId.equals(projectHomeId)) {
						List<IProjectNode> currProjectNodes = currProjectHome.getProjectNodes();

						if ("".equals(projectNodeId)) {
							if (!currProjectNodes.isEmpty()) {
								projectNodes.addAll(currProjectNodes);
							}
						} else {
							for (IProjectNode currProjectNode : currProjectNodes) {
								String currProjectNodeId = currProjectNode.getId();
								if (projectNodeId.equals(currProjectNodeId)) {
									projectNodes.add(currProjectNode);
								}
							}
						}
					}
				}
			}
		}

		String[][] rows = new String[projectNodes.size()][PROJECT_NODE_TITLES.length];
		int rowIndex = 0;
		String prevProjectId = null;
		String prevProjectHomeId = null;
		for (IProjectNode projectNode : projectNodes) {
			IProject project = projectNode.getProject();
			String currProjectId = project.getId();

			IProjectHome projectHome = projectNode.getProjectHome();
			String currProjectHomeId = projectHome.getId();

			String projectText = project.getName() + " (" + currProjectId + ")";
			String projectHomeText = projectHome.getName() + " (" + currProjectHomeId + ")";
			if (prevProjectId != null && prevProjectId.equals(currProjectId)) {
				projectText = "";
			}
			if (prevProjectHomeId != null && prevProjectHomeId.equals(currProjectHomeId)) {
				projectHomeText = "";
			}

			rows[rowIndex++] = new String[] { projectText, projectHomeText, projectNode.getId(), projectNode.getName(), projectNode.getDescription() };

			prevProjectId = currProjectId;
			prevProjectHomeId = currProjectHomeId;
		}

		PrettyPrinter.prettyPrint(PROJECT_NODE_TITLES, rows);
	}

	/**
	 * Create a ProjectNode.
	 * 
	 * Command: createprojectnode -projectid <projectId> -projecthomeid <projectHomeId> -projectnodeid <projectNodeId> -name <projectNodeName> -desc
	 * <projectNodeDescription>
	 * 
	 * @param projectId
	 * @param name
	 * @param description
	 */
	@Descriptor("Create ProjectNode")
	public void createprojectnode( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // optional
			@Descriptor("ProjectHome ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId, // required
			@Descriptor("ProjectNode ID") @Parameter(names = { "-projectnodeid", "--projectNodeId" }, absentValue = "") String projectNodeId, // required
			@Descriptor("ProjectNode Name") @Parameter(names = { "-name", "--name" }, absentValue = "") String name, // required
			@Descriptor("ProjectNode Description") @Parameter(names = { "-desc", "--description" }, absentValue = "") String description // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// projectHomeId is required for creating new ProjectNode
			if ("".equals(projectHomeId)) {
				System.out.println("Please specify -projecthomeid parameter.");
				return;
			}

			// projectNodeId is required for creating new ProjectNode
			if ("".equals(projectNodeId)) {
				System.out.println("Please specify -projectnodeid parameter.");
				return;
			}

			// name is required for creating new ProjectNode
			if ("".equals(name)) {
				System.out.println("Please specify -name parameter.");
				return;
			}

			IProjectHome projectHome = null;
			if ("".equals(projectId)) {
				// projectId is not specified
				projectHome = this.management.getProjectHome(projectHomeId);
			} else {
				// projectId is specified
				projectHome = this.management.getProjectHome(projectId, projectHomeId);
			}
			if (projectHome == null) {
				System.out.println("ProjectHome cannot be found.");
				return;
			}

			IProjectNode newProjectNode = projectHome.addProjectNode(projectNodeId, name, description);
			if (newProjectNode != null) {
				System.out.println("New ProjectNode is created. ");
			} else {
				System.out.println("New ProjectNode is not created.");
			}
		} catch (ClientException e) {
			System.out.println("Cannot create new ProjectNode. " + e.getMessage());
		}
	}

	/**
	 * Update ProjectNode information.
	 * 
	 * Command: updateprojectnode -projectid <projectId> -projecthomeid <projectHomeId> -projectnodeid <projectNodeId> -name
	 * <newProjectNodeName> -desc <newProjectNodeDescription>
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param newProjectNodeName
	 * @param newProjectNodeDescription
	 */
	@Descriptor("Update ProjectNode")
	public void updateprojectnode( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // optional
			@Descriptor("Project ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId, // optional
			@Descriptor("ProjectNode ID") @Parameter(names = { "-projectnodeid", "--projectNodeId" }, absentValue = "") String projectNodeId, // required
			@Descriptor("New ProjectNode name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String newProjectNodeName, // optional
			@Descriptor("New ProjectNode description") @Parameter(names = { "-desc", "--description" }, absentValue = "null") String newProjectNodeDescription // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		// projectHomeId is required for updating a ProjectHome
		if ("".equals(projectNodeId)) {
			System.out.println("Please specify -projectnodeid parameter.");
			return;
		}

		// projectNodeName cannot be updated to empty string
		if ("".equals(newProjectNodeName)) {
			System.out.println("ProjectNode name cannot be updated to empty string.");
			return;
		}

		try {
			IProjectNode projectNode = null;
			if (!"".equals(projectHomeId)) {
				projectNode = this.management.getProjectNode(projectId, projectHomeId, projectNodeId);
			} else {
				projectNode = this.management.getProjectNode(projectNodeId);
			}
			if (projectNode == null) {
				System.out.println("ProjectNode is not found.");
				return;
			}

			boolean isUpdated = false;
			if (!"null".equals(newProjectNodeName)) {
				projectNode.setName(newProjectNodeName);
				isUpdated = true;
			}
			if (!"null".equals(newProjectNodeDescription)) {
				projectNode.setDescription(newProjectNodeDescription);
				isUpdated = true;
			}

			if (isUpdated) {
				boolean succeed = projectNode.update();
				if (succeed) {
					System.out.println("ProjectNode is updated. ");
				} else {
					System.out.println("Failed to update ProjectNode.");
				}
			} else {
				System.out.println("ProjectNode is not updated.");
			}

		} catch (ClientException e) {
			System.out.println("ClientException: " + e.getMessage());
		}
	}

	/**
	 * Delete a ProjectNode.
	 * 
	 * Command: deleteprojectnode -projectid <projectId> -projecthomeid <projectHomeId> -projectnodeid <projectNodeId>
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 */
	@Descriptor("Delete ProjectNode from ProjectHome")
	public void deleteprojectnode( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // optional
			@Descriptor("ProjectHome ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId, // optional
			@Descriptor("ProjectNode ID") @Parameter(names = { "-projectnodeid", "--projectNodeId" }, absentValue = "") String projectNodeId // required
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// projectNodeId is required for deleting a ProjectNode
			if ("".equals(projectNodeId)) {
				System.out.println("Please specify -projectnodeid parameter.");
				return;
			}

			boolean succeed = false;
			if ("".equals(projectHomeId)) {
				// projectHomeId is not specified
				succeed = this.management.deleteProjectNode(projectNodeId);
			} else {
				// projectHomeId is specified
				succeed = this.management.deleteProjectNode(projectId, projectHomeId, projectNodeId);
			}

			if (succeed) {
				System.out.println("ProjectNode is deleted. ");
			} else {
				System.out.println("Failed to delete ProjectNode.");
			}

		} catch (ClientException e) {
			System.out.println("Failed to delete ProjectNode. " + e.getMessage());
		}
	}

}
