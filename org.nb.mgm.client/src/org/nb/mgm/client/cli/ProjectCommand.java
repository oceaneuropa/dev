package org.nb.mgm.client.cli;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.IProjectHome;
import org.nb.mgm.client.api.ISoftware;
import org.nb.mgm.client.api.ManagementClient;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ProjectCommand implements Annotated {

	protected static String[] PROJECT_TITLES = new String[] { "ID", "Name", "Description" };
	protected static String[] PROJECT_TITLES_ALL = new String[] { "ID", "Name", "Description", "Home", "Available Software" };

	protected BundleContext bundleContext;
	protected ServiceRegistration<?> registration;

	@Dependency
	protected ManagementClient mgmClient;

	/**
	 * 
	 * @param bundleContext
	 */
	public ProjectCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("ProjectCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "nb");
		props.put("osgi.command.function", new String[] { "lprojects", "createproject", "updateproject", "deleteproject" });
		this.registration = this.bundleContext.registerService(ProjectCommand.class.getName(), this, props);

		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("ProjectCommand.stop()");

		if (this.registration != null) {
			this.registration.unregister();
			this.registration = null;
		}

		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void managementSet() {
		// System.out.println("ProjectCommand.managementSet()");
	}

	@DependencyUnfullfilled
	public void managementUnset() {
		// System.out.println("ProjectCommand.managementUnset()");
	}

	/**
	 * List Projects.
	 * 
	 * Command: lprojects
	 * 
	 * @param mgm
	 * @throws ClientException
	 */
	@Descriptor("List Projects")
	public void lprojects(
			// Options
			@Descriptor("List with detailed information of each Project") @Parameter(names = { "-all", "--all" }, absentValue = "false", presentValue = "true") boolean all //
	) throws ClientException {
		if (this.mgmClient == null) {
			System.out.println("Please login first.");
			return;
		}

		all = true;

		List<IProject> projects = this.mgmClient.getProjects();
		if (all) {
			List<String[]> items = new ArrayList<String[]>();
			for (IProject project : projects) {
				String projectId = project.getId();
				String name = project.getName();
				String desc = project.getDescription();

				List<IProjectHome> projectHomes = project.getProjectHomes();
				int projectHomeSize = projectHomes.size();

				List<ISoftware> softwareList = project.getSoftware();
				int softwareSize = softwareList.size();

				// get the larger size
				int size = projectHomeSize >= softwareSize ? projectHomeSize : softwareSize;
				if (size == 0) {
					String[] item = new String[] { projectId, name, desc, "", "" };
					items.add(item);

				} else {
					for (int i = 0; i < size; i++) {
						IProjectHome projectHome = i < projectHomes.size() ? projectHomes.get(i) : null;
						ISoftware software = i < softwareList.size() ? softwareList.get(i) : null;

						String projectHomeText = null;
						if (projectHome != null) {
							String projectHomeId = projectHome.getId();
							String projectHomeName = projectHome.getName();
							projectHomeText = "(" + projectHomeId + ") " + projectHomeName;
						} else {
							projectHomeText = "";
						}

						String softwareText = null;
						if (software != null) {
							String softwareId = software.getId();
							String softwareName = software.getName();
							String softwareVersion = software.getVersion();
							softwareText = "(" + softwareId + ") " + softwareName + " (" + softwareVersion + ")";
						} else {
							softwareText = "";
						}

						String[] item = null;
						if (i == 0) {
							item = new String[] { projectId, name, desc, projectHomeText, softwareText };
						} else {
							item = new String[] { "", "", "", projectHomeText, softwareText };
						}
						items.add(item);
					}
				}
			}

			String[][] rows = new String[items.size()][PROJECT_TITLES_ALL.length];
			int rowIndex = 0;
			for (String[] item : items) {
				rows[rowIndex++] = item;
			}
			PrettyPrinter.prettyPrint(PROJECT_TITLES_ALL, rows);

		} else {
			String[][] rows = new String[projects.size()][PROJECT_TITLES.length];
			int rowIndex = 0;
			for (IProject project : projects) {
				rows[rowIndex++] = new String[] { project.getId(), project.getName(), project.getDescription() };
			}
			PrettyPrinter.prettyPrint(PROJECT_TITLES, rows);
		}
	}

	/**
	 * Create a Project.
	 * 
	 * Command: createproject -projectid <projectId> -name <projectName> -desc <projectDescription>
	 * 
	 * @param projectId
	 * @param name
	 * @param description
	 */
	@Descriptor("Create Project")
	public void createproject( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // required
			@Descriptor("Project Name") @Parameter(names = { "-name", "--name" }, absentValue = "") String name, // required
			@Descriptor("Project Description") @Parameter(names = { "-desc", "--description" }, absentValue = "") String description // optional
	) {
		if (this.mgmClient == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// projectId is required for creating new Project
			if ("".equals(projectId)) {
				System.out.println("Please specify -id parameter.");
				return;
			}

			// name is required for creating new Project
			if ("".equals(name)) {
				System.out.println("Please specify -name parameter.");
				return;
			}

			IProject newProject = this.mgmClient.addProject(projectId, name, description);
			if (newProject != null) {
				System.out.println("New Project is created. ");
			} else {
				System.out.println("New Project is not created.");
			}

		} catch (ClientException e) {
			System.out.println("Cannot create new Project. " + e.getMessage());
		}
	}

	/**
	 * Update Project information.
	 * 
	 * Command: updateproject -projectid <projectId> -name <newProjectName> -desc <newProjectDescription>
	 * 
	 * @param projectId
	 */
	@Descriptor("Update Project")
	public void updateproject( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // required
			@Descriptor("New project name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String newProjectName, // optional
			@Descriptor("New project description") @Parameter(names = { "-desc", "--description" }, absentValue = "null") String newProjectDescription // optional
	) {
		if (this.mgmClient == null) {
			System.out.println("Please login first.");
			return;
		}

		// projectId is required for updating a Project
		if ("".equals(projectId)) {
			System.out.println("Please specify -projectid parameter.");
			return;
		}

		try {
			IProject project = this.mgmClient.getProject(projectId);
			if (project == null) {
				System.out.println("Project is not found.");
				return;
			}

			boolean isUpdated = false;
			if (!"null".equals(newProjectName)) {
				project.setName(newProjectName);
				isUpdated = true;
			}
			if (!"null".equals(newProjectDescription)) {
				project.setDescription(newProjectDescription);
				isUpdated = true;
			}

			if (isUpdated) {
				boolean succeed = project.update();
				if (succeed) {
					System.out.println("Project is updated. ");
				} else {
					System.out.println("Failed to update Project.");
				}
			} else {
				System.out.println("Project is not updated.");
			}

		} catch (ClientException e) {
			System.out.println("ClientException: " + e.getMessage());
		}
	}

	/**
	 * Delete a Project.
	 * 
	 * Command: deleteproject -projectid <projectId>
	 * 
	 * @param projectId
	 */
	@Descriptor("Delete Project")
	public void deleteproject( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId // required
	) {
		if (this.mgmClient == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// projectId is required for deleting a Project
			if ("".equals(projectId)) {
				System.out.println("Please specify -projectid parameter.");
				return;
			}

			boolean succeed = this.mgmClient.deleteProject(projectId);
			if (succeed) {
				System.out.println("Project is deleted. ");
			} else {
				System.out.println("Failed to delete Project.");
			}

		} catch (ClientException e) {
			System.out.println("Failed to delete Project. " + e.getMessage());
		}
	}

}
