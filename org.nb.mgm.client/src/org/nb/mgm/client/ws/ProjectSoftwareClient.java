package org.nb.mgm.client.ws;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import org.nb.mgm.model.dto.SoftwareDTO;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;

/*
 * Project Software resource client.
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software (Body parameter: SoftwareDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software (Body parameter: SoftwareDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}
 * 
 * Project Software content resource client.
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}/content
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}/content (FormDataParam: InputStream, FormDataContentDisposition)
 * 
 */
public class ProjectSoftwareClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public ProjectSoftwareClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Get Software lists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software
	 * 
	 * @param projectId
	 * @return SoftwareDTO list.
	 * @throws ClientException
	 */
	public List<SoftwareDTO> getProjectSoftware(String projectId) throws ClientException {
		List<SoftwareDTO> softwareDTOs = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("software").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			softwareDTOs = response.readEntity(new GenericType<List<SoftwareDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		if (softwareDTOs == null) {
			softwareDTOs = Collections.emptyList();
		}
		return softwareDTOs;
	}

	/**
	 * Get Software.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}
	 * 
	 * @param projectId
	 * @param softwareId
	 * 
	 * @return SoftwareDTO
	 * @throws ClientException
	 */
	public SoftwareDTO getProjectSoftware(String projectId, String softwareId) throws ClientException {
		SoftwareDTO softwareDTO = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("software").path(softwareId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			softwareDTO = response.readEntity(SoftwareDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return softwareDTO;
	}

	/**
	 * Add Software to a Project.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software (Body parameter: SoftwareDTO)
	 * 
	 * @param projectId
	 * @param newSoftwareRequest
	 *            Body parameter for the new Software.
	 * 
	 * @return new SoftwareDTO
	 * @throws ClientException
	 */
	public SoftwareDTO addProjectSoftware(String projectId, SoftwareDTO newSoftwareRequest) throws ClientException {
		SoftwareDTO newSoftwareDTO = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("software").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<SoftwareDTO>(newSoftwareRequest) {
			}));
			checkResponse(response);

			newSoftwareDTO = response.readEntity(SoftwareDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return newSoftwareDTO;
	}

	/**
	 * Update Software.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software (Body parameter: SoftwareDTO)
	 * 
	 * @param projectId
	 * @param updateSoftwareRequest
	 *            Body parameter for updating the Software.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateProjectSoftware(String projectId, SoftwareDTO updateSoftwareRequest) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("software").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<SoftwareDTO>(updateSoftwareRequest) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Delete a Software from a Project.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}
	 * 
	 * @param projectId
	 * @param softwareId
	 * 
	 * @return Delete status
	 * @throws ClientException
	 */
	public StatusDTO deleteProjectSoftware(String projectId, String softwareId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("software").path(softwareId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
			response.close();

		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Upload Software file to Project.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}/content
	 * 
	 * FormDataParam: InputStream, FormDataContentDisposition
	 * 
	 * @param softwareFile
	 * @param destFilePath
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public StatusDTO uploadProjectSoftwareFile(String projectId, String softwareId, File softwareFile) throws ClientException {
		try {
			MultiPart multipart = new FormDataMultiPart();
			{
				FileDataBodyPart filePart = new FileDataBodyPart("file", softwareFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
				{
					FormDataContentDisposition.FormDataContentDispositionBuilder formBuilder = FormDataContentDisposition.name("file");
					formBuilder.fileName(URLEncoder.encode(softwareFile.getName(), "UTF-8"));
					formBuilder.size(softwareFile.length());
					formBuilder.modificationDate(new Date(softwareFile.lastModified()));
					filePart.setFormDataContentDisposition(formBuilder.build());
				}
				multipart.bodyPart(filePart);
			}

			WebTarget target = getRootPath().path("projects").path(projectId).path("software").path(softwareId).path("content");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.entity(multipart, multipart.getMediaType()));
			checkResponse(response);

			StatusDTO status = response.readEntity(StatusDTO.class);
			return status;

		} catch (ClientException e) {
			handleException(e);
		} catch (UnsupportedEncodingException e) {
			handleException(e);
		}

		return null;
	}

}