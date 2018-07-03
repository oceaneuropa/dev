package org.orbit.component.runtime.tier3.nodecontrol.util;

import java.net.URI;

import org.orbit.component.model.tier3.nodecontrol.NodeDTO;
import org.orbit.component.model.tier3.nodecontrol.NodespaceDTO;
import org.origin.common.resources.IPath;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.INodespace;
import org.origin.common.resources.node.NodeDescription;
import org.origin.common.resources.node.NodespaceDescription;

public class NodeControlConverter {

	private static NodeControlConverter converter = new NodeControlConverter();

	public static NodeControlConverter getInstance() {
		return converter;
	}

	/**
	 * 
	 * @param nodespace
	 * @return
	 */
	public NodespaceDTO toDTO(INodespace nodespace) {
		NodespaceDTO dto = new NodespaceDTO();
		try {
			NodespaceDescription desc = nodespace.getDescription();
			dto.setId(desc.getId());

			dto.setAttributes(desc.getAttributes());

			dto.setName(nodespace.getName());

			IPath fullpath = nodespace.getFullPath();
			URI uri = new URI(fullpath.getPathString());
			dto.setUri(uri);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public NodeDTO toDTO(INode node) {
		NodeDTO dto = new NodeDTO();
		try {
			NodeDescription desc = node.getDescription();
			dto.setId(desc.getId());

			dto.setAttributes(desc.getAttributes());

			dto.setName(node.getName());

			IPath fullpath = node.getFullPath();
			URI uri = new URI(fullpath.getPathString());
			dto.setUri(uri);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

}
