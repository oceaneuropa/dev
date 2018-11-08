package org.orbit.infra.runtime.datacast.ws;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataCast web service resource.
 * 
 * {contextRoot} example: /orbit/v1/datacast
 *
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.USER })
@javax.ws.rs.Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DataCastWSResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(DataCastWSResource.class);

	@Inject
	public DataCastService service;

	public DataCastService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DataCastService is not available.");
		}
		return this.service;
	}

	@GET
	@Path("allocate")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response allocate(@QueryParam("type") String type) {
		DataCastService service = getService();
		if (RequestConstants.ALLOCATE_TYPE__DATA_TUBE_ID.equals(type)) {
			try {
				String dataTubeId = service.allocateDataTubeIdForNewChannel();

				Map<String, String> result = new HashMap<String, String>();
				result.put("data_tube_id", dataTubeId);
				return Response.status(Status.OK).entity(result).build();

			} catch (ServerException e) {
				e.printStackTrace();
				String statusCode = String.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode());
				ErrorDTO error = handleError(e, statusCode, true);
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
			}
		}
		return Response.ok().build();
	}

	@POST
	@Path("request")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response request(@Context HttpHeaders httpHeaders, Request request) {
		return super.request(httpHeaders, request);
	}

}
