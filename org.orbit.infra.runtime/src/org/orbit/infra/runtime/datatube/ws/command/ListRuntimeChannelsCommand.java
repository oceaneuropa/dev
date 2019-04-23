package org.orbit.infra.runtime.datatube.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.datatube.RuntimeChannelDTO;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.datatube.service.RuntimeChannel;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class ListRuntimeChannelsCommand extends AbstractInfraCommand<DataTubeService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datatube.ListRuntimeChannelsCommand";

	public ListRuntimeChannelsCommand() {
		super(DataTubeService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATATUBE__LIST_RUNTIME_CHANNELS.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		DataTubeService dataTubeService = getService();

		RuntimeChannel[] runtimeChannels = dataTubeService.getRuntimeChannels();

		List<RuntimeChannelDTO> runtimeChannelDTOs = new ArrayList<RuntimeChannelDTO>();
		if (runtimeChannels != null) {
			for (RuntimeChannel currRuntimeChannel : runtimeChannels) {
				RuntimeChannelDTO runtimeChannelDTO = RuntimeModelConverter.DATA_TUBE.toDTO(currRuntimeChannel);
				if (runtimeChannelDTO != null) {
					runtimeChannelDTOs.add(runtimeChannelDTO);
				}
			}
		}

		return Response.ok().entity(runtimeChannelDTOs).build();
	}

}
