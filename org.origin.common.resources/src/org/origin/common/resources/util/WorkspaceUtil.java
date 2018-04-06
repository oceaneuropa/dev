package org.origin.common.resources.util;

import java.io.IOException;

import org.origin.common.resources.IFolder;
import org.origin.common.resources.IPath;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.extension.FolderConfiguratorHelper;
import org.origin.common.resources.impl.PathImpl;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.NodeDescription;

public class WorkspaceUtil {

	public static WorkspaceUtil INSTANCE = new WorkspaceUtil();

	/**
	 * Create node folder and initial structures in it (sub folders and files) at the root level of a workspace.
	 * 
	 * @param workspace
	 * @param id
	 * @param typeId
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public INode createNode(IWorkspace workspace, String id, String typeId, String name) throws IOException {
		if (name == null) {
			throw new IOException("Node name is null.");
		}
		if (id == null) {
			id = name;
		}

		INode node = WorkspaceHelper.INSTANCE.getRootNode(workspace, id);
		if (node != null) {
			throw new IOException("Node (id='" + id + "') already exists.");
		}

		node = WorkspaceHelper.INSTANCE.getRootNodeByName(workspace, name);
		if (node != null) {
			throw new IOException("Node (name='" + name + "') already exists.");
		}

		try {
			IPath path = new PathImpl(name);
			node = workspace.getFolder(path, INode.class);
			NodeDescription desc = new NodeDescription(id);
			desc.setAttirbute("typeId", typeId);
			node.create(desc);

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		return node;
	}

	/**
	 * 
	 * @param workspace
	 * @param id
	 * @param attrName
	 * @param attrValue
	 * @return
	 * @throws IOException
	 */
	public boolean setNodeAttribute(IWorkspace workspace, String id, String attrName, String attrValue) throws IOException {
		INode node = WorkspaceHelper.INSTANCE.getRootNode(workspace, id);
		if (node == null) {
			System.err.println("Node (id='" + id + "') is not found.");
		}

		if (attrName == null) {
			throw new IOException("attrName is null.");
		}

		NodeDescription desc = node.getDescription();
		if (attrValue != null) {
			desc.setAttirbute(attrName, attrValue);
		} else {
			desc.removeAttribute(attrName);
		}
		node.setDescription(desc);

		return true;
	}

	/**
	 * Delete node folder from the root level of a workspace.
	 * 
	 * @param workspace
	 * @param nodeId
	 * @return
	 * @throws IOException
	 */
	public boolean deleteNode(IWorkspace workspace, String nodeId) throws IOException {
		try {
			INode node = WorkspaceHelper.INSTANCE.getRootNode(workspace, nodeId);
			if (node == null) {
				return false;
			}
			return node.delete();

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * @param context
	 * @param workspace
	 * @param folder
	 * @throws IOException
	 */
	public void preConfigureFolder(Object context, IWorkspace workspace, IFolder folder) throws IOException {
		FolderConfiguratorHelper.INSTANCE.preConfigureFolder(null, workspace, folder);
	}

}

// // 2. Create bin folder and configuration folder
// IPath binPath = nodePath.append("bin");
// IPath configPath = nodePath.append("configuration");
// IFolder binFolder = workspace.getFolder(binPath);
// IFolder configFolder = workspace.getFolder(configPath);
// binFolder.create();
// configFolder.create();

// // 3. Create /bin/start.sh and /bin/stop.sh
// IPath start_sh_filePath = binPath.append("start.sh");
// IPath stop_sh_filePath = binPath.append("stop.sh");
// IFile start_sh_file = workspace.getFile(start_sh_filePath);
// IFile stop_sh_file = workspace.getFile(stop_sh_filePath);
// start_sh_file.create();
// stop_sh_file.create();
