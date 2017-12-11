package org.orbit.component.runtime.tier3.transferagent.resource;

import java.util.ArrayList;
import java.util.List;

import org.origin.core.resources.IResource;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.node.INode;

public class WorkspaceNodeHelper {

	public static WorkspaceNodeHelper INSTANCE = new WorkspaceNodeHelper();

	/**
	 * Get root level Nodes from workspace.
	 * 
	 * @param workspace
	 * @return
	 */
	public List<INode> getNodes(IWorkspace workspace) {
		List<INode> nodes = new ArrayList<INode>();
		IResource[] rootMembers = workspace.getRootMembers();
		for (IResource rootMember : rootMembers) {
			if (rootMember instanceof INode) {
				INode node = (INode) rootMember;
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * 
	 * @param workspace
	 * @param nodeId
	 * @return
	 */
	public INode getNode(IWorkspace workspace, String nodeId) {
		INode node = null;
		IResource[] rootMembers = workspace.getRootMembers();
		for (IResource rootMember : rootMembers) {
			if (rootMember instanceof INode) {
				INode currNode = (INode) rootMember;
				String currNodeName = currNode.getName();
				if (nodeId.equals(currNodeName)) {
					node = currNode;
					break;
				}
			}
		}
		return node;
	}

	/**
	 * 
	 * @param workspace
	 * @param nodeId
	 * @return
	 */
	public boolean nodeExists(IWorkspace workspace, String nodeId) {
		boolean exists = false;
		IResource[] rootMembers = workspace.getRootMembers();
		for (IResource rootMember : rootMembers) {
			if (rootMember instanceof INode) {
				INode currNode = (INode) rootMember;
				String currNodeName = currNode.getName();
				if (currNodeName.equals(nodeId)) {
					exists = true;
					break;
				}
			}
		}
		return exists;
	}

}
