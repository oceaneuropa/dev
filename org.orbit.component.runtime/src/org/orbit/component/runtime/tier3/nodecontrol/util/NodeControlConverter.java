package org.orbit.component.runtime.tier3.nodecontrol.util;

import java.net.URI;

import org.orbit.component.model.tier3.nodecontrol.INodeDTO;
import org.orbit.component.model.tier3.nodecontrol.INodespaceDTO;
import org.origin.common.resources.IPath;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.INodespace;

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
	public INodespaceDTO toDTO(INodespace nodespace) {
		INodespaceDTO dto = new INodespaceDTO();
		dto.setName(nodespace.getName());
		try {
			IPath fullpath = nodespace.getFullPath();
			URI uri = new URI(fullpath.toString());
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
	public INodeDTO toDTO(INode node) {
		INodeDTO dto = new INodeDTO();
		dto.setName(node.getName());
		try {
			IPath fullpath = node.getFullPath();
			URI uri = new URI(fullpath.toString());
			dto.setUri(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

}
