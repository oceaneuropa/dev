package org.origin.core.resources.node.internal.node;

import java.io.IOException;

import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IRoot;
import org.origin.core.resources.internal.FolderImpl;
import org.origin.core.resources.node.INode;
import org.origin.core.resources.node.NodeDescription;
import org.origin.core.resources.node.internal.misc.NodeDescriptionReader;
import org.origin.core.resources.node.internal.misc.NodeDescriptionWriter;

public class NodeImpl extends FolderImpl implements INode {

	private static final String DOT_METADATA_FOLDER_NAME = ".metadata";
	private static final String DOT_NODE_FILE_NAME = ".node";

	/**
	 * 
	 * @param root
	 * @param fullpath
	 */
	public NodeImpl(IRoot root, IPath fullpath) {
		super(root, fullpath);
	}

	@Override
	public boolean create(NodeDescription desc) throws IOException {
		if (exists()) {
			return false;
		}

		boolean succeed = super.create();
		if (succeed) {
			IFolder metadataFolder = getFolder(DOT_METADATA_FOLDER_NAME);
			if (!metadataFolder.exists()) {
				metadataFolder.create();
			}

			setDescription(desc);
		}
		return succeed;
	}

	@Override
	public boolean create() throws IOException {
		if (exists()) {
			return false;
		}

		boolean succeed = super.create();
		if (succeed) {
			IFolder metadataFolder = getFolder(DOT_METADATA_FOLDER_NAME);
			if (!metadataFolder.exists()) {
				metadataFolder.create();
			}

			String name = getName();
			NodeDescription nodespaceDesc = new NodeDescription(name, name);
			setDescription(nodespaceDesc);
		}
		return succeed;
	}

	@Override
	public void setDescription(NodeDescription desc) throws IOException {
		IFolder metadataFolder = getFolder(DOT_METADATA_FOLDER_NAME);
		if (!metadataFolder.exists()) {
			metadataFolder.create();
		}

		IFile nodeFile = metadataFolder.getFile(DOT_NODE_FILE_NAME);
		NodeDescriptionWriter.getInstance().write(desc, nodeFile);
	}

	@Override
	public NodeDescription getDescription() throws IOException {
		IFolder metadataFolder = getFolder(DOT_METADATA_FOLDER_NAME);
		if (metadataFolder.exists()) {
			IFile nodeFile = metadataFolder.getFile(DOT_NODE_FILE_NAME);
			if (nodeFile.exists()) {
				return NodeDescriptionReader.getInstance().read(nodeFile);
			}
		}
		return null;
	}

}
