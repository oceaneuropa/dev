package org.orbit.infra.runtime.datacast.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.datacast.DataTubeConfigDTO;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.datacast.service.DataTubeConfig;
import org.orbit.infra.runtime.util.AbstractDataCastCommand;
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class GetDataTubeConfigCommand extends AbstractDataCastCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.GetDataTubeConfigCommand";

	public GetDataTubeConfigCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__GET_DATATUBE_CONFIG.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String configId = request.getStringParameter("id");
		if (configId == null || configId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'id' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		DataCastService dataCastService = getService();

		DataTubeConfig dataTubeConfig = dataCastService.getDataTubeConfig(configId);
		if (dataTubeConfig == null) {
			// ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "DataTubeConfig cannot be created");
			// return Response.status(Status.BAD_REQUEST).entity(error).build();
			return Response.ok().build();
		}

		DataTubeConfigDTO dataTubeConfigDTO = ModelConverter.DATA_CAST.toDTO(dataTubeConfig);
		return Response.ok().entity(dataTubeConfigDTO).build();
	}

}
