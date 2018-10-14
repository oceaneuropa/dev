package org.orbit.infra.runtime.datacast.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.datacast.DataTubeConfigDTO;
import org.orbit.infra.runtime.common.ws.AbstractDataCastCommand;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.datacast.service.DataTubeConfig;
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class ListDataTubeConfigsCommand extends AbstractDataCastCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.ListDataTubeConfigsCommand";

	public ListDataTubeConfigsCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__LIST_DATATUBE_CONFIGS.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		DataCastService dataCastService = getService();

		DataTubeConfig[] dataTubeConfigs = null;
		if (request.hasParameter("enabled")) {
			boolean isEnabled = request.getBooleanParameter("enabled");
			dataTubeConfigs = dataCastService.getDataTubeConfigs(isEnabled);

		} else {
			dataTubeConfigs = dataCastService.getDataTubeConfigs();
		}

		List<DataTubeConfigDTO> dataTubeConfigDTOs = new ArrayList<DataTubeConfigDTO>();
		if (dataTubeConfigs != null) {
			for (DataTubeConfig currDataTubeConfig : dataTubeConfigs) {
				DataTubeConfigDTO dataTubeConfigDTO = ModelConverter.DataCast.toDTO(currDataTubeConfig);
				if (dataTubeConfigDTO != null) {
					dataTubeConfigDTOs.add(dataTubeConfigDTO);
				}
			}
		}

		return Response.ok().entity(dataTubeConfigDTOs).build();
	}

}
