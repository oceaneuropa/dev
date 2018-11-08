package org.orbit.infra.runtime.datacast.ws.command.channel;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class AllocateDataTubeIdForNewChannelCommand extends AbstractInfraCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.AllocateDataTubeIdForNewChannelCommand";

	public AllocateDataTubeIdForNewChannelCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__ALLOCATE_DATA_TUBE_ID.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		DataCastService dataCastService = getService();

		String dataTubeId = dataCastService.allocateDataTubeIdForNewChannel();

		Map<String, String> result = new HashMap<String, String>();
		result.put("data_tube_id", dataTubeId);
		return Response.status(Status.OK).entity(result).build();
	}

}
