package org.origin.core.resources.node.internal.misc;

import java.io.IOException;

import org.origin.core.resources.Constants;
import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.node.INode;
import org.origin.core.resources.node.NodeDescription;

public class NodeDescriptionPersistence {

	private static NodeDescriptionPersistence INSTANCE = new NodeDescriptionPersistence();

	public static NodeDescriptionPersistence getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param node
	 * @return
	 * @throws IOException
	 */
	private IFile getNodeDescriptionFile(INode node) throws IOException {
		IFolder dotMetadataFolder = node.getFolder(Constants.DOT_METADATA_FOLDER_NAME);
		IFile dotNodeFile = dotMetadataFolder.getFile(Constants.DOT_NODE_FILE_NAME);
		return dotNodeFile;
	}

	/**
	 * 
	 * @param node
	 * @return
	 * @throws IOException
	 */
	public NodeDescription load(INode node) throws IOException {
		IFile dotNodeFile = getNodeDescriptionFile(node);
		NodeDescriptionReader reader = new NodeDescriptionReader();
		return reader.read(dotNodeFile);
	}

	/**
	 * 
	 * @param node
	 * @param desc
	 * @throws IOException
	 */
	public void save(INode node, NodeDescription desc) throws IOException {
		IFolder dotMetadataFolder = node.getFolder(Constants.DOT_METADATA_FOLDER_NAME);
		if (!dotMetadataFolder.exists()) {
			dotMetadataFolder.create();
		}
		IFile dotNodeFile = getNodeDescriptionFile(node);
		if (!dotNodeFile.exists()) {
			dotNodeFile.create();
		}
		NodeDescriptionWriter writer = new NodeDescriptionWriter();
		writer.write(desc, dotNodeFile);
	}

}

/// **
// *
// * @param nodeFolder
// * @return
// */
// private File getNodeDescriptionFile(File nodeFolder) {
// File dotMetadataDir = new File(nodeFolder, Constants.DOT_METADATA_FOLDER_NAME);
// return new File(dotMetadataDir, Constants.DOT_NODE_FILE_NAME);
// }

/// **
// *
// * @param nodeFolder
// * @return
// * @throws IOException
// */
// public NodeDescription load(File nodeFolder) throws IOException {
// File dotNodeFile = getNodeDescriptionFile(nodeFolder);
// NodeDescriptionReader reader = new NodeDescriptionReader();
// return reader.read(dotNodeFile);
// }

/// **
// *
// * @param nodeFolder
// * @param nodeDesc
// * @throws IOException
// */
// public void save(File nodeFolder, NodeDescription nodeDesc) throws IOException {
// File dotNodeFile = getNodeDescriptionFile(nodeFolder);
// if (!dotNodeFile.getParentFile().exists()) {
// dotNodeFile.getParentFile().mkdirs();
// }
// NodeDescriptionWriter writer = new NodeDescriptionWriter();
// writer.write(nodeDesc, dotNodeFile);
// }