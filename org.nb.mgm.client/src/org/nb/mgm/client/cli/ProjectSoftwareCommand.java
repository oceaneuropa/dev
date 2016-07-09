package org.nb.mgm.client.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.ISoftware;
import org.nb.mgm.client.api.Management;
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

public class ProjectSoftwareCommand implements Annotated {

	protected static String[] SOFTWARE_TITLES = new String[] { "Project", "ID", "Type", "Name", "Version", "Description", "Length", "Last Modified", "Local Path" };

	protected BundleContext bundleContext;
	protected ServiceRegistration<?> registration;

	@Dependency
	protected Management management;

	/**
	 * 
	 * @param bundleContext
	 */
	public ProjectSoftwareCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("ProjectSoftwareCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "nb");
		props.put("osgi.command.function", new String[] { "lprojectsoftware", "addprojectsoftware", "updateprojectsoftware", "deleteprojectsoftware", "uploadprojectsoftware" });
		this.registration = this.bundleContext.registerService(ProjectSoftwareCommand.class.getName(), this, props);

		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("ProjectSoftwareCommand.stop()");

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
	 * List Project Software.
	 * 
	 * Command: lprojectsoftware
	 * 
	 * @param projectId
	 * @throws ClientException
	 */
	@Descriptor("List Project Software")
	public void lprojectsoftware( //
			// parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId // optional
	) throws ClientException {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		List<ISoftware> softwareList = new ArrayList<ISoftware>();

		List<IProject> projects = this.management.getProjects();
		for (IProject project : projects) {
			String currProjectId = project.getId();
			if ("".equals(projectId) || currProjectId.equals(projectId)) {
				List<ISoftware> currSoftwareList = project.getProjectSoftware();
				if (!currSoftwareList.isEmpty()) {
					softwareList.addAll(currSoftwareList);
				}
			}
		}

		String[][] rows = new String[softwareList.size()][SOFTWARE_TITLES.length];
		int rowIndex = 0;
		String prevProjectId = null;
		for (ISoftware software : softwareList) {
			IProject project = software.getProject();
			String currProjectId = project.getId();
			String projectText = project.getName() + " (" + currProjectId + ")";
			if (prevProjectId != null && prevProjectId.equals(currProjectId)) {
				projectText = "";
			}

			String lengthText = String.valueOf(software.getLength());
			String lastModifiedText = software.getLastModified() != null ? DateUtil.toString(software.getLastModified(), DateUtil.getJdbcDateFormat()) : "";
			// String md5Text = software.getMd5() != null ? software.getMd5() : "";
			String localPath = software.getLocalPath() != null ? software.getLocalPath() : "";
			rows[rowIndex++] = new String[] { projectText, software.getId(), software.getType(), software.getName(), software.getVersion(), software.getDescription(), lengthText, lastModifiedText, localPath };

			prevProjectId = currProjectId;
		}

		PrettyPrinter.prettyPrint(SOFTWARE_TITLES, rows);
	}

	/**
	 * Add a software to a Project.
	 * 
	 * Command: addprojectsoftware -projectid <projectId> -type <softwareType> -name <softwareName> -desc <softwareDescription> -file <filePath>
	 * 
	 * @param projectId
	 * @param name
	 * @param description
	 */
	@Descriptor("Add a software to a Project")
	public void addprojectsoftware( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // required
			@Descriptor("Software Type") @Parameter(names = { "-type", "--type" }, absentValue = "") String type, // required
			@Descriptor("Software Name") @Parameter(names = { "-name", "--name" }, absentValue = "") String name, // required
			@Descriptor("Software Version") @Parameter(names = { "-version", "--version" }, absentValue = "") String version, // optional
			@Descriptor("Software Description") @Parameter(names = { "-desc", "--description" }, absentValue = "") String description // optional,
	// @Descriptor("Software file path") @Parameter(names = { "-file", "--file" }, absentValue = "") String filePath // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// projectId is required for adding new Software
			if ("".equals(projectId)) {
				System.out.println("Please specify -projectid parameter.");
				return;
			}

			// type is required for adding new Software
			if ("".equals(name)) {
				System.out.println("Please specify -type parameter.");
				return;
			}

			// name is required for adding new Software
			if ("".equals(name)) {
				System.out.println("Please specify -name parameter.");
				return;
			}

			IProject project = this.management.getProject(projectId);
			if (project == null) {
				System.out.println("Project cannot be found.");
				return;
			}

			ISoftware newSoftware = project.addProjectSoftware(type, name, version, description);
			if (newSoftware != null) {
				System.out.println("New Software is created. ");
			} else {
				System.out.println("New Software is not created.");
			}

		} catch (ClientException e) {
			System.out.println("Cannot create new Software. " + e.getMessage());
		}
	}

	/**
	 * Update Software.
	 * 
	 * Command: updateprojecthome -projectid <projectId> -softwareid <softwareId> -type <softwareType> -name <softwareName> -version
	 * <softwareVersion> -desc <softwareDescription>
	 * 
	 * @param projectId
	 * @param softwareId
	 * @param newSoftwareName
	 * @param newProjectHomeDescription
	 */
	@Descriptor("Update Software")
	public void updateprojectsoftware( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // optional
			@Descriptor("Software ID") @Parameter(names = { "-softwareid", "--softwareId" }, absentValue = "") String softwareId, // required
			@Descriptor("New Software type") @Parameter(names = { "-type", "--type" }, absentValue = "null") String newSoftwareType, // optional
			@Descriptor("New Software name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String newSoftwareName, // optional
			@Descriptor("New Software version") @Parameter(names = { "-version", "--version" }, absentValue = "null") String newSoftwareVersion, // optional
			@Descriptor("New Software description") @Parameter(names = { "-desc", "--description" }, absentValue = "null") String newProjectHomeDescription // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		// Check softwareid
		if ("".equals(softwareId)) {
			System.out.println("Please specify -softwareid parameter.");
			return;
		}
		// Check new type
		if ("".equals(newSoftwareType)) {
			System.out.println("Software type cannot be updated to empty string.");
			return;
		}
		// Check new name
		if ("".equals(newSoftwareName)) {
			System.out.println("Software name cannot be updated to empty string.");
			return;
		}
		// Check new version
		if ("".equals(newSoftwareVersion)) {
			System.out.println("Software version cannot be updated to empty string.");
			return;
		}

		try {
			ISoftware software = null;
			if ("".equals(projectId)) {
				software = this.management.getProjectSoftware(softwareId);
			} else {
				software = this.management.getProjectSoftware(projectId, softwareId);
			}
			if (software == null) {
				System.out.println("Software is not found.");
				return;
			}

			boolean isUpdated = false;
			// Update type
			if (!"null".equals(newSoftwareType)) {
				software.setType(newSoftwareType);
				isUpdated = true;
			}
			// Update name
			if (!"null".equals(newSoftwareName)) {
				software.setName(newSoftwareName);
				isUpdated = true;
			}
			// Update version
			if (!"null".equals(newSoftwareVersion)) {
				software.setVersion(newSoftwareVersion);
				isUpdated = true;
			}
			// Update description
			if (!"null".equals(newProjectHomeDescription)) {
				software.setDescription(newProjectHomeDescription);
				isUpdated = true;
			}

			if (isUpdated) {
				boolean succeed = software.update();
				if (succeed) {
					System.out.println("Software is updated. ");
				} else {
					System.out.println("Failed to update Software.");
				}
			} else {
				System.out.println("No Software information is updated.");
			}
		} catch (ClientException e) {
			System.out.println("ClientException: " + e.getMessage());
		}
	}

	/**
	 * Delete a Software.
	 * 
	 * Command: deleteprojectsoftware -projectid <projectId> -softwareid <softwareId>
	 * 
	 * @param projectId
	 * @param softwareId
	 */
	@Descriptor("Delete Software from Project")
	public void deleteprojectsoftware( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // optional
			@Descriptor("Software ID") @Parameter(names = { "-softwareid", "--softwareId" }, absentValue = "") String softwareId // required
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// softwareId is required for deleting a Software
			if ("".equals(softwareId)) {
				System.out.println("Please specify -softwareid parameter.");
				return;
			}

			boolean succeed = false;
			if ("".equals(projectId)) {
				// projectId is not specified
				succeed = this.management.deleteProjectSoftware(softwareId);
			} else {
				// projectId is specified
				succeed = this.management.deleteProjectSoftware(projectId, softwareId);
			}

			if (succeed) {
				System.out.println("Software is deleted. ");
			} else {
				System.out.println("Failed to delete Software.");
			}
		} catch (ClientException e) {
			System.out.println("Failed to delete Software. " + e.getMessage());
		}
	}

	/**
	 * Upload a Software to Project.
	 * 
	 * Command: uploadprojectsoftware -projectid <projectId> -softwareid <softwareId> -file <softwareFilePath>
	 * 
	 * e.g.
	 * 
	 * uploadprojectsoftware -projectid Project1 -softwareid 0c64a77b-21d5-43eb-a292-578920d7eafd -file '/Users/yayang/Downloads/test_software/commons-io-2.5-bin.zip'
	 * 
	 * uploadprojectsoftware -projectid Project1 -softwareid 0c64a77b-21d5-43eb-a292-578920d7eafd -file '/Users/yayang/Downloads/test_software/japanese_issue.zip'
	 * 
	 * @param projectId
	 * @param softwareId
	 */
	@Descriptor("Upload Software to Project")
	public void uploadprojectsoftware( //
			// Parameters
			@Descriptor("Project ID") @Parameter(names = { "-projectid", "--projectId" }, absentValue = "") String projectId, // optional
			@Descriptor("Software ID") @Parameter(names = { "-softwareid", "--softwareId" }, absentValue = "") String softwareId, // required
			@Descriptor("Software file path") @Parameter(names = { "-file", "--file" }, absentValue = "") String filePath // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// softwareId is required
			if ("".equals(softwareId)) {
				System.out.println("Please specify -softwareid parameter.");
				return;
			}

			// file is required
			File file = new File(filePath);
			if (!file.exists() || !file.isFile()) {
				System.out.println("Please specify -file parameter with existing file location.");
				return;
			}

			ISoftware software = null;
			if ("".equals(projectId)) {
				software = this.management.getProjectSoftware(softwareId);
			} else {
				software = this.management.getProjectSoftware(projectId, softwareId);
			}
			if (software == null) {
				System.out.println("Software is not found.");
				return;
			}

			boolean succeed = software.uploadSoftware(file);
			if (succeed) {
				System.out.println("Software is uploaded. ");
			} else {
				System.out.println("Failed to upload Software.");
			}
		} catch (ClientException e) {
			System.out.println("Failed to upload Software. " + e.getMessage());
		}
	}

}
