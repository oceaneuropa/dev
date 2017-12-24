package org.orbit.component.connector.tier2.appstore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.orbit.component.model.tier2.appstore.AppManifestDTO;
import org.orbit.component.model.tier2.appstore.AppQueryDTO;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.util.ResponseUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * AppSore client.
 * 
 * {contextRoot} example:
 * /orbit/v1/appstore
 * 
 * App metadata.
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps?type={type}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps/query (Body parameter: AppQueryDTO)
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}/exists
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}
 * 
 * Upload an app.
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}/content (FormData: InputStream and FormDataContentDisposition)
 * 
 * Download an app.
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}/content
 * 
 */
public class AppStoreWSClient extends AbstractWSClient {

	public AppStoreWSClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Get apps.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps?type={type}
	 * 
	 * @param type
	 * @return
	 * @throws ClientException
	 */
	public List<AppManifestDTO> getApps(String type) throws ClientException {
		List<AppManifestDTO> apps = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("apps");
			if (type != null) {
				target = target.queryParam("type", type);
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			apps = response.readEntity(new GenericType<List<AppManifestDTO>>() {
			});

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (apps == null) {
			apps = Collections.emptyList();
		}
		return apps;
	}

	/**
	 * Get apps.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps/query (Body parameter: AppQueryDTO)
	 * 
	 * @param queryDTO
	 * @return
	 * @throws ClientException
	 */
	public List<AppManifestDTO> getApps(AppQueryDTO queryDTO) throws ClientException {
		List<AppManifestDTO> apps = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("apps/query");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<AppQueryDTO>(queryDTO) {
			}));
			checkResponse(target, response);

			apps = response.readEntity(new GenericType<List<AppManifestDTO>>() {
			});

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (apps == null) {
			apps = Collections.emptyList();
		}
		return apps;
	}

	/**
	 * Get an app.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ClientException
	 */
	public AppManifestDTO getApp(String appId, String appVersion) throws ClientException {
		AppManifestDTO app = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("apps").path(appId).path(appVersion);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			app = response.readEntity(AppManifestDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return app;
	}

	/**
	 * Check whether an app exists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ClientException
	 */
	public boolean appExists(String appId, String appVersion) throws ClientException {
		Response response = null;
		try {
			WebTarget target = getRootPath().path("apps").path(appId).path(appVersion).path("exists");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			String responseString = response.readEntity(String.class);

			ObjectMapper mapper = createObjectMapper(false);
			Map<?, ?> result = mapper.readValue(responseString, Map.class);
			if (result != null && result.containsKey("exists")) { //$NON-NLS-1$
				Object value = result.get("exists"); //$NON-NLS-1$
				if (value instanceof Boolean) {
					return (boolean) value;
				}
			}

		} catch (ClientException | IOException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return false;
	}

	/**
	 * Add an app.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
	 * 
	 * @param newAppRequestDTO
	 * @return
	 * @throws ClientException
	 */
	public AppManifestDTO addApp(AppManifestDTO newAppRequestDTO) throws ClientException {
		AppManifestDTO newApp = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("apps");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<AppManifestDTO>(newAppRequestDTO) {
			}));
			checkResponse(target, response);

			newApp = response.readEntity(AppManifestDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return newApp;
	}

	/**
	 * Update an app.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
	 * 
	 * @param updateAppRequestDTO
	 *            Body parameter for updating the app.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateApp(AppManifestDTO updateAppRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("apps");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<AppManifestDTO>(updateAppRequestDTO) {
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
	 * Upload an app file.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}/content (FormData: InputStream and FormDataContentDisposition)
	 * 
	 * @param appId
	 * @param appVersion
	 * @param filePath
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public StatusDTO uploadAppArchive(String appId, String appVersion, Path filePath) throws ClientException {
		File file = filePath.toFile();
		if (file == null || !file.exists()) {
			throw new ClientException(401, "File doesn't exist.");
		}
		if (!file.isFile()) {
			throw new ClientException(401, "File " + file.getAbsolutePath() + " is not a single file.");
		}

		try {
			MultiPart multipart = new FormDataMultiPart();
			{
				FileDataBodyPart filePart = new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE);
				{
					FormDataContentDisposition.FormDataContentDispositionBuilder formBuilder = FormDataContentDisposition.name("file");
					formBuilder.fileName(URLEncoder.encode(file.getName(), "UTF-8"));
					formBuilder.size(file.length());
					formBuilder.modificationDate(new Date(file.lastModified()));
					filePart.setFormDataContentDisposition(formBuilder.build());
				}
				multipart.bodyPart(filePart);
			}

			WebTarget target = getRootPath().path("apps").path(appId).path(appVersion).path("content");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.entity(multipart, multipart.getMediaType()));
			checkResponse(target, response);

			StatusDTO status = response.readEntity(StatusDTO.class);
			return status;

		} catch (ClientException | UnsupportedEncodingException e) {
			handleException(e);
		}

		return null;
	}

	/**
	 * Download app file.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}/content
	 * 
	 * @param appId
	 * @param appVersion
	 * @param output
	 * @return
	 * @throws ClientException
	 */
	public boolean downloadAppArchive(String appId, String appVersion, OutputStream output) throws ClientException {
		InputStream input = null;
		try {
			WebTarget target = getRootPath().path("apps").path(appId).path(appVersion).path("content");
			Builder builder = target.request(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM);
			Response response = updateHeaders(builder).get();
			checkResponse(target, response);

			input = response.readEntity(InputStream.class);

			if (input != null) {
				IOUtil.copy(input, output);
			}

		} catch (ClientException | IOException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(input, true);
		}
		return true;
	}

	/**
	 * Delete an app.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}
	 * 
	 * @param appId
	 * @param appVersion
	 * 
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO deleteApp(String appId, String appVersion) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("apps").path(appId).path(appVersion);
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
