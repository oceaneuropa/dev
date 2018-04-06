package org.origin.common.resources.node.internal.misc;

import java.io.IOException;

import org.origin.common.resources.Constants;
import org.origin.common.resources.IFile;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.node.INodespace;
import org.origin.common.resources.node.NodespaceDescription;

public class NodespaceDescriptionPersistence {

	private static NodespaceDescriptionPersistence INSTANCE = new NodespaceDescriptionPersistence();

	public static NodespaceDescriptionPersistence getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param nodespace
	 * @return
	 * @throws IOException
	 */
	private IFile getNodespaceDescriptionFile(INodespace nodespace) throws IOException {
		IFolder dotMetadataFolder = nodespace.getFolder(Constants.DOT_METADATA_FOLDER_NAME);
		IFile dotNodespaceFile = dotMetadataFolder.getFile(Constants.DOT_NODESPACE_FILE_NAME);
		return dotNodespaceFile;
	}

	/**
	 * 
	 * @param nodespace
	 * @return
	 * @throws IOException
	 */
	public NodespaceDescription load(INodespace nodespace) throws IOException {
		IFile dotNodespaceFile = getNodespaceDescriptionFile(nodespace);
		NodespaceDescriptionReader reader = new NodespaceDescriptionReader();
		return reader.read(dotNodespaceFile);
	}

	/**
	 * 
	 * @param nodespace
	 * @param desc
	 * @throws IOException
	 */
	public void save(INodespace nodespace, NodespaceDescription desc) throws IOException {
		IFolder dotMetadataFolder = nodespace.getFolder(Constants.DOT_METADATA_FOLDER_NAME);
		if (!dotMetadataFolder.exists()) {
			dotMetadataFolder.create();
		}
		IFile dotNodespaceFile = getNodespaceDescriptionFile(nodespace);
		if (!dotNodespaceFile.exists()) {
			dotNodespaceFile.create();
		}
		NodespaceDescriptionWriter writer = new NodespaceDescriptionWriter();
		writer.write(desc, dotNodespaceFile);
	}

}

/// **
// *
// * @param nodespaceFolder
// * @return
// */
// private File getNodespaceDescriptionFile(File nodespaceFolder) {
// File dotMetadataDir = new File(nodespaceFolder, Constants.DOT_METADATA_FOLDER_NAME);
// return new File(dotMetadataDir, Constants.DOT_NODESPACE_FILE_NAME);
// }

/// **
// *
// * @param nodespaceFolder
// * @return
// * @throws IOException
// */
// public NodespaceDescription load(File nodespaceFolder) throws IOException {
// File dotNodespaceFile = getNodespaceDescriptionFile(nodespaceFolder);
// NodespaceDescriptionReader reader = new NodespaceDescriptionReader();
// return reader.read(dotNodespaceFile);
// }

/// **
// *
// * @param nodespaceFolder
// * @param nodespaceDesc
// * @throws IOException
// */
// public void save(File nodespaceFolder, NodespaceDescription nodespaceDesc) throws IOException {
// File dotNodespaceFile = getNodespaceDescriptionFile(nodespaceFolder);
// if (!dotNodespaceFile.getParentFile().exists()) {
// dotNodespaceFile.getParentFile().mkdirs();
// }
// NodespaceDescriptionWriter writer = new NodespaceDescriptionWriter();
// writer.write(nodespaceDesc, dotNodespaceFile);
// }