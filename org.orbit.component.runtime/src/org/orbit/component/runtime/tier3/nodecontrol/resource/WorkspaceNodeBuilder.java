package org.orbit.component.runtime.tier3.nodecontrol.resource;

import java.io.IOException;

import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.impl.PathImpl;
import org.origin.core.resources.node.INode;
import org.origin.core.resources.node.NodeDescription;

public class WorkspaceNodeBuilder {

	public static WorkspaceNodeBuilder INSTANCE = new WorkspaceNodeBuilder();

	/**
	 * Create node folder and initial structures in it (sub folders and files) at the root level of a workspace.
	 * 
	 * @param workspace
	 * @param nodeId
	 * @return
	 * @throws IOException
	 */
	public boolean createNode(IWorkspace workspace, String nodeId) throws IOException {
		if (WorkspaceNodeHelper.INSTANCE.nodeExists(workspace, nodeId)) {
			return false;
		}
		try {
			// 1. Create node folder
			IPath nodePath = new PathImpl(nodeId);
			INode node = workspace.getFolder(nodePath, INode.class);
			NodeDescription nodeDesc = new NodeDescription(nodeId);
			node.create(nodeDesc);

			// 2. Create bin folder and configuration folder
			IPath binPath = nodePath.append("bin");
			IPath configPath = nodePath.append("configuration");
			IFolder binFolder = workspace.getFolder(binPath);
			IFolder configFolder = workspace.getFolder(configPath);
			binFolder.create();
			configFolder.create();

			// 3. Create /bin/start.sh and /bin/stop.sh
			IPath start_sh_filePath = binPath.append("start.sh");
			IPath stop_sh_filePath = binPath.append("stop.sh");
			IFile start_sh_file = workspace.getFile(start_sh_filePath);
			IFile stop_sh_file = workspace.getFile(stop_sh_filePath);
			start_sh_file.create();
			stop_sh_file.create();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
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
			INode node = WorkspaceNodeHelper.INSTANCE.getNode(workspace, nodeId);
			if (node == null) {
				return false;
			}
			return node.delete();

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
