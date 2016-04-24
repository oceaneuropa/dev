package org.nb.mgm.ws.dto;

import org.nb.mgm.model.Artifact;
import org.nb.mgm.model.Home;
import org.nb.mgm.model.Machine;
import org.nb.mgm.model.MetaSector;
import org.nb.mgm.model.MetaSpace;
import org.nb.mgm.service.MgmException;
import org.origin.common.rest.dto.ErrorDTO;

public class DTOConverter {

	private static DTOConverter converter = new DTOConverter();

	public static DTOConverter getInstance() {
		return converter;
	}

	/**
	 * Convert MgmException object to Error DTO.
	 * 
	 * @param e
	 * @return
	 */
	public ErrorDTO toDTO(MgmException e) {
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
	 * Convert Machine runtime model to Machine DTO.
	 * 
	 * @param machine
	 * @return
	 */
	public MachineDTO toDTO(Machine machine) {
		if (machine == null) {
			return null;
		}
		MachineDTO dto = new MachineDTO();

		dto.setId(machine.getId());
		dto.setName(machine.getName());
		dto.setDescription(machine.getDescription());

		dto.setIpAddress(machine.getIpAddress());

		return dto;
	}

	/**
	 * Convert Home runtime model to Home DTO.
	 * 
	 * @param home
	 * @return
	 */
	public HomeDTO toDTO(Home home) {
		if (home == null) {
			return null;
		}
		HomeDTO dto = new HomeDTO();

		dto.setId(home.getId());
		dto.setName(home.getName());
		dto.setDescription(home.getDescription());

		dto.setUrl(home.getUrl());

		return dto;
	}

	/**
	 * Convert MetaSector runtime model to MetaSector DTO.
	 * 
	 * @param metaSector
	 * @return
	 */
	public MetaSectorDTO toDTO(MetaSector metaSector) {
		if (metaSector == null) {
			return null;
		}

		MetaSectorDTO dto = new MetaSectorDTO();

		dto.setId(metaSector.getId());
		dto.setName(metaSector.getName());
		dto.setDescription(metaSector.getDescription());

		return dto;
	}

	/**
	 * Convert MetaSpace runtime model to MetaSpace DTO.
	 * 
	 * @param metaSpace
	 * @return
	 */
	public MetaSpaceDTO toDTO(MetaSpace metaSpace) {
		if (metaSpace == null) {
			return null;
		}

		MetaSpaceDTO dto = new MetaSpaceDTO();

		dto.setId(metaSpace.getId());
		dto.setName(metaSpace.getName());
		dto.setDescription(metaSpace.getDescription());

		return dto;
	}

	/**
	 * Convert Artifact runtime model to Artifact DTO.
	 * 
	 * @param artifact
	 * @return
	 */
	public ArtifactDTO toDTO(Artifact artifact) {
		if (artifact == null) {
			return null;
		}

		ArtifactDTO dto = new ArtifactDTO();

		dto.setId(artifact.getId());
		dto.setName(artifact.getName());
		dto.setDescription(artifact.getDescription());

		dto.setVersion(artifact.getVersion());
		dto.setFilePath(artifact.getFilePath());
		dto.setFileName(artifact.getFileName());
		dto.setFileSize(artifact.getFileSize());
		dto.setChecksum(artifact.getChecksum());

		return dto;
	}

}
