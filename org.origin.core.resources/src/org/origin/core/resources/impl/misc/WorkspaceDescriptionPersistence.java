package org.origin.core.resources.impl.misc;

import java.io.IOException;

import org.origin.core.resources.Constants;
import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.WorkspaceDescription;

public class WorkspaceDescriptionPersistence {

	private static WorkspaceDescriptionPersistence INSTANCE = new WorkspaceDescriptionPersistence();

	public static WorkspaceDescriptionPersistence getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param workspace
	 * @return
	 * @throws IOException
	 */
	public IFile getWorkspaceDescriptionFile(IWorkspace workspace) throws IOException {
		IFolder dotMetadataFolder = workspace.findRootMember(Constants.DOT_METADATA_FOLDER_NAME, IFolder.class);
		IFile dotWorkspaceFile = dotMetadataFolder.getFile(Constants.DOT_WORKSPACE_FILE_NAME);
		return dotWorkspaceFile;
	}

	/**
	 * 
	 * @param workspace
	 * @return
	 * @throws IOException
	 */
	public WorkspaceDescription load(IWorkspace workspace) throws IOException {
		WorkspaceDescription desc = null;
		IFile dotWorkspaceFile = getWorkspaceDescriptionFile(workspace);
		if (dotWorkspaceFile.exists()) {
			WorkspaceDescriptionReader reader = new WorkspaceDescriptionReader();
			desc = reader.read(dotWorkspaceFile);
		}
		return desc;
	}

	/**
	 * 
	 * @param workspace
	 * @param desc
	 * @throws IOException
	 */
	public void save(IWorkspace workspace, WorkspaceDescription desc) throws IOException {
		IFolder dotMetadataFolder = workspace.findRootMember(Constants.DOT_METADATA_FOLDER_NAME, IFolder.class);
		if (!dotMetadataFolder.exists()) {
			dotMetadataFolder.create();
		}
		IFile dotWorkspaceFile = getWorkspaceDescriptionFile(workspace);
		if (!dotWorkspaceFile.exists()) {
			dotWorkspaceFile.create();
		}
		WorkspaceDescriptionWriter writer = new WorkspaceDescriptionWriter();
		writer.write(desc, dotWorkspaceFile);
	}

}

/// **
// *
// * @param projectFolder
// * @return
// */
// private File getProjectDescriptionFile(File projectFolder) {
// File dotMetadataDir = new File(projectFolder, ".metadata");
// return new File(dotMetadataDir, ".project");
// }

/// **
// *
// * @param projectFolder
// * @return
// * @throws IOException
// */
// public ProjectDescription load(File projectFolder) throws IOException {
// File dotProjectFile = getProjectDescriptionFile(projectFolder);
// ProjectDescriptionReader reader = new ProjectDescriptionReader();
// return reader.read(dotProjectFile);
// }

/// **
// *
// * @param projectFolder
// * @param desc
// * @throws IOException
// */
// public void save(File projectFolder, ProjectDescription desc) throws IOException {
// File dotNodespaceFile = getProjectDescriptionFile(projectFolder);
// if (!dotNodespaceFile.getParentFile().exists()) {
// dotNodespaceFile.getParentFile().mkdirs();
// }
// ProjectDescriptionWriter writer = new ProjectDescriptionWriter();
// writer.write(desc, dotNodespaceFile);
// }