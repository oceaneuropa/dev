package org.orbit.infra.runtime.datacast.ws.command;

import java.util.HashMap;
import java.util.List;
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

public class RemoveDataTubeConfigPropertiesCommand extends AbstractDataCastCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.RemoveDataTubeConfigPropertiesCommand";

	public RemoveDataTubeConfigPropertiesCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__REMOVE_DATATUBE_CONFIG_PROPERTIES.equalsIgnoreCase(requestName)) {
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

		List<String> propertyNames = null;
		if (request.hasParameter("property_names")) {
			propertyNames = (List<String>) request.getListParameter("properties");
		}
		if (propertyNames == null || propertyNames.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'property_names' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean hasSucceed = false;
		boolean hasFailed = false;

		DataCastService dataCastService = getService();

		DataTubeConfig dataTubeConfig = dataCastService.getDataTubeConfig(id);
		if (dataTubeConfig != null) {
			Map<String, Object> currProperties = dataTubeConfig.getProperties();
			for (String propName : propertyNames) {
				currProperties.remove(propName);
			}

			boolean currSucceed = dataCastService.updateDataTubeProperties(id, currProperties);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		boolean succeed = false;
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
