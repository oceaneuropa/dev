package org.origin.common.resources.impl.misc;

import java.io.IOException;

import org.origin.common.resources.Constants;
import org.origin.common.resources.IFile;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.IProject;
import org.origin.common.resources.ProjectDescription;

public class ProjectDescriptionPersistence {

	private static ProjectDescriptionPersistence INSTANCE = new ProjectDescriptionPersistence();

	public static ProjectDescriptionPersistence getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param project
	 * @return
	 * @throws IOException
	 */
	private IFile getProjectDescriptionFile(IProject project) throws IOException {
		IFolder dotMetadataFolder = project.getFolder(Constants.DOT_METADATA_FOLDER_NAME);
		IFile dotProjectFile = dotMetadataFolder.getFile(Constants.DOT_PROJECT_FILE_NAME);
		return dotProjectFile;
	}

	/**
	 * 
	 * @param project
	 * @return
	 * @throws IOException
	 */
	public ProjectDescription load(IProject project) throws IOException {
		ProjectDescription desc = null;
		IFile dotProjectFile = getProjectDescriptionFile(project);
		if (dotProjectFile.exists()) {
			ProjectDescriptionReader reader = new ProjectDescriptionReader();
			desc = reader.read(dotProjectFile);
		}
		return desc;
	}

	/**
	 * 
	 * @param project
	 * @param desc
	 * @throws IOException
	 */
	public void save(IProject project, ProjectDescription desc) throws IOException {
		IFolder dotMetadataFolder = project.getFolder(Constants.DOT_METADATA_FOLDER_NAME);
		if (!dotMetadataFolder.exists()) {
			dotMetadataFolder.create();
		}
		IFile dotProjectFile = getProjectDescriptionFile(project);
		if (!dotProjectFile.exists()) {
			dotProjectFile.create();
		}
		ProjectDescriptionWriter writer = new ProjectDescriptionWriter();
		writer.write(desc, dotProjectFile);
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