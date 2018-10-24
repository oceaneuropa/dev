package org.orbit.infra.runtime.datacast.ws.command;

import java.util.Map;

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

public class CreateDataTubeConfigCommand extends AbstractDataCastCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.CreateDataTubeConfigCommand";

	public CreateDataTubeConfigCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__CREATE_DATATUBE_CONFIG.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String dataTubeId = request.getStringParameter("data_tube_id");
		if (dataTubeId == null || dataTubeId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'data_tube_id' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		String name = request.getStringParameter("name");
		boolean isEnabled = false;
		if (request.hasParameter("enabled")) {
			isEnabled = request.getBooleanParameter("enabled");
		}
		Map<String, Object> properties = null;
		if (request.hasParameter("properties")) {
			properties = (Map<String, Object>) request.getMapParameter("properties");
		}

		DataCastService dataCastService = getService();

		DataTubeConfig dataTubeConfig = dataCastService.createDataTubeConfig(dataTubeId, name, isEnabled, properties);
		if (dataTubeConfig == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "DataTubeConfig cannot be created");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		DataTubeConfigDTO dataTubeConfigDTO = ModelConverter.DATA_CAST.toDTO(dataTubeConfig);
		return Response.ok().entity(dataTubeConfigDTO).build();
	}

}
