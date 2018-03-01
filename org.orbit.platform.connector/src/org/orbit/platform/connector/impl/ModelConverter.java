package org.orbit.platform.connector.impl;

import java.util.ArrayList;
import java.util.List;

import org.orbit.platform.model.dto.ExtensionDTO;
import org.orbit.platform.model.dto.ExtensionInfo;
import org.orbit.platform.model.dto.ExtensionInfoImpl;
import org.orbit.platform.model.dto.ProcessDTO;
import org.orbit.platform.model.dto.ProcessInfo;
import org.orbit.platform.model.dto.ProcessInfoImpl;

public class ModelConverter {

	public static ModelConverter INSTANCE = new ModelConverter();

	/**
	 * 
	 * @param extensionDTOs
	 * @return
	 */
	public List<ExtensionInfo> toExtensionInfos(List<ExtensionDTO> extensionDTOs) {
		List<ExtensionInfo> extensionInfos = new ArrayList<ExtensionInfo>();
		if (extensionDTOs != null) {
			for (ExtensionDTO extensionDTO : extensionDTOs) {
				ExtensionInfo extensionInfo = toExtensionInfo(extensionDTO);
				if (extensionInfo != null) {
					extensionInfos.add(extensionInfo);
				}
			}
		}
		return extensionInfos;
	}

	/**
	 * 
	 * @param extensionDTO
	 * @return
	 */
	public ExtensionInfo toExtensionInfo(ExtensionDTO extensionDTO) {
		if (extensionDTO == null) {
			return null;
		}
		ExtensionInfoImpl extensionInfo = new ExtensionInfoImpl();
		extensionInfo.setTypeId(extensionDTO.getTypeId());
		extensionInfo.setId(extensionDTO.getId());
		return extensionInfo;
	}

	/**
	 * 
	 * @param processDTOs
	 * @return
	 */
	public List<ProcessInfo> toProcessInfos(List<ProcessDTO> processDTOs) {
		List<ProcessInfo> processInfos = new ArrayList<ProcessInfo>();
		if (processDTOs != null) {
			for (ProcessDTO processDTO : processDTOs) {
				ProcessInfo processInfo = toProcessInfo(processDTO);
				if (processInfo != null) {
					processInfos.add(processInfo);
				}
			}
		}
		return processInfos;
	}

	/**
	 * 
	 * @param processDTO
	 * @return
	 */
	public ProcessInfo toProcessInfo(ProcessDTO processDTO) {
		if (processDTO == null) {
			return null;
		}
		ProcessInfoImpl processInfo = new ProcessInfoImpl();
		processInfo.setPID(processDTO.getPID());
		processInfo.setName(processDTO.getName());
		return processInfo;
	}

}
