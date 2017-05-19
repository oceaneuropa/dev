package org.orbit.component.model.tier2.appstore;

import org.origin.common.rest.model.ErrorDTO;

public class ModelConverter {

	private static ModelConverter converter = new ModelConverter();

	public static ModelConverter getInstance() {
		return converter;
	}

	// ------------------------------------------------------------------------------------------
	// RTO to DTO
	// ------------------------------------------------------------------------------------------
	/**
	 * Convert AppStoreException object to Error DTO.
	 * 
	 * @param e
	 * @return
	 */
	public ErrorDTO toDTO(AppStoreException e) {
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
	 * Convert AppManifestRTO to AppManifestDTO.
	 * 
	 * @param app
	 * @return
	 */
	public AppManifestDTO toDTO(AppManifestRTO app) {
		if (app == null) {
			return null;
		}
		AppManifestDTO dto = new AppManifestDTO();

		dto.setAppId(app.getAppId());
		dto.setNamespace(app.getNamespace());
		dto.setCategoryId(app.getCategoryId());
		dto.setName(app.getName());
		dto.setVersion(app.getVersion());
		dto.setPriority(app.getPriority());
		dto.setAppManifest(app.getAppManifest());
		dto.setDescription(app.getDescription());
		dto.setDateCreated(app.getDateCreated());
		dto.setDateModified(app.getDateModified());

		return dto;
	}

	// ------------------------------------------------------------------------------------------
	// DTO to RTO
	// ------------------------------------------------------------------------------------------
	/**
	 * Convert AppManifestDTO to AppManifestRTO.
	 * 
	 * @param app
	 * @return
	 */
	public AppManifestRTO toRTO(AppManifestDTO appDTO) {
		if (appDTO == null) {
			return null;
		}
		AppManifestRTO app = new AppManifestRTO();

		app.setAppId(appDTO.getAppId());
		app.setNamespace(appDTO.getNamespace());
		app.setCategoryId(appDTO.getCategoryId());
		app.setName(appDTO.getName());
		app.setVersion(appDTO.getVersion());
		app.setPriority(appDTO.getPriority());
		app.setAppManifest(appDTO.getAppManifest());
		app.setDescription(appDTO.getDescription());
		app.setDateCreated(appDTO.getDateCreated());
		app.setDateModified(appDTO.getDateModified());

		return app;
	}

	/**
	 * Convert AppQueryDTO to AppQueryRTO.
	 * 
	 * @param app
	 * @return
	 */
	public AppQueryRTO toRTO(AppQueryDTO queryDTO) {
		if (queryDTO == null) {
			return null;
		}
		AppQueryRTO query = new AppQueryRTO();

		// Set/Get
		query.setAppId(queryDTO.getAppId());
		query.setNamespace(queryDTO.getNamespace());
		query.setCategoryId(queryDTO.getCategoryId());
		query.setName(queryDTO.getName());
		query.setVersion(queryDTO.getVersion());
		query.setDescription(queryDTO.getDescription());

		// Where operator
		query.setAppId_oper(queryDTO.getAppId_oper());
		query.setNamespace_oper(queryDTO.getNamespace_oper());
		query.setCategoryId_oper(queryDTO.getCategoryId_oper());
		query.setName_oper(queryDTO.getName_oper());
		query.setVersion_oper(queryDTO.getVersion_oper());
		query.setDescription_oper(queryDTO.getDescription_oper());

		return query;
	}

}
