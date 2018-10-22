package org.orbit.infra.runtime.datacast.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.datacast.ChannelMetadataDTO;
import org.orbit.infra.runtime.common.ws.AbstractDataCastCommand;
import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class ListChannelMetadatasCommand extends AbstractDataCastCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.ListChannelMetadatasCommand";

	public ListChannelMetadatasCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__LIST_CHANNEL_METADATAS.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		DataCastService dataCastService = getService();

		ChannelMetadata[] channelMetadatas = null;
		if (request.hasParameter("data_tube_id")) {
			String dataTubeId = request.getStringParameter("data_tube_id");
			channelMetadatas = dataCastService.getChannelMetadatas(dataTubeId);

		} else {
			channelMetadatas = dataCastService.getChannelMetadatas();
		}

		List<ChannelMetadataDTO> channelMetadataDTOs = new ArrayList<ChannelMetadataDTO>();
		if (channelMetadatas != null) {
			for (ChannelMetadata currChannelMetadata : channelMetadatas) {
				ChannelMetadataDTO channelMetadataDTO = ModelConverter.DATA_CAST.toDTO(currChannelMetadata);
				if (channelMetadataDTO != null) {
					channelMetadataDTOs.add(channelMetadataDTO);
				}
			}
		}

		return Response.ok().entity(channelMetadataDTOs).build();
	}

}
