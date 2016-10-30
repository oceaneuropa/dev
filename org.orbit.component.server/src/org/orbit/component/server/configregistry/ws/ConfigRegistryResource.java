package org.orbit.component.server.configregistry.ws;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.api.configregistry.EPath;
import org.orbit.component.model.configregistry.dto.SetPropertiesDTO;
import org.orbit.component.model.configregistry.exception.ConfigRegistryException;
import org.orbit.component.server.configregistry.service.ConfigRegistry;
import org.orbit.component.server.configregistry.service.ConfigRegistryService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * Config registry resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{userId}/properties?path={path}
 * 
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/{userId}/properties?path={path} (Body parameter: PropertiesDTO)
 * 
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps?namespace={namespace}&categoryId={categoryId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps/query (Body parameter: AppQueryDTO)
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/exists
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}
 * 
 * Download an app.
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/content
 * 
 * Upload an app.
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps/{appId}/content (FormData: InputStream and FormDataContentDisposition)
 * 
 */
@Path("/configregistry/{userId}")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigRegistryResource extends AbstractApplicationResource {

	protected static final Properties EMPTY_PROPERTIES = new Properties();
	protected static final Map<String, String> EMPTY_MAP = new LinkedHashMap<String, String>();

	/**
	 * Get properties.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{userId}/properties?path={path}
	 * 
	 * @param userId
	 * @param path
	 * @return
	 */
	@GET
	@Path("properties")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties(@PathParam("userId") String userId, @QueryParam("path") String path) {
		ConfigRegistryService service = getService(ConfigRegistryService.class);

		Map<String, String> properties = null;
		try {
			ConfigRegistry configRegistry = service.getRegistry(userId);
			properties = configRegistry.getProperties(new EPath(path));

		} catch (ConfigRegistryException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		if (properties == null) {
			properties = EMPTY_MAP;
		}
		return Response.ok().entity(properties).build();
	}

	/**
	 * Add an app.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/{userId}/{root}/{path}/properties (Body parameter: PropertiesDTO)
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/apps (Body parameter: AppManifestDTO)
	 * 
	 * @param newAppRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setProperties(@PathParam("userId") String userId, @QueryParam("path") String path, SetPropertiesDTO propertiesDTO) {
		if (propertiesDTO == null) {
			ErrorDTO nullPropertiesError = new ErrorDTO("propertiesDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullPropertiesError).build();
		}

		// ConfigurationRegistryService configRegistryService = getService(ConfigurationRegistryService.class);
		// try {
		// configRegistryService.se
		//
		// String appId = newAppRequestDTO.getAppId();
		// if (appId == null || appId.isEmpty()) {
		// appId = UUID.randomUUID().toString();
		// newAppRequestDTO.setAppId(appId);
		// }
		// if (appStoreService.appExists(appId)) {
		// ErrorDTO appExistsError = new ErrorDTO(String.format("App '%s' already exists.", appId));
		// return Response.status(Status.BAD_REQUEST).entity(appExistsError).build();
		// }
		//
		// AppManifestRTO appToAdd = ModelConverter.getInstance().toRTO(newAppRequestDTO);
		// AppManifestRTO newApp = appStoreService.addApp(appToAdd);
		// if (newApp == null) {
		// ErrorDTO newAppNotCreated = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "App is not added.");
		// return Response.status(Status.NOT_FOUND).entity(newAppNotCreated).build();
		// }
		//
		// newAppDTO = ModelConverter.getInstance().toDTO(newApp);
		//
		// } catch (AppStoreException e) {
		// ErrorDTO error = handleError(e, e.getCode(), true);
		// return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		// }
		//
		// return Response.ok().entity(newAppDTO).build();
		return null;
	}

}
