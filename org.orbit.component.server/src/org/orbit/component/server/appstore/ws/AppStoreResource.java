package org.orbit.component.server.appstore.ws;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.orbit.component.model.appstore.ModelConverter;
import org.orbit.component.model.appstore.dto.AppManifestDTO;
import org.orbit.component.model.appstore.dto.AppQueryDTO;
import org.orbit.component.model.appstore.exception.AppStoreException;
import org.orbit.component.model.appstore.runtime.AppManifestRTO;
import org.orbit.component.model.appstore.runtime.AppQueryRTO;
import org.orbit.component.server.appstore.service.AppStoreService;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * Apps resource.
 * 
 * App metadata.
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/appstore/apps?namespace={namespace}&categoryId={categoryId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/appstore/apps/query (Body parameter: AppQueryDTO)
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/appstore/apps/{appId}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/appstore/apps/{appId}/exists
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/appstore/apps (Body parameter: AppManifestDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/appstore/apps (Body parameter: AppManifestDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/appstore/apps/{appId}
 * 
 * Download an app.
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/appstore/apps/{appId}/content
 * 
 * Upload an app.
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/appstore/apps/{appId}/content (FormData: InputStream and FormDataContentDisposition)
 * 
 */
@Path("/appstore/apps")
@Produces(MediaType.APPLICATION_JSON)
public class AppStoreResource extends AbstractApplicationResource {

	/**
	 * Get apps.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps?namespace={namespace}&categoryId={categoryId}
	 * 
	 * @param namespace
	 * @param categoryId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApps(@QueryParam("namespace") String namespace, @QueryParam("categoryId") String categoryId) {
		AppStoreService appStoreService = getService(AppStoreService.class);

		List<AppManifestDTO> appDTOs = new ArrayList<AppManifestDTO>();
		try {
			List<AppManifestRTO> apps = appStoreService.getApps(namespace, categoryId);
			if (apps != null) {
				for (AppManifestRTO app : apps) {
					AppManifestDTO appDTO = ModelConverter.getInstance().toDTO(app);
					appDTOs.add(appDTO);
				}
			}
		} catch (AppStoreException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(appDTOs).build();
	}

	/**
	 * Get apps.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps/query (Body parameter: AppQueryDTO)
	 * 
	 * @param queryDTO
	 * @return
	 */
	@POST
	@Path("query")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getApps(AppQueryDTO queryDTO) {
		AppStoreService appStoreService = getService(AppStoreService.class);

		List<AppManifestDTO> appDTOs = new ArrayList<AppManifestDTO>();
		try {
			AppQueryRTO query = ModelConverter.getInstance().toRTO(queryDTO);
			List<AppManifestRTO> apps = appStoreService.getApps(query);
			if (apps != null) {
				for (AppManifestRTO app : apps) {
					AppManifestDTO appDTO = ModelConverter.getInstance().toDTO(app);
					appDTOs.add(appDTO);
				}
			}
		} catch (AppStoreException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(appDTOs).build();
	}

	/**
	 * Get an app.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}
	 * 
	 * @param appId
	 * @return
	 */
	@GET
	@Path("{appId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApp(@PathParam("appId") String appId) {
		AppManifestDTO appDTO = null;

		AppStoreService appStoreService = getService(AppStoreService.class);
		try {
			AppManifestRTO app = appStoreService.getApp(appId);
			if (app == null) {
				ErrorDTO appNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("App cannot be found for '%s'.", appId));
				return Response.status(Status.NOT_FOUND).entity(appNotFoundError).build();
			}
			appDTO = ModelConverter.getInstance().toDTO(app);

		} catch (AppStoreException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(appDTO).build();
	}

	/**
	 * Check whether an app exists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/exists
	 * 
	 * @param appId
	 * @return
	 */
	@GET
	@Path("{appId}/exists")
	@Produces(MediaType.APPLICATION_JSON)
	public Response appExists(@PathParam("appId") String appId) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		AppStoreService appStoreService = getService(AppStoreService.class);
		try {
			boolean exists = appStoreService.appExists(appId);
			result.put("exists", exists);

		} catch (AppStoreException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(result).build();
	}

	/**
	 * Add an app.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
	 * 
	 * @param newAppRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createApp(AppManifestDTO newAppRequestDTO) {
		if (newAppRequestDTO == null) {
			ErrorDTO nullAppDTOError = new ErrorDTO("newAppRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullAppDTOError).build();
		}

		AppManifestDTO newAppDTO = null;

		AppStoreService appStoreService = getService(AppStoreService.class);
		try {
			String appId = newAppRequestDTO.getAppId();
			if (appId == null || appId.isEmpty()) {
				appId = UUID.randomUUID().toString();
				newAppRequestDTO.setAppId(appId);
			}
			if (appStoreService.appExists(appId)) {
				ErrorDTO appExistsError = new ErrorDTO(String.format("App '%s' already exists.", appId));
				return Response.status(Status.BAD_REQUEST).entity(appExistsError).build();
			}

			AppManifestRTO appToAdd = ModelConverter.getInstance().toRTO(newAppRequestDTO);
			AppManifestRTO newApp = appStoreService.addApp(appToAdd);
			if (newApp == null) {
				ErrorDTO newAppNotCreated = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "App is not added.");
				return Response.status(Status.NOT_FOUND).entity(newAppNotCreated).build();
			}

			newAppDTO = ModelConverter.getInstance().toDTO(newApp);

		} catch (AppStoreException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(newAppDTO).build();
	}

	/**
	 * Update an app.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
	 * 
	 * @param updateAppRequestDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateApp(AppManifestDTO updateAppRequestDTO) {
		if (updateAppRequestDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("updateAppRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		boolean succeed = false;
		AppStoreService appStoreService = getService(AppStoreService.class);
		try {
			AppManifestRTO appToUpdate = ModelConverter.getInstance().toRTO(updateAppRequestDTO);
			succeed = appStoreService.updateApp(appToUpdate);

		} catch (AppStoreException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "App is updated successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "App is not update.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Delete an app.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}
	 * 
	 * @param appId
	 * @return
	 */
	@DELETE
	@Path("/{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteApp(@PathParam(value = "appId") String appId) {
		if (appId == null || appId.isEmpty()) {
			ErrorDTO nullAppIdError = new ErrorDTO("appId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullAppIdError).build();
		}

		boolean succeed = false;
		AppStoreService appStoreService = getService(AppStoreService.class);
		try {
			succeed = appStoreService.deleteApp(appId);

		} catch (AppStoreException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "App is deleted successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "App is not deleted.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Upload an app.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/content (FormData: InputStream and FormDataContentDisposition)
	 * 
	 * @param appId
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @return
	 */
	@POST
	@Path("/{appId}/content")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadApp( //
			@PathParam(value = "appId") String appId, //
			@FormDataParam("file") InputStream uploadedInputStream, //
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		if (appId == null || appId.isEmpty()) {
			ErrorDTO nullAppIdError = new ErrorDTO("appId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullAppIdError).build();
		}

		boolean succeed = false;

		String fileName = fileDetail.getFileName();
		AppStoreService appStoreService = getService(AppStoreService.class);
		try {
			AppManifestRTO app = appStoreService.getApp(appId);
			if (app == null) {
				ErrorDTO appExistsError = new ErrorDTO("App does not exists.");
				return Response.status(Status.BAD_REQUEST).entity(appExistsError).build();
			}

			succeed = appStoreService.uploadApp(appId, fileName, uploadedInputStream);

		} catch (AppStoreException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		} finally {
			IOUtil.closeQuietly(uploadedInputStream, true);
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, String.format("App '%s' is successfully uploaded.", fileName));
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, String.format("App '%s' is not uploaded.", fileName));
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Download an app.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/content
	 * 
	 * @param appId
	 * @return
	 */
	@GET
	@Path("/{appId}/content")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM })
	public Response downloadApp(@PathParam(value = "appId") String appId) {
		if (appId == null || appId.isEmpty()) {
			ErrorDTO nullAppIdError = new ErrorDTO("appId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullAppIdError).build();
		}

		byte[] fileContentBytes = null;
		String fileName = null;

		AppStoreService appStoreService = getService(AppStoreService.class);
		try {
			AppManifestRTO app = appStoreService.getApp(appId);
			if (app == null) {
				ErrorDTO appExistsError = new ErrorDTO("App does not exists.");
				return Response.status(Status.BAD_REQUEST).entity(appExistsError).build();
			}

			fileName = app.getFileName();

			fileContentBytes = appStoreService.downloadApp(appId);

		} catch (AppStoreException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (fileContentBytes == null) {
			fileContentBytes = new byte[0];
		}

		return Response.ok(fileContentBytes, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + fileName).build();
	}

}
