package org.origin.core.resources.node.internal.node;

import java.io.IOException;

import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IRoot;
import org.origin.core.resources.internal.FolderImpl;
import org.origin.core.resources.node.INodespace;
import org.origin.core.resources.node.NodespaceDescription;
import org.origin.core.resources.node.internal.misc.NodespaceDescriptionReader;
import org.origin.core.resources.node.internal.misc.NodespaceDescriptionWriter;

public class NodespaceImpl extends FolderImpl implements INodespace {

	private static final String DOT_METADATA_FOLDER_NAME = ".metadata";
	private static final String DOT_NODESPACE_FILE_NAME = ".nodespace";

	/**
	 * 
	 * @param parent
	 * @param name
	 */
	public NodespaceImpl(IRoot root, IPath fullpath) {
		super(root, fullpath);
	}

	@Override
	public boolean create(NodespaceDescription desc) throws IOException {
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
			NodespaceDescription nodespaceDesc = new NodespaceDescription(name, name);
			setDescription(nodespaceDesc);
		}
		return succeed;
	}

	@Override
	public void setDescription(NodespaceDescription desc) throws IOException {
		IFolder metadataFolder = getFolder(DOT_METADATA_FOLDER_NAME);
		if (!metadataFolder.exists()) {
			metadataFolder.create();
		}

		IFile nodespaceFile = metadataFolder.getFile(DOT_NODESPACE_FILE_NAME);
		NodespaceDescriptionWriter.getInstance().write(desc, nodespaceFile);
	}

	@Override
	public NodespaceDescription getDescription() throws IOException {
		IFolder metadataFolder = getFolder(DOT_METADATA_FOLDER_NAME);
		if (metadataFolder.exists()) {
			IFile nodespaceFile = metadataFolder.getFile(DOT_NODESPACE_FILE_NAME);
			if (nodespaceFile.exists()) {
				return NodespaceDescriptionReader.getInstance().read(nodespaceFile);
			}
		}
		return null;
	}

}
