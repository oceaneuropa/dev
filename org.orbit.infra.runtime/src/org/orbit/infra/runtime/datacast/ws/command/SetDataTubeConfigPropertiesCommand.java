package org.orbit.infra.runtime.datacast.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.common.ws.AbstractDataCastCommand;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.datacast.service.DataTubeConfig;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class SetDataTubeConfigPropertiesCommand extends AbstractDataCastCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.SetDataTubeConfigPropertiesCommand";

	public SetDataTubeConfigPropertiesCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__SET_DATATUBE_CONFIG_PROPERTIES.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String id = request.getStringParameter("id");
		if (id == null || id.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'id' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Map<String, Object> properties = null;
		if (request.hasParameter("properties")) {
			properties = (Map<String, Object>) request.getMapParameter("properties");
		}
		if (properties == null || properties.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'properties' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		DataCastService dataCastService = getService();

		boolean succeed = false;

		DataTubeConfig dataTubeConfig = dataCastService.getDataTubeConfig(id);
		if (dataTubeConfig != null) {
			Map<String, Object> existingProperties = dataTubeConfig.getProperties();
			existingProperties.putAll(properties);

			succeed = dataCastService.updateDataTubeProperties(id, existingProperties);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
