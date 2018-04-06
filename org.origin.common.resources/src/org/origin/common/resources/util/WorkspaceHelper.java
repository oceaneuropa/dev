package org.origin.common.resources.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.NodeDescription;

public class WorkspaceHelper {

	public static WorkspaceHelper INSTANCE = new WorkspaceHelper();

	/**
	 * Get root level Nodes from workspace.
	 * 
	 * @param workspace
	 * @return
	 */
	public List<INode> getRootNodes(IWorkspace workspace) {
		List<INode> nodes = new ArrayList<INode>();
		IResource[] members = workspace.getRootMembers();
		for (IResource member : members) {
			if (member instanceof INode) {
				INode node = (INode) member;
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * 
	 * @param workspace
	 * @param typeId
	 * @return
	 */
	public List<INode> getRootNodes(IWorkspace workspace, String typeId) {
		List<INode> resultNodes = new ArrayList<INode>();
		if (typeId != null) {
			List<INode> nodes = getRootNodes(workspace);
			if (nodes != null) {
				for (INode currNode : nodes) {
					String currTypeId = null;
					try {
						NodeDescription desc = currNode.getDescription();
						if (desc != null) {
							currTypeId = desc.getStringAttribute("typeId");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (typeId.equals(currTypeId)) {
						resultNodes.add(currNode);
					}
				}
			}
		}
		return resultNodes;
	}

	/**
	 * 
	 * @param workspace
	 * @param id
	 * @return
	 */
	public INode getRootNode(IWorkspace workspace, String id) {
		if (id == null) {
			return null;
		}
		INode node = null;
		IResource[] members = workspace.getRootMembers();
		for (IResource currMember : members) {
			if (currMember instanceof INode) {
				INode currNode = (INode) currMember;
				try {
					NodeDescription currDesc = currNode.getDescription();
					if (currDesc != null) {
						String currId = currDesc.getId();
						if (id.equals(currId)) {
							node = currNode;
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return node;
	}

	/**
	 * 
	 * @param workspace
	 * @param id
	 * @return
	 */
	public INode getRootNodeByName(IWorkspace workspace, String name) {
		if (name == null) {
			return null;
		}
		INode node = null;
		IResource[] members = workspace.getRootMembers();
		for (IResource currMember : members) {
			if (currMember instanceof INode) {
				INode currNode = (INode) currMember;
				String currName = currNode.getName();
				if (name.equals(currName)) {
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
	 * @param id
	 * @return
	 */
	public boolean rootNodeExists(IWorkspace workspace, String id) {
		boolean exists = false;
		if (id != null) {
			IResource[] members = workspace.getRootMembers();
			for (IResource currMember : members) {
				if (currMember instanceof INode) {
					INode currNode = (INode) currMember;
					try {
						NodeDescription currDesc = currNode.getDescription();
						if (currDesc != null) {
							String currId = currDesc.getId();
							if (id.equals(currId)) {
								exists = true;
								break;
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return exists;
	}

	/**
	 * 
	 * @param workspace
	 * @param name
	 * @return
	 */
	public boolean rootNodeExistsByName(IWorkspace workspace, String name) {
		boolean exists = false;
		if (name != null) {
			IResource[] members = workspace.getRootMembers();
			for (IResource currMember : members) {
				if (currMember instanceof INode) {
					INode currNode = (INode) currMember;
					String currName = currNode.getName();
					if (name.equals(currName)) {
						exists = true;
						break;
					}
				}
			}
		}
		return exists;
	}

}
