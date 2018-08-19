package org.orbit.infra.connector.extensionregistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.extensionregistry.ExtensionItemCreateRequest;
import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.orbit.infra.model.extensionregistry.ExtensionItemSetPropertiesRequest;
import org.orbit.infra.model.extensionregistry.ExtensionItemUpdateRequest;
import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.util.ResponseUtil;
import org.origin.common.util.StringUtil;

/*
 * Extension Registry Client
 * 
 * contextRoot:  /orbit/v1/extensionregistry/
 * 
 * The service
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/ping
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/echo?message={message}
 * 
 * ExtensionItems
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/extensionitems?platformId={platformId}&typeId={typeId}
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/extensionitems (Body parameter: ExtensionItemCreateRequest)
 * URL (PUT):    {scheme}://{host}:{port}/{contextRoot}/extensionitems (Body parameter: ExtensionItemUpdateRequest)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/extensionitems?platformId={platformId}
 * 
 * ExtensionItem
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/extensionitem?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/extensionitem?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
 * 
 * ExtensionItem Properties
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties (Body parameter: ExtensionItemSetPropertiesRequest)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties?platformId={platformId}&typeId={typeId}&extensionId={extensionId}&propertyNames={propertyNames}
 * 
 */
public class ExtensionRegistryWSClient extends WSClient {

	/**
	 * 
	 * @param config
	 */
	public ExtensionRegistryWSClient(WSClientConfiguration config) {
		super(config);
	}

	/**
	 * Get extension items.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensionitems?platformId={platformId}&typeId={typeId}
	 * 
	 * @param platformId
	 * @param typeId
	 * @return
	 * @throws ClientException
	 */
	public List<ExtensionItemDTO> getExtensionItems(String platformId, String typeId) throws ClientException {
		List<ExtensionItemDTO> DTOs = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("extensionitems");
			target = target.queryParam("platformId", platformId);
			if (typeId != null) {
				target = target.queryParam("typeId", typeId);
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			DTOs = response.readEntity(new GenericType<List<ExtensionItemDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (DTOs == null) {
			DTOs = Collections.emptyList();
		}
		return DTOs;
	}

	/**
	 * Get an extension item.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensionitem/?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws ClientException
	 */
	public ExtensionItemDTO getExtensionItem(String platformId, String typeId, String extensionId) throws ClientException {
		ExtensionItemDTO DTO = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("extensionitem");
			target = target.queryParam("platformId", platformId);
			target = target.queryParam("typeId", typeId);
			target = target.queryParam("extensionId", extensionId);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			DTO = response.readEntity(new GenericType<ExtensionItemDTO>() {
			});
		} catch (ClientException e) {
			int code = e.getCode();
			if (Status.NOT_FOUND.getStatusCode() == code) {
				return null;
			}
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return DTO;
	}

	/**
	 * Add an extension item.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/extensionitems (Body parameter: ExtensionItemCreateRequest)
	 * 
	 * @param platformId
	 * @param typeId
	 * @param name
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public ExtensionItemDTO addExtensionItem(String platformId, String typeId, String extensionId, String name, String description, Map<String, Object> properties) throws ClientException {
		ExtensionItemDTO newDTO = null;
		Response response = null;
		try {
			ExtensionItemCreateRequest request = new ExtensionItemCreateRequest();
			request.setPlatformId(platformId);
			request.setTypeId(typeId);
			request.setExtensionId(extensionId);
			request.setName(name);
			request.setDescription(description);
			request.setProperties(properties);

			WebTarget target = getRootPath().path("extensionitems");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<ExtensionItemCreateRequest>(request) {
			}));
			checkResponse(target, response);

			newDTO = response.readEntity(ExtensionItemDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return newDTO;
	}

	/**
	 * Update an extension item.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/extensionitems (Body parameter: ExtensionItemUpdateRequest)
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param newTypeId
	 * @param newExtensionId
	 * @param newName
	 * @param newDescription
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO updateExtensionItem(String platformId, String typeId, String extensionId, String newTypeId, String newExtensionId, String newName, String newDescription, Map<String, Object> properties) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			ExtensionItemUpdateRequest request = new ExtensionItemUpdateRequest();
			request.setPlatformId(platformId);
			request.setTypeId(typeId);
			request.setExtensionId(extensionId);

			if (newTypeId != null) {
				request.setNewTypeId(newTypeId);
			}
			if (newExtensionId != null) {
				request.setNewExtensionId(newExtensionId);
			}
			if (newName != null) {
				request.setNewName(newName);
			}
			if (newDescription != null) {
				request.setNewDescription(newDescription);
			}
			if (properties != null) {
				request.setProperties(properties);
			}

			WebTarget target = getRootPath().path("extensionitems");

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<ExtensionItemUpdateRequest>(request) {
			}));
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Remove extension items.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/extensionitems?platformId={platformId}
	 * 
	 * @param platformId
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeExtensionItems(String platformId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("extensionitems");
			target = target.queryParam("platformId", platformId);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Remove an extension item.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/extensionitem?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeExtensionItem(String platformId, String typeId, String extensionId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("extensionitem");
			target = target.queryParam("platformId", platformId);
			target = target.queryParam("typeId", typeId);
			target = target.queryParam("extensionId", extensionId);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Get extension properties.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties?platformId={platformId}&typeId={typeId}&extensionId={extensionId}
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws ClientException
	 */
	public Map<String, Object> getExtensionProperties(String platformId, String typeId, String extensionId) throws ClientException {
		Map<String, Object> properties = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("extensionitem").path("properties");
			target = target.queryParam("platformId", platformId);
			target = target.queryParam("typeId", typeId);
			target = target.queryParam("extensionId", extensionId);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			properties = response.readEntity(new GenericType<Map<String, Object>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	/**
	 * Set extension properties.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties (Body parameter: ExtensionItemSetPropertiesRequest)
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO setExtensionProperties(String platformId, String typeId, String extensionId, Map<String, Object> properties) throws ClientException {
		if (properties == null) {
			throw new IllegalArgumentException("properties is null.");
		}

		StatusDTO status = null;
		Response response = null;
		try {
			ExtensionItemSetPropertiesRequest request = new ExtensionItemSetPropertiesRequest();
			request.setPlatformId(platformId);
			request.setTypeId(typeId);
			request.setExtensionId(extensionId);

			WebTarget target = getRootPath().path("extensionitem").path("properties");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<ExtensionItemSetPropertiesRequest>(request) {
			}));
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Remove extension properties.
	 * 
	 * URL (DELETE):
	 * {scheme}://{host}:{port}/{contextRoot}/extensionitem/properties?platformId={platformId}&typeId={typeId}&extensionId={extensionId}&propertyNames={
	 * propertyNames}
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propertyNames
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO removeExtensionProperties(String platformId, String typeId, String extensionId, List<String> propertyNames) throws ClientException {
		if (propertyNames == null || propertyNames.isEmpty()) {
			throw new IllegalArgumentException("propertyNames is empty.");
		}

		StatusDTO status = null;
		Response response = null;
		try {
			String propertyNamesStr = StringUtil.toString(propertyNames);

			WebTarget target = getRootPath().path("extensionitem").path("properties");
			target = target.queryParam("platformId", platformId);
			target = target.queryParam("typeId", typeId);
			target = target.queryParam("extensionId", extensionId);
			target = target.queryParam("propertynames", propertyNamesStr);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

}
