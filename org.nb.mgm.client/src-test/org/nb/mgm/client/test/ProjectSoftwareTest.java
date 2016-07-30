package org.nb.mgm.client.test;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.ISoftware;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.client.api.ManagementFactory;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.DateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectSoftwareTest {

	protected ManagementClient management;

	public ProjectSoftwareTest() {
		this.management = getManagement();
	}

	protected void setUp() {
		this.management = getManagement();
	}

	protected ManagementClient getManagement() {
		return ManagementFactory.createManagement("http://127.0.0.1:9090", "admin", "123");
	}

	@Test
	public void test001_getProjectSoftwareList() {
		System.out.println("--- --- --- ProjectSoftware_test001_getProjectSoftwareList() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				System.out.println(project.toString());

				List<ISoftware> softwareList = project.getSoftware();
				for (ISoftware software : softwareList) {
					String softwareId = software.getId();
					ISoftware softwareFound = project.getSoftware(softwareId);
					// System.out.println("\t" + software.toString());
					System.out.println("\t" + softwareFound.toString());
				}
				if (!softwareList.isEmpty()) {
					System.out.println();
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test002_deleteProjectSoftware() {
		System.out.println("--- --- --- ProjectSoftware_test002_deleteProjectSoftware() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				List<ISoftware> softwareList = project.getSoftware();

				for (ISoftware software : softwareList) {
					String softwareId = software.getId();
					String softwareName = software.getName();

					boolean succeed = project.deleteSoftware(softwareId);
					if (succeed) {
						System.out.println("'" + softwareName + "' (" + softwareId + ") is deleted.");
					} else {
						System.out.println("'" + softwareName + "' (" + softwareId + ") is not deleted.");
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test003_addProjectSoftware() {
		System.out.println("--- --- --- ProjectSoftware_test003_addProjectSoftware() --- --- ---");

		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				String projectId = project.getId();

				if ("Project1".equals(projectId)) {
					ISoftware software1 = project.addSoftware("system", "ServiceRegistry", "1.0.0", "ServiceRegistry description.");
					ISoftware software2 = project.addSoftware("system", "UserRegistry", "1.0.0", "UserRegistry description.");

					if (software1 != null) {
						System.out.println("ServiceRegistry is created.");
					} else {
						System.out.println("ServiceRegistry is not created.");
					}
					if (software2 != null) {
						System.out.println("UserRegistry is created.");
					} else {
						System.out.println("UserRegistry is not created.");
					}

				} else if ("Project2".equals(projectId)) {
					ISoftware software3 = project.addSoftware("system", "AppStore", "1.0.0", "AppStore description.");
					ISoftware software4 = project.addSoftware("system", "DistributedFileSystem", "1.0.0", "DistributedFileSystem description.");

					if (software3 != null) {
						System.out.println("AppStore is created.");
					} else {
						System.out.println("AppStore is not created.");
					}
					if (software4 != null) {
						System.out.println("DistributedFileSystem is created.");
					} else {
						System.out.println("DistributedFileSystem is not created.");
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Ignore
	public void test004_updateProjectSoftware() {
		System.out.println("--- --- --- ProjectSoftware_test004_updateProjectSoftware() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				List<ISoftware> softwareList = project.getSoftware();

				for (ISoftware software : softwareList) {
					String softwareId = software.getId();
					String softwareName = software.getName();
					String newSoftwareDescription = "Description of " + softwareName + " (" + DateUtil.toString(new Date(), DateUtil.SIMPLE_DATE_FORMAT3) + ")";

					software.setDescription(newSoftwareDescription);

					boolean succeed = software.update();
					if (succeed) {
						System.out.println("'" + softwareName + "' (" + softwareId + ") is updated.");
					} else {
						System.out.println("'" + softwareName + "' (" + softwareId + ") is not updated.");
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(ProjectSoftwareTest.class);
		System.out.println("--- --- --- ProjectSoftwareTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
