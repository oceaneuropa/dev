package org.orbit.component.runtime.tier2.appstore.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.orbit.component.model.tier2.appstore.AppManifestDTO;
import org.orbit.component.runtime.model.appstore.AppManifest;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.component.runtime.util.ModelConverter;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;

/*
 * App web service resource.
 * 
 * {contextRoot}: /orbit/v1/appstore
 * 
 * App metadata.
 * URL (GET):  {scheme}://{host}:{port}/{contextRoot}/app?appId={appId}&appVersion={appVersion}
 * URL (GET):  {scheme}://{host}:{port}/{contextRoot}/app/exists?appId={appId}&appVersion={appVersion}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/app/{appId}/{appVersion}/content (FormData: InputStream and FormDataContentDisposition) // Upload an app.
 * URL (GET):  {scheme}://{host}:{port}/{contextRoot}/app/content?appId={appId}&appVersion={appVersion} //Download an app
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/app/upload?id={id}&appId={appId}&appVersion={appVersion} (FormData: InputStream and FormDataContentDisposition) // Upload an app.
 */
@Path("/app")
@Produces(MediaType.APPLICATION_JSON)
public class AppStoreWSAppResource extends AbstractWSApplicationResource {

	@Inject
	public AppStoreService service;

	protected AppStoreService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("AppStoreService is not available.");
		}
		return this.service;
	}

	/**
	 * Get an app.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/app?appId={appId}&appVersion={appVersion}
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApp(@QueryParam("appId") String appId, @QueryParam("appVersion") String appVersion) {
		AppManifestDTO appDTO = null;

		AppStoreService service = getService();
		try {
			AppManifest app = service.getApp(appId, appVersion);
			if (app == null) {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("App cannot be found for '%s'.", appId));
				return Response.status(Status.NOT_FOUND).entity(error).build();
			}
			appDTO = ModelConverter.AppStore.toDTO(app);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(appDTO).build();
	}

	/**
	 * Check whether an app exists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/app/exists?appId={appId}&appVersion={appVersion}
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 */
	@GET
	@Path("/exists")
	@Produces(MediaType.APPLICATION_JSON)
	public Response exists(@QueryParam("appId") String appId, @QueryParam("appVersion") String appVersion) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		AppStoreService service = getService();
		try {
			boolean exists = service.appExists(appId, appVersion);
			result.put("exists", exists);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(result).build();
	}

	// /**
	// * Upload an app.
	// *
	// * URL (POST): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/{appVersion}/content (FormData: InputStream and FormDataContentDisposition)
	// *
	// * @param appId
	// * @param appVersion
	// * @param uploadedInputStream
	// * @param fileDetail
	// * @return
	// */
	// @POST
	// @Path("/{appId}/{appVersion}/content")
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// public Response uploadApp0( //
	// @PathParam(value = "appId") String appId, //
	// @PathParam(value = "appVersion") String appVersion, //
	// @FormDataParam("file") InputStream uploadedInputStream, //
	// @FormDataParam("file") FormDataContentDisposition fileDetail) {
	//
	// if (appId == null || appId.isEmpty()) {
	// ErrorDTO nullAppIdError = new ErrorDTO("appId is null.");
	// return Response.status(Status.BAD_REQUEST).entity(nullAppIdError).build();
	// }
	// if (appVersion == null || appVersion.isEmpty()) {
	// ErrorDTO nullAppVersionError = new ErrorDTO("appVersion is null.");
	// return Response.status(Status.BAD_REQUEST).entity(nullAppVersionError).build();
	// }
	//
	// boolean succeed = false;
	//
	// String fileName = fileDetail.getFileName();
	//
	// AppStoreService service = getService();
	// try {
	// AppManifest app = service.getApp(appId, appVersion);
	// if (app == null) {
	// ErrorDTO appExistsError = new ErrorDTO("App does not exists.");
	// return Response.status(Status.BAD_REQUEST).entity(appExistsError).build();
	// }
	//
	// succeed = service.uploadApp(appId, appVersion, fileName, uploadedInputStream);
	//
	// } catch (ServerException e) {
	// ErrorDTO error = handleError(e, e.getCode(), true);
	// return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
	// } finally {
	// IOUtil.closeQuietly(uploadedInputStream, true);
	// }
	//
	// if (succeed) {
	// StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, String.format("App '%s' is successfully uploaded.", fileName));
	// return Response.ok().entity(statusDTO).build();
	// } else {
	// StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, String.format("App '%s' is not uploaded.", fileName));
	// return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
	// }
	// }

	/**
	 * Upload an app.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/app/content?id={id}&appId={appId}&appVersion={appVersion} (FormData: InputStream and
	 * FormDataContentDisposition)
	 * 
	 * @param appId
	 * @param appVersion
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @return
	 */
	@POST
	@Path("/content")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response setContent( //
			@QueryParam(value = "id") int id, //
			@QueryParam(value = "appId") String appId, //
			@QueryParam(value = "appVersion") String appVersion, //
			@FormDataParam("file") InputStream uploadedInputStream, //
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		if (appId == null || appId.isEmpty()) {
			ErrorDTO nullAppIdError = new ErrorDTO("appId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullAppIdError).build();
		}
		if (appVersion == null || appVersion.isEmpty()) {
			ErrorDTO nullAppVersionError = new ErrorDTO("appVersion is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullAppVersionError).build();
		}

		boolean succeed = false;

		String fileName = fileDetail.getFileName();

		AppStoreService service = getService();
		try {
			AppManifest app = null;
			if (id > 0) {
				app = service.getApp(id);
			} else {
				app = service.getApp(appId, appVersion);
			}
			if (app == null) {
				ErrorDTO appExistsError = new ErrorDTO("App does not exists.");
				return Response.status(Status.BAD_REQUEST).entity(appExistsError).build();
			}

			succeed = service.setContent(appId, appVersion, fileName, uploadedInputStream);

		} catch (ServerException e) {
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
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/app/content?appId={appId}&appVersion={appVersion}
	 *
	 * @param appId
	 * @param appVersion
	 * @return
	 */
	@GET
	@Path("/content")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM })
	public Response getContent(@QueryParam("appId") String appId, @QueryParam("appVersion") String appVersion) {
		if (appId == null || appId.isEmpty()) {
			ErrorDTO nullAppIdError = new ErrorDTO("appId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullAppIdError).build();
		}

		byte[] bytes = null;
		String fileName = null;

		InputStream input = null;
		try {
			AppStoreService service = getService();
			AppManifest app = service.getApp(appId, appVersion);
			if (app == null) {
				ErrorDTO appExistsError = new ErrorDTO("App does not exists.");
				return Response.status(Status.BAD_REQUEST).entity(appExistsError).build();
			}

			fileName = app.getAppFileName();

			input = service.getContentInputStream(appId, appVersion);
			if (input != null) {
				bytes = IOUtil.toByteArray(input);
			}

		} catch (IOException e) {
			ErrorDTO error = handleError(e, StatusDTO.RESP_500, true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();

		} finally {
			IOUtil.closeQuietly(input, true);
		}

		if (bytes == null) {
			bytes = new byte[0];
		}

		return Response.ok(bytes, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + fileName).build();
	}

}
