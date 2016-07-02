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
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.IProjectHome;
import org.nb.mgm.client.api.ManagementFactory;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.DateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectHomeTest {

	protected Management management;

	public ProjectHomeTest() {
		this.management = getManagement();
	}

	protected void setUp() {
		this.management = getManagement();
	}

	protected Management getManagement() {
		return ManagementFactory.createManagement("http://127.0.0.1:9090", "admin", "123");
	}

	@Test
	public void test001_getProjectHomes() {
		System.out.println("--- --- --- test001_getProjectHomes() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				System.out.println(project.toString());

				List<IProjectHome> projectHomes = project.getProjectHomes();
				for (IProjectHome projectHome1 : projectHomes) {
					String projectHomeId = projectHome1.getId();
					IProjectHome projectHome2 = project.getProjectHome(projectHomeId);
					// System.out.println("\t" + projectHome1.toString());
					System.out.println("\t" + projectHome2.toString());
				}
				if (!projectHomes.isEmpty()) {
					System.out.println();
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test002_deleteProjectHomes() {
		System.out.println("--- --- --- test002_deleteProjectHomes() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				String projectId = project.getId();
				String projectName = project.getName();

				List<IProjectHome> projectHomes = project.getProjectHomes();
				for (IProjectHome projectHome : projectHomes) {
					String projectHomeId = projectHome.getId();
					String projectHomeName = projectHome.getName();
					boolean succeed = project.deleteProjectHome(projectHomeId);

					if (succeed) {
						System.out.println("ProjectHome '" + projectHomeName + "' (" + projectHomeId + ") is deleted from Project '" + projectName + "' (" + projectId + ").");
					} else {
						System.out.println("ProjectHome ' '" + projectHomeName + "' (" + projectHomeId + ") failed to be deleted from Project '" + projectName + "' (" + projectId + ").");
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test003_addProjectHomes() {
		System.out.println("--- --- --- test003_addProjectHomes() --- --- ---");

		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				String projectId = project.getId();
				String projectName = project.getName();

				if ("Project1".equals(projectId)) {
					IProjectHome newProjectHomeConfig11 = this.management.addProjectHome(projectId, "HomeConfig11", "Description of HomeConfig11");
					if (newProjectHomeConfig11 != null) {
						System.out.println(newProjectHomeConfig11 + " is created in Project '" + projectName + "' (" + projectId + ").");
					} else {
						System.out.println("HomeConfig11 failed to be created in Project '" + projectName + "' (" + projectId + ").");
					}

					IProjectHome newProjectHomeConfig12 = this.management.addProjectHome(projectId, "HomeConfig12", "Description of HomeConfig12");
					if (newProjectHomeConfig12 != null) {
						System.out.println(newProjectHomeConfig12 + " is created in Project '" + projectName + "' (" + projectId + ").");
					} else {
						System.out.println("HomeConfig12 failed to be created in Project '" + projectName + "' (" + projectId + ").");
					}

				} else if ("Project2".equals(projectId)) {
					IProjectHome newProjectHomeConfig21 = this.management.addProjectHome(projectId, "HomeConfig21", "Description of HomeConfig21");
					if (newProjectHomeConfig21 != null) {
						System.out.println(newProjectHomeConfig21 + " is created in Project '" + projectName + "' (" + projectId + ").");
					} else {
						System.out.println("HomeConfig21 failed to be created in Project '" + projectName + "' (" + projectId + ").");
					}

					IProjectHome newProjectHomeConfig22 = this.management.addProjectHome(projectId, "HomeConfig22", "Description of HomeConfig22");
					if (newProjectHomeConfig22 != null) {
						System.out.println(newProjectHomeConfig22 + " is created in Project '" + projectName + "' (" + projectId + ").");
					} else {
						System.out.println("HomeConfig22 failed to be created in Project '" + projectName + "' (" + projectId + ").");
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Ignore
	public void test004_updateProjectHomeConfigs() {
		System.out.println("--- --- --- test004_updateProjectHomeConfigs() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				String projectId = project.getId();
				String projectName = project.getName();

				List<IProjectHome> homeConfigs = project.getProjectHomes();
				for (IProjectHome homeConfig : homeConfigs) {
					String homeConfigId = homeConfig.getId();
					String homeConfigName = homeConfig.getName();
					String newHomeConfigDescription = "Description of " + homeConfigName + " (" + DateUtil.toString(new Date(), DateUtil.SIMPLE_DATE_FORMAT3) + ")";

					homeConfig.setDescription(newHomeConfigDescription);
					boolean succeed = homeConfig.update();

					if (succeed) {
						System.out.println("ProjectHomeConfig '" + homeConfigName + "' (" + homeConfigId + ") is updated in Project '" + projectName + "' (" + projectId + ").");
					} else {
						System.out.println("ProjectHomeConfig '" + homeConfigName + "' (" + homeConfigId + ") failed to be updated in Project '" + projectName + "' (" + projectId + ").");
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
		Result result = JUnitCore.runClasses(ProjectHomeTest.class);
		System.out.println("--- --- --- ProjectHomeTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
