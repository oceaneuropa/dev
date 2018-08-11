package org.orbit.component.runtime.tier2.appstore.ws;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier2.appstore.AppManifestDTO;
import org.orbit.component.model.tier2.appstore.AppQueryDTO;
import org.orbit.component.runtime.model.appstore.AppManifest;
import org.orbit.component.runtime.model.appstore.AppQuery;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.component.runtime.util.ModelConverter;
import org.orbit.platform.sdk.token.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;

/*
 * Apps web service resource.
 * 
 * {contextRoot}: /orbit/v1/appstore
 * 
 * App metadata.
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/apps?type={type}
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/apps/query (Body parameter: AppQueryDTO)
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
 * URL (PUT):    {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/apps?appId={appId}&appVersion={appVersion}
 * 
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN, OrbitRoles.APP_STORE_ADMIN })
@Path("/apps")
@Produces(MediaType.APPLICATION_JSON)
public class AppStoreWSAppsResource extends AbstractWSApplicationResource {

	@Inject
	public AppStoreService service;

	protected AppStoreService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("AppStoreService is not available.");
		}
		return this.service;
	}

	/**
	 * Get apps.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps?type={type}
	 * 
	 * @param type
	 * @return
	 */
	@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.USER })
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApps(@QueryParam("type") String type) {
		AppStoreService service = getService();

		List<AppManifestDTO> appDTOs = new ArrayList<AppManifestDTO>();
		try {
			List<AppManifest> apps = service.getApps(type);
			if (apps != null) {
				for (AppManifest app : apps) {
					AppManifestDTO appDTO = ModelConverter.AppStore.toAppDTO(app);
					appDTOs.add(appDTO);
				}
			}
		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(appDTOs).build();
	}

	/**
	 * Get apps.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/apps/query (Body parameter: AppQueryDTO)
	 * 
	 * @param queryDTO
	 * @return
	 */
	@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.USER })
	@POST
	@Path("query")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getApps(AppQueryDTO queryDTO) {
		AppStoreService service = getService();

		List<AppManifestDTO> appDTOs = new ArrayList<AppManifestDTO>();
		try {
			AppQuery query = ModelConverter.AppStore.toAppQuery(queryDTO);
			List<AppManifest> apps = service.getApps(query);
			if (apps != null) {
				for (AppManifest app : apps) {
					AppManifestDTO appDTO = ModelConverter.AppStore.toAppDTO(app);
					appDTOs.add(appDTO);
				}
			}
		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(appDTOs).build();
	}

	/**
	 * Add an app.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
	 * 
	 * @param newAppRequestDTO
	 * @return
	 */
	@Secured(roles = { OrbitRoles.SYSTEM_ADMIN, OrbitRoles.APP_STORE_ADMIN })
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addApp(AppManifestDTO newAppRequestDTO) {
		if (newAppRequestDTO == null) {
			ErrorDTO nullAppDTOError = new ErrorDTO("newAppRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullAppDTOError).build();
		}

		AppManifestDTO newAppDTO = null;

		AppStoreService service = getService();
		try {
			String appId = newAppRequestDTO.getAppId();
			String appVersion = newAppRequestDTO.getAppVersion();

			if (service.appExists(appId, appVersion)) {
				ErrorDTO appExistsError = new ErrorDTO(String.format("App '%s' already exists.", appVersion));
				return Response.status(Status.BAD_REQUEST).entity(appExistsError).build();
			}

			AppManifest appToAdd = ModelConverter.AppStore.toApp(newAppRequestDTO);
			AppManifest newApp = service.addApp(appToAdd);
			if (newApp == null) {
				ErrorDTO newAppNotCreated = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "App is not added.");
				return Response.status(Status.NOT_FOUND).entity(newAppNotCreated).build();
			}

			newAppDTO = ModelConverter.AppStore.toAppDTO(newApp);

		} catch (ServerException e) {
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
	@Secured(roles = { OrbitRoles.SYSTEM_ADMIN })
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateApp(AppManifestDTO updateAppRequestDTO) {
		if (updateAppRequestDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("updateAppRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		boolean succeed = false;
		AppStoreService service = getService();
		try {
			AppManifest appToUpdate = ModelConverter.AppStore.toApp(updateAppRequestDTO);
			succeed = service.updateApp(appToUpdate);

		} catch (ServerException e) {
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
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/apps?appId={appId}&appVersion={appVersion}
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 */
	@Secured(roles = { OrbitRoles.SYSTEM_ADMIN })
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteApp(@QueryParam("appId") String appId, @QueryParam("appVersion") String appVersion) {
		if (appId == null || appId.isEmpty()) {
			ErrorDTO nullAppIdError = new ErrorDTO("appId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullAppIdError).build();
		}

		boolean succeed = false;
		AppStoreService service = getService();
		try {
			succeed = service.deleteApp(appId, appVersion);

		} catch (ServerException e) {
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

}

// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}/exists
// *
// * Upload an app.
// * URL (POST): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}/content (FormData: InputStream and FormDataContentDisposition)
// *
// * Download an app.
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}/content

// /**
// * Get an app.
// *
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}
// *
// * @param appId
// * @param appVersion
// * @return
// */
// @GET
// @Path("{appId}/{appVersion}")
// @Produces(MediaType.APPLICATION_JSON)
// public Response getApp(@PathParam("appId") String appId, @PathParam("appVersion") String appVersion) {
// AppManifestDTO appDTO = null;
//
// // AppStoreService service = getService(AppStoreService.class);
// AppStoreService service = getService();
// try {
// AppManifestRTO app = service.getApp(appId, appVersion);
// if (app == null) {
// ErrorDTO appNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("App cannot be found for '%s'.", appId));
// return Response.status(Status.NOT_FOUND).entity(appNotFoundError).build();
// }
// appDTO = AppStoreModelConverter.getInstance().toDTO(app);
//
// } catch (ServerException e) {
// ErrorDTO error = handleError(e, e.getCode(), true);
// return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
// }
//
// return Response.ok().entity(appDTO).build();
// }
//
// /**
// * Check whether an app exists.
// *
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}/exists
// *
// * @param appId
// * @param appVersion
// * @return
// */
// @GET
// @Path("{appId}/{appVersion}/exists")
// @Produces(MediaType.APPLICATION_JSON)
// public Response appExists(@PathParam("appId") String appId, @PathParam("appVersion") String appVersion) {
// Map<String, Boolean> result = new HashMap<String, Boolean>();
// // AppStoreService service = getService(AppStoreService.class);
// AppStoreService service = getService();
// try {
// boolean exists = service.appExists(appId, appVersion);
// result.put("exists", exists);
//
// } catch (ServerException e) {
// ErrorDTO error = handleError(e, e.getCode(), true);
// return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
// }
// return Response.ok().entity(result).build();
// }
