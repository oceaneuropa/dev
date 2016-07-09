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
import org.nb.mgm.client.api.IProjectHome;
import org.nb.mgm.client.api.Management;
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
		System.out.println("--- --- --- ProjectHome_test001_getProjectHomes() --- --- ---");
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
		System.out.println("--- --- --- ProjectHome_test002_deleteProjectHomes() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				List<IProjectHome> projectHomes = project.getProjectHomes();

				for (IProjectHome projectHome : projectHomes) {
					String projectHomeId = projectHome.getId();
					String projectHomeName = projectHome.getName();
					boolean succeed = project.deleteProjectHome(projectHomeId);

					if (succeed) {
						System.out.println("ProjectHome '" + projectHomeName + "' (" + projectHomeId + ") is deleted.");
					} else {
						System.out.println("ProjectHome ' '" + projectHomeName + "' (" + projectHomeId + ") is not deleted.");
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
		System.out.println("--- --- --- ProjectHome_test003_addProjectHomes() --- --- ---");

		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				String projectId = project.getId();

				if ("Project1".equals(projectId)) {
					IProjectHome home1 = project.addProjectHome("HomeConfig11", "Description of HomeConfig11");
					IProjectHome home2 = project.addProjectHome("HomeConfig12", "Description of HomeConfig12");

					if (home1 != null) {
						System.out.println("HomeConfig11 is created.");
					} else {
						System.out.println("HomeConfig11 is not created.");
					}
					if (home2 != null) {
						System.out.println("HomeConfig12 is created.");
					} else {
						System.out.println("HomeConfig12 is not created.");
					}

				} else if ("Project2".equals(projectId)) {
					IProjectHome home3 = project.addProjectHome("HomeConfig21", "Description of HomeConfig21");
					IProjectHome home4 = project.addProjectHome("HomeConfig22", "Description of HomeConfig22");

					if (home3 != null) {
						System.out.println("HomeConfig21 is created.");
					} else {
						System.out.println("HomeConfig21 is not created.");
					}
					if (home4 != null) {
						System.out.println("HomeConfig22 is created.");
					} else {
						System.out.println("HomeConfig22 is not created.");
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Ignore
	public void test004_updateProjectHomes() {
		System.out.println("--- --- --- ProjectHome_test004_updateProjectHomes() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				List<IProjectHome> projectHomes = project.getProjectHomes();

				for (IProjectHome projectHome : projectHomes) {
					String projectHomeId = projectHome.getId();
					String projectHomeName = projectHome.getName();
					String newProjectHomeDescription = "Description of " + projectHomeName + " (" + DateUtil.toString(new Date(), DateUtil.SIMPLE_DATE_FORMAT3) + ")";

					projectHome.setDescription(newProjectHomeDescription);
					boolean succeed = projectHome.update();

					if (succeed) {
						System.out.println("ProjectHome '" + projectHomeName + "' (" + projectHomeId + ") is updated.");
					} else {
						System.out.println("ProjectHome '" + projectHomeName + "' (" + projectHomeId + ") is not updated.");
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
