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
import org.nb.mgm.client.api.MgmFactory;
import org.nb.mgm.client.api.Project;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.DateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectTest {

	protected Management management;

	public ProjectTest() {
		this.management = getManagement();
	}

	protected void setUp() {
		this.management = getManagement();
	}

	protected Management getManagement() {
		return MgmFactory.createManagement("http://127.0.0.1:9090", "admin", "123");
	}

	@Test
	public void test001_getProjects() {
		System.out.println("--- --- --- test001_getProjects() --- --- ---");
		try {
			List<Project> projects = this.management.getProjects();
			for (Project project : projects) {
				System.out.println(project.toString());
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test002_deleteProjects() {
		System.out.println("--- --- --- test002_deleteProjects() --- --- ---");
		try {
			List<Project> projects = this.management.getProjects();
			for (Project project : projects) {
				String projectId = project.getId();
				String projectName = project.getName();
				boolean succeed = this.management.deleteProject(projectId);

				if (succeed) {
					System.out.println("Project '" + projectName + "' (" + projectId + ") is deleted.");
				} else {
					System.out.println("Project '" + projectName + "' (" + projectId + ") failed to be deleted.");
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test003_addProjects() {
		System.out.println("--- --- --- test003_addProjects() --- --- ---");

		try {
			for (int i = 1; i <= 3; i++) {
				String projectId = "Project" + i;
				String projectName = "Project " + i;
				String description = "Description of " + projectName;

				Project newProject = this.management.addProject(projectId, projectName, description);
				if (newProject == null) {
					System.out.println("new Project is: null");
				} else {
					System.out.println("new Project is: " + newProject.toString());
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test004_updateProjects() {
		System.out.println("--- --- --- test004_updateProjects() --- --- ---");
		try {
			List<Project> projects = this.management.getProjects();
			for (Project project : projects) {
				String projectId = project.getId();
				String projectName = project.getName();
				// String projectDescription = project.getDescription();
				String newProjectDescription = "Description of " + projectName + " (" + DateUtil.toString(new Date(), DateUtil.SIMPLE_DATE_FORMAT3) + ")";

				project.setDescription(newProjectDescription);
				boolean succeed = project.update();

				if (succeed) {
					System.out.println("Project '" + projectName + "' (" + projectId + ") is updated.");
				} else {
					System.out.println("Project '" + projectName + "' (" + projectId + ") failed to be updated.");
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
		Result result = JUnitCore.runClasses(ProjectTest.class);
		System.out.println("--- --- --- ProjectTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
