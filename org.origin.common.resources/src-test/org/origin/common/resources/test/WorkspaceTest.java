package org.origin.common.resources.test;

import java.io.File;
import java.io.IOException;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.IPath;
import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.ResourcesFactory;
import org.origin.common.resources.impl.PathImpl;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.NodeDescription;
import org.origin.common.resources.node.internal.misc.NodeResourceProvider;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkspaceTest {

	protected IWorkspace workspace;

	public WorkspaceTest() {
		setUp();
	}

	protected void setUp() {
		NodeResourceProvider.register();
		File nodespacesFolder = new File("/Users/ocean/origin/ta1/nodespaces");
		this.workspace = ResourcesFactory.getInstance().createWorkspace(nodespacesFolder);
	}

	/**
	 * List root files/folders.
	 * 
	 */
	// @Ignore
	@Test
	public void test001() {
		System.out.println("--- --- --- test001() --- --- ---");
		IResource[] rootMembers = this.workspace.getRootMembers();
		if (rootMembers != null) {
			for (IResource rootMember : rootMembers) {
				System.out.println(rootMember.getClass().getName() + " - " + rootMember.getFullPath().toString());
			}
		}
		System.out.println();
	}

	/**
	 * Create node1 and node2 folders at root level.
	 * 
	 */
	@Test
	public void test002() {
		System.out.println("--- --- --- test002() --- --- ---");

		try {
			IPath node1Path = new PathImpl("node1");
			IPath node2Path = new PathImpl("node2");
			INode node1 = this.workspace.getFolder(node1Path, INode.class);
			INode node2 = this.workspace.getFolder(node2Path, INode.class);

			if (!node1.exists()) {
				NodeDescription nodeDesc1 = new NodeDescription(node1.getName());
				boolean succeed = node1.create(nodeDesc1);
				System.out.println(node1.getFullPath().toString() + " is created: " + succeed);
			}
			System.out.println(node1.getClass().getSimpleName() + " - " + node1.getFullPath().toString() + " - exists: " + node1.exists());

			if (!node2.exists()) {
				NodeDescription nodeDesc2 = new NodeDescription(node2.getName());
				boolean succeed = node2.create(nodeDesc2);
				System.out.println(node2.getFullPath().toString() + " is created: " + succeed);
			}
			System.out.println(node2.getClass().getSimpleName() + " - " + node2.getFullPath().toString() + " - exists: " + node2.exists());

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	/**
	 * Create bin folder and configuration folder under node1 and node2 folders.
	 * 
	 */
	@Test
	public void test003() {
		System.out.println("--- --- --- test003() --- --- ---");
		try {
			IPath binPath1 = new PathImpl("/node1/bin");
			IPath configPath1 = new PathImpl("/node1/configuration");
			IFolder binFolder1 = this.workspace.getFolder(binPath1);
			IFolder configFolder1 = this.workspace.getFolder(configPath1);
			if (!binFolder1.exists()) {
				binFolder1.create();
			}
			if (!configFolder1.exists()) {
				configFolder1.create();
			}
			System.out.println(binFolder1.getClass().getSimpleName() + " - " + binFolder1.getFullPath().toString() + " - exists: " + binFolder1.exists());
			System.out.println(configFolder1.getClass().getSimpleName() + " - " + configFolder1.getFullPath().toString() + " - exists: " + configFolder1.exists());

			IPath binPath2 = new PathImpl("/node2/bin");
			IPath configPath2 = new PathImpl("/node2/configuration");
			IFolder binFolder2 = this.workspace.getFolder(binPath2);
			IFolder configFolder2 = this.workspace.getFolder(configPath2);
			if (!binFolder2.exists()) {
				binFolder2.create();
			}
			if (!configFolder2.exists()) {
				configFolder2.create();
			}
			System.out.println(binFolder2.getClass().getSimpleName() + " - " + binFolder2.getFullPath().toString() + " - exists: " + binFolder2.exists());
			System.out.println(configFolder2.getClass().getSimpleName() + " - " + configFolder2.getFullPath().toString() + " - exists: " + configFolder2.exists());

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	/**
	 * Delete bin folder and configuration folder under node1 and node2 folders.
	 */
	@Ignore
	public void test004() {
		System.out.println("--- --- --- test004() --- --- ---");
		try {
			IPath binPath1 = new PathImpl("/node1/bin");
			IPath configPath1 = new PathImpl("/node1/configuration");
			IFolder binFolder1 = this.workspace.getFolder(binPath1);
			IFolder configFolder1 = this.workspace.getFolder(configPath1);
			binFolder1.delete();
			configFolder1.delete();

			IPath binPath2 = new PathImpl("/node2/bin");
			IPath configPath2 = new PathImpl("/node2/configuration");
			IFolder binFolder2 = this.workspace.getFolder(binPath2);
			IFolder configFolder2 = this.workspace.getFolder(configPath2);
			binFolder2.delete();
			configFolder2.delete();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	/**
	 * List root files/folders and its content files.
	 * 
	 */
	@Test
	public void test099() {
		System.out.println("--- --- --- test099() --- --- ---");
		IResource[] rootMembers = this.workspace.getRootMembers();
		if (rootMembers != null) {
			for (IResource rootMember : rootMembers) {
				System.out.println(rootMember.getClass().getSimpleName() + " - " + rootMember.getFullPath().toString());

				if (rootMember instanceof IFolder) {
					IFolder folder = (IFolder) rootMember;
					IResource[] members = folder.getMembers();
					for (IResource member : members) {
						System.out.println("\t" + member.getClass().getSimpleName() + " - " + member.getFullPath().toString());
					}
					System.out.println();
				}
			}
		}
		System.out.println();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(WorkspaceTest.class);
		System.out.println("--- --- --- WorkspaceTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
