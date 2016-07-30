package org.nb.mgm.client.cli;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.IProjectHome;
import org.nb.mgm.client.api.IProjectNode;
import org.nb.mgm.client.api.ISoftware;
import org.nb.mgm.client.api.ManagementClient;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.DateUtil;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ProjectNodeCommand implements Annotated {

	protected static String[] PROJECT_NODE_TITLES = new String[] { "ID", "Name", "Description" };
	protected static String[] PROJECT_NODE_ALL_TITLES = new String[] { "Project", "Home", "ID", "Name", "Description", "Installed Software" };
	protected static String[] SOFTWARE_TITLES = new String[] { "ID", "Type", "Name", "Version", "Description", "File Name", "Exists", "Length", "Last Modified" };

	protected BundleContext bundleContext;
	protected ServiceRegistration<?> registration;

	@Dependency
	protected ManagementClient management;

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
		props.put("osgi.command.function", new String[] { "lprojectnodes", "createprojectnode", "updateprojectnode", "deleteprojectnode", "lprojectnodesoftware", "installsoftware", "uninstallsoftware" });
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
			// Options
			@Descriptor("List with detailed information of each ProjectNode") @Parameter(names = { "-all", "--all" }, absentValue = "false", presentValue = "true") boolean all, //
			// parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // optional
			@Descriptor("ProjectHome ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId, // optional
			@Descriptor("ProjectNode ID") @Parameter(names = { "-projectnodeid", "--projectNodeId" }, absentValue = "") String projectNodeId // optional
	) throws ClientException {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		all = true;

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

		if (all) {
			List<String[]> items = new ArrayList<String[]>();
			for (IProjectNode projectNode : projectNodes) {
				IProject project = projectNode.getProject();
				String currProjectId = project.getId();

				IProjectHome projectHome = projectNode.getProjectHome();
				String currProjectHomeName = projectHome.getName();

				String currProjectNodeId = projectNode.getId();
				String projectNodeName = projectNode.getName();
				String projectNodeDesc = projectNode.getDescription();

				// String projectNodeIdText = "[" + currProjectId + "]/[" + currProjectHomeName + "]/" + currProjectNodeId;
				// String projectNodeNameText = "[" + currProjectId + "]/[" + currProjectHomeName + "]/" + projectNodeName;
				String projectNodeIdText = currProjectNodeId;
				String projectNodeNameText = projectNodeName;

				List<ISoftware> softwareList = projectNode.getSoftware();
				if (softwareList.isEmpty()) {
					String[] item = new String[] { currProjectId, currProjectHomeName, projectNodeIdText, projectNodeNameText, projectNodeDesc, "" };
					items.add(item);

				} else {
					for (int i = 0; i < softwareList.size(); i++) {
						ISoftware currSoftware = softwareList.get(i);
						String softwareId = currSoftware.getId();
						String softwareName = currSoftware.getName();
						String softwareVersion = currSoftware.getVersion();
						String softwareText = "(" + softwareId + ") " + softwareName + " (" + softwareVersion + ")";

						String[] item = null;
						if (i == 0) {
							item = new String[] { currProjectId, currProjectHomeName, projectNodeIdText, projectNodeNameText, projectNodeDesc, softwareText };
						} else {
							item = new String[] { "", "", "", "", "", softwareText };
						}
						items.add(item);
					}
				}
			}

			String[][] rows = new String[items.size()][PROJECT_NODE_ALL_TITLES.length];
			int rowIndex = 0;
			for (String[] item : items) {
				rows[rowIndex++] = item;
			}
			PrettyPrinter.prettyPrint(PROJECT_NODE_ALL_TITLES, rows, projectNodes.size());

		} else {
			String[][] rows = new String[projectNodes.size()][PROJECT_NODE_TITLES.length];
			int rowIndex = 0;
			// String prevProjectId = null;
			// String prevProjectHomeId = null;
			for (IProjectNode projectNode : projectNodes) {
				IProject project = projectNode.getProject();
				String currProjectId = project.getId();

				IProjectHome projectHome = projectNode.getProjectHome();
				// String currProjectHomeId = projectHome.getId();
				String currProjectHomeName = projectHome.getName();

				// String projectText = project.getName() + " (" + currProjectId + ")";
				// String projectHomeText = projectHome.getName() + " (" + currProjectHomeId + ")";
				// if (prevProjectId != null && prevProjectId.equals(currProjectId)) {
				// projectText = "";
				// }
				// if (prevProjectHomeId != null && prevProjectHomeId.equals(currProjectHomeId)) {
				// projectHomeText = "";
				// }

				String currProjectNodeId = projectNode.getId();
				String projectNodeName = projectNode.getName();
				String projectNodeDesc = projectNode.getDescription();

				String projectNodeIdText = "[" + currProjectId + "]/[" + currProjectHomeName + "]/" + currProjectNodeId;
				String projectNodeNameText = projectNodeName;

				rows[rowIndex++] = new String[] { projectNodeIdText, projectNodeNameText, projectNodeDesc };
				// prevProjectId = currProjectId;
				// prevProjectHomeId = currProjectHomeId;
			}
			PrettyPrinter.prettyPrint(PROJECT_NODE_TITLES, rows, projectNodes.size());
		}
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
				System.out.println("New ProjectNode is created.");
			} else {
				System.out.println("New ProjectNode is not created.");
			}
		} catch (ClientException e) {
			System.out.println("Cannot create new ProjectNode. " + e.getMessage());
		}
	}

	/**
	 * Update a ProjectNode.
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
			@Descriptor("ProjectHome ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId, // optional
			@Descriptor("ProjectNode ID") @Parameter(names = { "-projectnodeid", "--projectNodeId" }, absentValue = "") String projectNodeId, // required
			@Descriptor("New ProjectNode name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String newProjectNodeName, // optional
			@Descriptor("New ProjectNode description") @Parameter(names = { "-desc", "--description" }, absentValue = "null") String newProjectNodeDescription // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		// projectHomeId is required
		if ("".equals(projectHomeId)) {
			System.out.println("Please specify -projecthomeid parameter.");
			return;
		}

		// projectNodeId is required
		if ("".equals(projectNodeId)) {
			System.out.println("Please specify -projectnodeid parameter.");
			return;
		}

		try {
			IProjectNode projectNode = this.management.getProjectNode(projectHomeId, projectNodeId);
			if (projectNode == null) {
				System.out.println("ProjectNode is not found.");
				return;
			}

			boolean isUpdated = false;
			if (!"null".equals(newProjectNodeName)) {
				// projectNodeName cannot be empty if specified
				if ("".equals(newProjectNodeName)) {
					System.out.println("ProjectNode name cannot be updated to empty string.");
					return;
				}

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
					System.out.println("ProjectNode is updated.");
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
			@Descriptor("ProjectHome ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId, // optional
			@Descriptor("ProjectNode ID") @Parameter(names = { "-projectnodeid", "--projectNodeId" }, absentValue = "") String projectNodeId // required
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// projectNodeId is required
			if ("".equals(projectNodeId)) {
				System.out.println("Please specify -projectnodeid parameter.");
				return;
			}

			boolean succeed = this.management.deleteProjectNode(projectHomeId, projectNodeId);
			if (succeed) {
				System.out.println("ProjectNode is deleted.");
			} else {
				System.out.println("Failed to delete ProjectNode.");
			}

		} catch (ClientException e) {
			System.out.println("Failed to delete ProjectNode. " + e.getMessage());
		}
	}

	/**
	 * List ProjectNode Software.
	 * 
	 * Command: lprojectnodesoftware
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @throws ClientException
	 */
	@Descriptor("List ProjectNode Software")
	public void lprojectnodesoftware( //
			// parameters
			@Descriptor("ProjectHome ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId, // optional
			@Descriptor("ProjectNode ID") @Parameter(names = { "-projectnodeid", "--projectNodeId" }, absentValue = "") String projectNodeId // optional
	) throws ClientException {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		// // projectHomeId is required
		// if ("".equals(projectHomeId)) {
		// System.out.println("Please specify -projecthomeid parameter.");
		// return;
		// }
		// // projectNodeId is required
		// if ("".equals(projectNodeId)) {
		// System.out.println("Please specify -projectnodeid parameter.");
		// return;
		// }

		List<IProjectNode> matchedProjectNodes = new ArrayList<IProjectNode>();

		List<IProject> projects = this.management.getProjects();
		for (IProject project : projects) {
			List<IProjectHome> projectHomes = project.getProjectHomes();
			for (IProjectHome projectHome : projectHomes) {
				String currProjectHomeId = projectHome.getId();

				boolean matchProjectHome = false;
				if ("".equals(projectHomeId) || currProjectHomeId.equals(projectHomeId)) {
					matchProjectHome = true;
				}

				if (matchProjectHome) {
					List<IProjectNode> projectNodes = projectHome.getProjectNodes();
					for (IProjectNode projectNode : projectNodes) {
						String currProjectNodeId = projectNode.getId();

						boolean matchProjectNode = false;
						if ("".equals(projectNodeId) || currProjectNodeId.equals(projectNodeId)) {
							matchProjectNode = true;
						}

						if (matchProjectNode) {
							matchedProjectNodes.add(projectNode);
						}
					}
				}
			}
		}

		for (IProjectNode projectNode : matchedProjectNodes) {
			// IProjectNode projectNode = this.management.getProjectNode(projectHomeId, projectNodeId);
			// if (projectNode == null) {
			// System.out.println("ProjectNode is not found.");
			// return;
			// }

			IProject project = projectNode.getProject();
			if (project == null) {
				System.out.println("Project is not found.");
				return;
			}
			String projectId = project.getId();

			IProjectHome projectHome = projectNode.getProjectHome();
			if (projectHome == null) {
				System.out.println("ProjectHome is not found.");
				return;
			}

			String projectHomeName = projectHome.getName();
			String title = "[/" + projectId + "/" + projectHomeName + "/" + projectNode.getName() + "] software:";

			List<ISoftware> softwareList = this.management.getProjectNodeSoftware(projectId, projectHome.getId(), projectNode.getId());
			String[][] rows = new String[softwareList.size()][SOFTWARE_TITLES.length];
			int rowIndex = 0;
			// String prevProjectId = null;
			for (ISoftware software : softwareList) {
				// IProject project = software.getProject();
				// String currProjectId = project.getId();
				// String projectText = project.getName() + " (" + currProjectId + ")";
				// if (prevProjectId != null && prevProjectId.equals(currProjectId)) {
				// projectText = "";
				// }

				String fileName = software.getFileName() != null ? software.getFileName() : "n/a";
				String exists = software.exists() ? "true" : "false";
				String lengthText = String.valueOf(software.getLength());
				String lastModifiedText = software.getLastModified() != null ? DateUtil.toString(software.getLastModified(), DateUtil.getJdbcDateFormat()) : "n/a";

				rows[rowIndex++] = new String[] { software.getId(), software.getType(), software.getName(), software.getVersion(), software.getDescription(), fileName, exists, lengthText, lastModifiedText };
				// prevProjectId = currProjectId;
			}

			if (rows.length > 0) {
				System.out.println(title);
				PrettyPrinter.prettyPrint(SOFTWARE_TITLES, rows);
				System.out.println();
			}
		}
	}

	/**
	 * Install Software to ProjectNode
	 * 
	 * Command: installsoftware -projecthomeid <projectHomeId> -projectnodeid <projectNodeId> -softwareid <softwareId>
	 * 
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param softwareId
	 */
	@Descriptor("Install Software to ProjectNode")
	public void installsoftware( //
			// Parameters
			@Descriptor("ProjectHome ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId, // optional
			@Descriptor("ProjectNode ID") @Parameter(names = { "-projectnodeid", "--projectNodeId" }, absentValue = "") String projectNodeId, // required
			@Descriptor("Software ID") @Parameter(names = { "-softwareid", "--softwareId" }, absentValue = "") String softwareId // required
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		// projectHomeId is required
		if ("".equals(projectHomeId)) {
			System.out.println("Please specify -projecthomeid parameter.");
			return;
		}

		// projectNodeId is required
		if ("".equals(projectNodeId)) {
			System.out.println("Please specify -projectnodeid parameter.");
			return;
		}

		try {
			IProjectNode projectNode = this.management.getProjectNode(projectHomeId, projectNodeId);
			if (projectNode == null) {
				System.out.println("ProjectNode is not found.");
				return;
			}

			boolean succeed = projectNode.installSoftware(softwareId);
			if (succeed) {
				System.out.println("Software is installed to ProjectNode.");
			} else {
				System.out.println("Failed to install software to ProjectNode.");
			}
		} catch (ClientException e) {
			System.out.println("Cannot install software to ProjectNode: " + e.getMessage());
		}
	}

	/**
	 * Uninstall Software from ProjectNode
	 * 
	 * Command: uninstallsoftware -projecthomeid <projectHomeId> -projectnodeid <projectNodeId> -softwareid <softwareId>
	 * 
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param softwareId
	 */
	@Descriptor("Uninstall Software from ProjectNode")
	public void uninstallsoftware( //
			// Parameters
			@Descriptor("ProjectHome ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId, // optional
			@Descriptor("ProjectNode ID") @Parameter(names = { "-projectnodeid", "--projectNodeId" }, absentValue = "") String projectNodeId, // required
			@Descriptor("Software ID") @Parameter(names = { "-softwareid", "--softwareId" }, absentValue = "") String softwareId // required
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		// projectHomeId is required
		if ("".equals(projectHomeId)) {
			System.out.println("Please specify -projecthomeid parameter.");
			return;
		}

		// projectNodeId is required
		if ("".equals(projectNodeId)) {
			System.out.println("Please specify -projectnodeid parameter.");
			return;
		}

		try {
			IProjectNode projectNode = this.management.getProjectNode(projectHomeId, projectNodeId);
			if (projectNode == null) {
				System.out.println("ProjectNode is not found.");
				return;
			}

			boolean succeed = projectNode.uninstallSoftware(softwareId);
			if (succeed) {
				System.out.println("Software is uninstalled from ProjectNode.");
			} else {
				System.out.println("Failed to uninstall software from ProjectNode.");
			}
		} catch (ClientException e) {
			System.out.println("Cannot uninstall software from ProjectNode: " + e.getMessage());
		}
	}

}
