package org.nb.home.model.dto;

import java.util.Map;

import org.nb.home.model.exception.HomeException;
import org.nb.home.model.runtime.Workspace;
import org.nb.home.model.runtime.config.WorkspaceConfig;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.model.ErrorDTO;

public class DTOConverter {

	private static DTOConverter converter = new DTOConverter();

	public static DTOConverter getInstance() {
		return converter;
	}

	/**
	 * Convert HomeException object to Error DTO.
	 * 
	 * @param e
	 * @return
	 */
	public ErrorDTO toDTO(HomeException e) {
		if (e == null) {
			return null;
		}

		ErrorDTO dto = new ErrorDTO();

		dto.setCode(e.getCode());
		dto.setMessage(e.getMessage());

		if (e.getCause() != null) {
			String causeName = e.getCause().getClass().getName();
			String causeMessage = e.getCause().getMessage();
			dto.setException(causeName + " " + causeMessage);
		} else {
			String causeName = e.getClass().getName();
			dto.setException(causeName);
		}

		return dto;
	}

	/**
	 * Convert Workspace runtime model to Workspace DTO.
	 * 
	 * @param workspace
	 * @return
	 */
	public WorkspaceDTO toDTO(Workspace workspace) {
		if (workspace == null) {
			return null;
		}
		WorkspaceConfig workspaceConfig = workspace.getAdapter(WorkspaceConfig.class);
		return toDTO(workspaceConfig);
	}

	/**
	 * Convert Workspace runtime model to Workspace DTO.
	 * 
	 * @param workspace
	 * @return
	 */
	public WorkspaceDTO toDTO(WorkspaceConfig workspace) {
		if (workspace == null) {
			return null;
		}

		Map<String, Object> properties = workspace.getProperties();
		String propertiesString = JSONUtil.toJsonString(properties);

		WorkspaceDTO dto = new WorkspaceDTO();

		dto.setName(workspace.getName());
		dto.setManagementId(workspace.getManagementId());
		dto.setPropertiesString(propertiesString);

		return dto;
	}

}
