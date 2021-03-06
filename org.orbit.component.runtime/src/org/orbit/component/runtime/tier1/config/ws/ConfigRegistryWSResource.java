package org.orbit.component.runtime.tier1.config.ws;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
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

import org.orbit.component.model.tier1.configregistry.SetPropertiesDTO;
import org.orbit.component.runtime.model.configregistry.EPath;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistry;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceV0;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;

/*
 * Config registry resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{accountId}/properties?path={path}
 * 
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/{accountId}/properties?path={path} (Body parameter: PropertiesDTO)
 * 
 */
@Secured(roles = { OrbitRoles.USER })
@Path("/{accountId}")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigRegistryWSResource extends AbstractWSApplicationResource {

	protected static final Properties EMPTY_PROPERTIES = new Properties();
	protected static final Map<String, String> EMPTY_MAP = new LinkedHashMap<String, String>();

	@Inject
	public ConfigRegistryServiceV0 service;

	public ConfigRegistryServiceV0 getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("ConfigRegistryService is not available.");
		}
		return this.service;
	}

	/**
	 * Get properties.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{accountId}/properties?path={path}
	 * 
	 * @param accountId
	 * @param path
	 * @return
	 */
	@GET
	@Path("properties")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties(@PathParam("accountId") String accountId, @QueryParam("path") String path) {
		// ConfigRegistryService service = getService(ConfigRegistryService.class);
		ConfigRegistryServiceV0 service = getService();

		Map<String, String> properties = null;
		try {
			ConfigRegistry configRegistry = service.getRegistry(accountId);
			properties = configRegistry.getProperties(new EPath(path));

		} catch (ServerException e) {
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
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/{accountId}/{root}/{path}/properties (Body parameter: PropertiesDTO)
	 * 
	 * @param newAppRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setProperties(@PathParam("accountId") String accountId, @QueryParam("path") String path, SetPropertiesDTO propertiesDTO) {
		if (propertiesDTO == null) {
			ErrorDTO nullPropertiesError = new ErrorDTO("propertiesDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullPropertiesError).build();
		}

		// // ConfigurationRegistryService service = getService(ConfigurationRegistryService.class);
		// ConfigRegistryService service = getService();
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
