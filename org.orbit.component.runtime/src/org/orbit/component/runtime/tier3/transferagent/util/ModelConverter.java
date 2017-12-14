package org.orbit.component.runtime.tier3.transferagent.util;

import java.net.URI;

import org.orbit.component.model.tier3.transferagent.dto.INodeDTO;
import org.orbit.component.model.tier3.transferagent.dto.INodespaceDTO;
import org.origin.core.resources.IPath;
import org.origin.core.resources.node.INode;
import org.origin.core.resources.node.INodespace;

public class ModelConverter {

	private static ModelConverter converter = new ModelConverter();

	public static ModelConverter getInstance() {
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
