package org.origin.common.resources.node.internal.utils;

public class NodespaceBuilder {

	private static NodespaceBuilder INSTANCE = new NodespaceBuilder();

	public static NodespaceBuilder getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param nodespaceDir
	 * @param nodeId
	 * @param nodeName
	 * @return
	 */
	// public NodeManifest createNode(File nodespaceDir, String nodeId, String nodeName) {
	// NodeManifest nodeManifest = null;
	//
	// INodespace nodespace = NodespaceHelper.getInstance().getNodespace(nodespaceDir);
	// if (nodespace != null) {
	// nodeManifest = new NodeManifest();
	// nodeManifest.setNodeId(nodeId);
	// nodeManifest.setNodeName(nodeName);
	//
	// nodespace.getNodeManifests().add(nodeManifest);
	// }
	//
	// return nodeManifest;
	// }

	/**
	 * 
	 * @param nodespaceDir
	 * @param nodeId
	 * @return
	 */
	// public NodeManifest deleteNode(File nodespaceDir, String nodeId) {
	// NodeManifest nodeManifest = null;
	//
	// INodespace nodespace = NodespaceHelper.getInstance().getNodespace(nodespaceDir);
	// if (nodespace != null) {
	// nodeManifest = NodespaceHelper.getInstance().getNodeManifest(nodespaceDir, nodeId);
	// if (nodeManifest != null) {
	// nodespace.getNodeManifests().remove(nodeManifest);
	// }
	// }
	//
	// return nodeManifest;
	// }

}
