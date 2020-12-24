package org.orbit.infra.runtime.subscription.ws.command.source;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsSource;
import org.orbit.infra.model.subs.dto.SubsSourceDTO;
import org.orbit.infra.runtime.subscription.SubsServerService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ListSubsSourcesWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.ListSubsSourcesWSCommand";

	public ListSubsSourcesWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__LIST_SOURCES.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasTypeParam = request.hasParameter("type");

		SubsServerService service = getService();

		List<SubsSource> sources = null;
		if (hasTypeParam) {
			String type = request.getStringParameter("type");
			sources = service.getSources(type);
		} else {
			sources = service.getSources();
		}

		List<SubsSourceDTO> sourceDTOs = new ArrayList<SubsSourceDTO>();
		if (sources != null) {
			for (SubsSource source : sources) {
				SubsSourceDTO sourceDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(source);
				if (sourceDTO != null) {
					sourceDTOs.add(sourceDTO);
				}
			}
		}

		return Response.ok().entity(sourceDTOs).build();
	}

}
