package org.nb.mgm.client.cli;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.IProjectHome;
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

public class ProjectHomeCommand implements Annotated {

	protected static String[] PROJECT_HOME_TITLES = new String[] { "Project", "ID", "Name", "Description" };

	protected BundleContext bundleContext;
	protected ServiceRegistration<?> registration;

	@Dependency
	protected Management management;

	/**
	 * 
	 * @param bundleContext
	 */
	public ProjectHomeCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("ProjectHomeCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "nb");
		props.put("osgi.command.function", new String[] { "lprojecthomes", "createprojecthome", "updateprojecthome", "deleteprojecthome" });
		this.registration = this.bundleContext.registerService(ProjectHomeCommand.class.getName(), this, props);

		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("ProjectHomeCommand.stop()");

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
	 * List ProjectHomes.
	 * 
	 * Command: lprojecthomes
	 * 
	 * @param projectId
	 * @throws ClientException
	 */
	@Descriptor("List ProjectHomes")
	public void lprojecthomes( //
			// parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId // optional
	) throws ClientException {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		List<IProjectHome> projectHomes = new ArrayList<IProjectHome>();

		List<IProject> projects = this.management.getProjects();
		for (IProject project : projects) {
			String currProjectId = project.getId();
			if ("".equals(projectId) || currProjectId.equals(projectId)) {
				List<IProjectHome> currProjectHomes = project.getProjectHomes();
				if (!currProjectHomes.isEmpty()) {
					projectHomes.addAll(currProjectHomes);
				}
			}
		}

		String[][] rows = new String[projectHomes.size()][PROJECT_HOME_TITLES.length];
		int rowIndex = 0;
		String prevProjectId = null;
		for (IProjectHome projectHome : projectHomes) {
			IProject project = projectHome.getProject();
			String currProjectId = project.getId();
			String projectText = project.getName() + " (" + currProjectId + ")";
			if (prevProjectId != null && prevProjectId.equals(currProjectId)) {
				projectText = "";
			}
			rows[rowIndex++] = new String[] { projectText, projectHome.getId(), projectHome.getName(), projectHome.getDescription() };

			prevProjectId = currProjectId;
		}

		PrettyPrinter.prettyPrint(PROJECT_HOME_TITLES, rows);
	}

	/**
	 * Create a ProjectHome.
	 * 
	 * Command: createprojecthome -projectid <projectId> -name <projectHomeName> -desc <projectHomeDescription>
	 * 
	 * @param projectId
	 * @param name
	 * @param description
	 */
	@Descriptor("Create ProjectHome")
	public void createprojecthome( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // required
			@Descriptor("ProjectHome Name") @Parameter(names = { "-name", "--name" }, absentValue = "") String name, // required
			@Descriptor("ProjectHome Description") @Parameter(names = { "-desc", "--description" }, absentValue = "") String description // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// projectId is required for creating new ProjectHome
			if ("".equals(projectId)) {
				System.out.println("Please specify -projectid parameter.");
				return;
			}

			// name is required for creating new ProjectHome
			if ("".equals(name)) {
				System.out.println("Please specify -name parameter.");
				return;
			}

			IProject project = this.management.getProject(projectId);
			if (project == null) {
				System.out.println("Project cannot be found.");
				return;
			}

			IProjectHome newProjectHome = project.addProjectHome(name, description);
			if (newProjectHome != null) {
				System.out.println("New ProjectHome is created. ");
			} else {
				System.out.println("New ProjectHome is not created.");
			}

		} catch (ClientException e) {
			System.out.println("Cannot create new ProjectHome. " + e.getMessage());
		}
	}

	/**
	 * Update ProjectHome.
	 * 
	 * Command: updateprojecthome -projectid <projectId> -projecthomeid <projectHomeId> -name <newProjectHomeName> -desc <newProjectHomeDescription>
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param newProjectHomeName
	 * @param newProjectHomeDescription
	 */
	@Descriptor("Update ProjectHome")
	public void updateprojecthome( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // optional
			@Descriptor("ProjectHome ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId, // required
			@Descriptor("New ProjectHome name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String newProjectHomeName, // optional
			@Descriptor("New ProjectHome description") @Parameter(names = { "-desc", "--description" }, absentValue = "null") String newProjectHomeDescription // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		// projectHomeId is required for updating a ProjectHome
		if ("".equals(projectHomeId)) {
			System.out.println("Please specify -projecthomeid parameter.");
			return;
		}

		// projectHomeName cannot be updated to empty string
		if ("".equals(newProjectHomeName)) {
			System.out.println("ProjectHome name cannot be updated to empty string.");
			return;
		}

		try {
			IProjectHome projectHome = null;
			if ("".equals(projectId)) {
				projectHome = this.management.getProjectHome(projectHomeId);
			} else {
				projectHome = this.management.getProjectHome(projectId, projectHomeId);
			}
			if (projectHome == null) {
				System.out.println("ProjectHome is not found.");
				return;
			}

			boolean isUpdated = false;
			if (!"null".equals(newProjectHomeName)) {
				projectHome.setName(newProjectHomeName);
				isUpdated = true;
			}
			if (!"null".equals(newProjectHomeDescription)) {
				projectHome.setDescription(newProjectHomeDescription);
				isUpdated = true;
			}

			if (isUpdated) {
				boolean succeed = projectHome.update();
				if (succeed) {
					System.out.println("ProjectHome is updated. ");
				} else {
					System.out.println("Failed to update ProjectHome.");
				}
			} else {
				System.out.println("ProjectHome is not updated.");
			}

		} catch (ClientException e) {
			System.out.println("ClientException: " + e.getMessage());
		}
	}

	/**
	 * Delete a ProjectHome.
	 * 
	 * Command: deleteprojecthome -projectid <projectId> -projecthomeid <projectHomeId>
	 * 
	 * @param projectId
	 * @param projectHomeId
	 */
	@Descriptor("Delete ProjectHome from Project")
	public void deleteprojecthome( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // optional
			@Descriptor("ProjectHome ID") @Parameter(names = { "-projecthomeid", "--projectHomeId" }, absentValue = "") String projectHomeId // required
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// projectHomeId is required for deleting a ProjectHome
			if ("".equals(projectHomeId)) {
				System.out.println("Please specify -projecthomeid parameter.");
				return;
			}

			boolean succeed = false;
			if ("".equals(projectId)) {
				// projectId is not specified
				succeed = this.management.deleteProjectHome(projectHomeId);
			} else {
				// projectId is specified
				succeed = this.management.deleteProjectHome(projectId, projectHomeId);
			}

			if (succeed) {
				System.out.println("ProjectHome is deleted. ");
			} else {
				System.out.println("Failed to delete ProjectHome.");
			}

		} catch (ClientException e) {
			System.out.println("Failed to delete ProjectHome. " + e.getMessage());
		}
	}

}
