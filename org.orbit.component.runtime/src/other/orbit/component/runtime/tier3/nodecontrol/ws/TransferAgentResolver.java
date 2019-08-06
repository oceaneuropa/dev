package other.orbit.component.runtime.tier3.nodecontrol.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.ComponentServices;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;

public class TransferAgentResolver implements ContextResolver<NodeControlService> {

	@Override
	public NodeControlService getContext(Class<?> clazz) {
		return ComponentServices.getInstance().getTransferAgentService();
	}

}
