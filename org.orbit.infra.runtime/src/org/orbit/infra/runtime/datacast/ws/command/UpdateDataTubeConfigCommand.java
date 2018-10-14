package org.orbit.infra.runtime.datacast.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.common.ws.AbstractDataCastCommand;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class UpdateDataTubeConfigCommand extends AbstractDataCastCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.UpdateDataTubeConfigCommand";

	public UpdateDataTubeConfigCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__UPDATE_DATATUBE_CONFIG.equalsIgnoreCase(requestName)) {
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

		boolean hasSucceed = false;
		boolean hasFailed = false;

		DataCastService dataCastService = getService();

		if (request.hasParameter("enabled")) {
			boolean isEnabled = request.getBooleanParameter("enabled");
			boolean currSucceed = dataCastService.updateDataTubeEnabled(id, isEnabled);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		if (request.hasParameter("name")) {
			String name = request.getStringParameter("name");
			boolean currSucceed = dataCastService.updateDataTubeName(id, name);
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
