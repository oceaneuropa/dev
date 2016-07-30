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
import org.nb.mgm.client.api.IProjectNode;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.client.api.ManagementFactory;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.DateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectNodeTest {

	protected ManagementClient management;

	public ProjectNodeTest() {
		this.management = getManagement();
	}

	protected void setUp() {
		this.management = getManagement();
	}

	protected ManagementClient getManagement() {
		return ManagementFactory.createManagement("http://127.0.0.1:9090", "admin", "123");
	}

	@Test
	public void test001_getProjectNodes() {
		System.out.println("--- --- --- ProjectNode_test001_getProjectNodes() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				System.out.println(project.toString());

				List<IProjectHome> projectHomes = project.getProjectHomes();
				for (IProjectHome projectHome : projectHomes) {
					System.out.println("\t" + projectHome.toString());

					List<IProjectNode> projectNodes = projectHome.getProjectNodes();
					for (IProjectNode projectNode : projectNodes) {
						System.out.println("\t\t" + projectNode.toString());
					}

					if (!projectNodes.isEmpty()) {
						System.out.println();
					}
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
	public void test002_deleteProjectNodes() {
		System.out.println("--- --- --- ProjectNode_test002_deleteProjectNodes() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				String projectId = project.getId();
				String projectName = project.getName();

				List<IProjectHome> projectHomes = project.getProjectHomes();
				for (IProjectHome projectHome : projectHomes) {
					String projectHomeId = projectHome.getId();
					String projectHomeName = projectHome.getName();

					List<IProjectNode> projectNodes = projectHome.getProjectNodes();
					for (IProjectNode projectNode : projectNodes) {
						String projectNodeId = projectNode.getId();
						String projectNodeName = projectNode.getName();

						boolean succeed = projectHome.deleteProjectNode(projectNodeId);
						if (succeed) {
							System.out.println("ProjectNode '" + projectNodeName + "' (" + projectNodeId + ") is deleted from '" + projectName + "' (" + projectId + ") -> '" + projectHomeName + "' (" + projectHomeId + ").");
						} else {
							System.out.println("ProjectNode ' '" + projectNodeName + "' (" + projectNodeId + ") failed to be deleted from  '" + projectName + "' (" + projectId + ") -> '" + projectHomeName + "' (" + projectHomeId + ").");
						}
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test003_addProjectNodes() {
		System.out.println("--- --- --- ProjectNode_test003_addProjectNodes() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				String projectId = project.getId();

				if ("Project1".equals(projectId)) {
					List<IProjectHome> projectHomes = project.getProjectHomes();
					for (IProjectHome projectHome : projectHomes) {
						String projectHomeName = projectHome.getName();

						if ("HomeConfig11".equals(projectHomeName)) {
							IProjectNode node1101 = projectHome.addProjectNode("node1101", "Node1101", "Description of Node1101");
							IProjectNode node1102 = projectHome.addProjectNode("node1102", "Node1102", "Description of Node1102");
							if (node1101 != null) {
								System.out.println(node1101 + " is created.");
							} else {
								System.out.println("node1101 failed to be created.");
							}
							if (node1102 != null) {
								System.out.println(node1102 + " is created.");
							} else {
								System.out.println("node1102 failed to be created.");
							}

						} else if ("HomeConfig12".equals(projectHomeName)) {
							IProjectNode node1201 = projectHome.addProjectNode("node1201", "Node1201", "Description of Node1201");
							IProjectNode node1202 = projectHome.addProjectNode("node1202", "Node1202", "Description of Node1202");

							if (node1201 != null) {
								System.out.println(node1201 + " is created.");
							} else {
								System.out.println("node1201 failed to be created.");
							}
							if (node1202 != null) {
								System.out.println(node1202 + " is created.");
							} else {
								System.out.println("node1102 failed to be created.");
							}
						}
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test004_updateProjectNodes() {
		System.out.println("--- --- --- ProjectNode_test004_updateProjectNodes() --- --- ---");
		try {
			List<IProject> projects = this.management.getProjects();
			for (IProject project : projects) {
				List<IProjectHome> projectHomes = project.getProjectHomes();

				for (IProjectHome projectHome : projectHomes) {
					List<IProjectNode> projectNodes = projectHome.getProjectNodes();

					for (IProjectNode projectNode : projectNodes) {
						String projectNodeName = projectNode.getName();
						String newProjectNodeDescription = "Description of " + projectNodeName + " (" + DateUtil.toString(new Date(), DateUtil.SIMPLE_DATE_FORMAT3) + ")";

						projectNode.setDescription(newProjectNodeDescription);
						boolean succeed = projectNode.update();
						if (succeed) {
							System.out.println(projectNode + " is updated.");
						} else {
							System.out.println(projectNode + " failed to be updated.");
						}
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
		Result result = JUnitCore.runClasses(ProjectNodeTest.class);
		System.out.println("--- --- --- ProjectNodeTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
