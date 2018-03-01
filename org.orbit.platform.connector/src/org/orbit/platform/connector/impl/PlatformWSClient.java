package org.orbit.platform.connector.impl;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.platform.model.dto.ExtensionDTO;
import org.orbit.platform.model.dto.ProcessDTO;
import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class PlatformWSClient extends AbstractWSClient {

	public PlatformWSClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	public Builder updateHeaders(Builder builder) {
		return super.updateHeaders(builder);
	}

	/**
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensions/?extensionTypeId={extensionTypeId}
	 * 
	 * @param extensionTypeId
	 * @return
	 */
	public List<ExtensionDTO> getExtensions(String extensionTypeId) throws ClientException {
		List<ExtensionDTO> serviceExtensionDTOs = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("extensions");
			if (extensionTypeId != null) {
				target = target.queryParam("extensionTypeId", extensionTypeId);
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			serviceExtensionDTOs = response.readEntity(new GenericType<List<ExtensionDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (serviceExtensionDTOs == null) {
			serviceExtensionDTOs = Collections.emptyList();
		}
		return serviceExtensionDTOs;
	}

	/**
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensions?extensionTypeId={extensionTypeId}&extensionId={extensionId}
	 * 
	 * @param extensionTypeId
	 * @param extensionId
	 * @return
	 */
	public ExtensionDTO getExtension(String extensionTypeId, String extensionId) throws ClientException {
		ExtensionDTO serviceExtensionDTO = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("extensions").queryParam("extensionTypeId", extensionTypeId).queryParam("extensionId", extensionId);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			List<ExtensionDTO> serviceExtensionDTOs = response.readEntity(new GenericType<List<ExtensionDTO>>() {
			});
			if (serviceExtensionDTOs != null && !serviceExtensionDTOs.isEmpty()) {
				serviceExtensionDTO = serviceExtensionDTOs.get(0);
			}
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return serviceExtensionDTO;
	}

	/**
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/processes?extensionTypeId={extensionTypeId}&extensionId={extensionId}
	 * 
	 * @param extensionTypeId
	 * @return
	 */
	public List<ProcessDTO> getProcesses(String extensionTypeId, String extensionId) throws ClientException {
		List<ProcessDTO> serviceInstanceDTOs = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("processes");
			if (extensionTypeId != null) {
				target = target.queryParam("extensionTypeId", extensionTypeId);
			}
			if (extensionId != null) {
				target = target.queryParam("extensionId", extensionId);
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			serviceInstanceDTOs = response.readEntity(new GenericType<List<ProcessDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (serviceInstanceDTOs == null) {
			serviceInstanceDTOs = Collections.emptyList();
		}
		return serviceInstanceDTOs;
	}

}

// /**
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extension_types
// *
// * @return
// */
// public List<String> getExtensionTypeIds() throws ClientException {
// List<String> extensionTypeIds = null;
// Response response = null;
// try {
// WebTarget target = getRootPath().path("extension_types");
// Builder builder = target.request(MediaType.APPLICATION_JSON);
// response = updateHeaders(builder).get();
// checkResponse(target, response);
//
// extensionTypeIds = response.readEntity(new GenericType<List<String>>() {
// });
// } catch (ClientException e) {
// handleException(e);
// } finally {
// ResponseUtil.closeQuietly(response, true);
// }
// if (extensionTypeIds == null) {
// extensionTypeIds = Collections.emptyList();
// }
// return extensionTypeIds;
// }

// /**
// * URL (POST): {scheme}://{host}:{port}/{contextRoot}/services/ (Body parameter: ServiceRequestDTO)
// *
// * @param extensionTypeId
// * @param extensionId
// * @param properties
// * @return
// */
// public StatusDTO startService(String extensionTypeId, String extensionId, Map<String, Object> properties) throws ClientException {
// StatusDTO status = null;
// Response response = null;
// try {
// ServiceRequestDTO startServiceRequestDTO = new ServiceRequestDTO("start_service", extensionTypeId, extensionId, properties);
//
// WebTarget target = getRootPath().path("services");
// Builder builder = target.request(MediaType.APPLICATION_JSON);
// response = updateHeaders(builder).post(Entity.json(new GenericEntity<ServiceRequestDTO>(startServiceRequestDTO) {
// }));
// checkResponse(target, response);
//
// status = response.readEntity(StatusDTO.class);
//
// } catch (ClientException e) {
// handleException(e);
// } finally {
// ResponseUtil.closeQuietly(response, true);
// }
// return status;
// }
//
// /**
// * URL (POST): {scheme}://{host}:{port}/{contextRoot}/services/ (Body parameter: ServiceRequestDTO)
// *
// * @param extensionTypeId
// * @param extensionId
// * @param properties
// * @return
// */
// public StatusDTO stopService(String extensionTypeId, String extensionId, Map<String, Object> properties) throws ClientException {
// StatusDTO status = null;
// Response response = null;
// try {
// ServiceRequestDTO stopServiceRequestDTO = new ServiceRequestDTO("stop_service", extensionTypeId, extensionId, properties);
//
// WebTarget target = getRootPath().path("services");
// Builder builder = target.request(MediaType.APPLICATION_JSON);
// response = updateHeaders(builder).post(Entity.json(new GenericEntity<ServiceRequestDTO>(stopServiceRequestDTO) {
// }));
// checkResponse(target, response);
//
// status = response.readEntity(StatusDTO.class);
//
// } catch (ClientException e) {
// handleException(e);
// } finally {
// ResponseUtil.closeQuietly(response, true);
// }
// return status;
// }

/// **
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/processes/?extensionTypeId={extensionTypeId}&extensionId={extensionId}
// *
// * @param extensionTypeId
// * @param extensionId
// * @return
// */
// public ProcessInfoDTO getServiceInstance(String extensionTypeId, String extensionId) throws ClientException {
// ProcessInfoDTO serviceInstanceDTO = null;
// Response response = null;
// try {
// WebTarget target = getRootPath().path("extensions").queryParam("extensionTypeId", extensionTypeId).queryParam("extensionId", extensionId);
// Builder builder = target.request(MediaType.APPLICATION_JSON);
// response = updateHeaders(builder).get();
// checkResponse(target, response);
//
// serviceInstanceDTO = response.readEntity(ProcessInfoDTO.class);
//
// } catch (ClientException e) {
// handleException(e);
// } finally {
// ResponseUtil.closeQuietly(response, true);
// }
// return serviceInstanceDTO;
// }
