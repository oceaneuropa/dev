package org.orbit.component.connector.tier3.domainmanagement;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.orbit.component.api.tier3.domainmanagement.MachineConfig;
import org.orbit.component.api.tier3.domainmanagement.NodeConfig;
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Responses;
import org.origin.common.rest.util.ResponseUtil;

public class ResponseConverter {

	protected static ResponseConverter INSTANCE = new ResponseConverter();

	public static ResponseConverter getInstance() {
		return INSTANCE;
	}

	public MachineConfig[] convertToMachineConfigs(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}

		Responses responses = ResponseUtil.parseResponses(response);

		List<MachineConfig> machineConfigs = new ArrayList<MachineConfig>();
		// Response response = responses.getResponse(Response.class);
		// if (response != null) {
		// Object bodyObj = response.getBody();
		// if (bodyObj instanceof List) {
		// List<?> elements = (List<?>) bodyObj;
		// for (Object element : elements) {
		// if (element instanceof Map<?, ?>) {
		// Map<?, ?> map = (Map<?, ?>) element;
		// MachineConfig machine = DomainManagementConverter.INSTANCE.toMachineConfig(map);
		// if (machine != null) {
		// machineConfigs.add(machine);
		// }
		// }
		// }
		// }
		// }
		return machineConfigs.toArray(new MachineConfig[machineConfigs.size()]);
	}

	public MachineConfig convertToMachineConfig(Response response) {
		MachineConfig machineConfig = null;
		// Response response = responses.getResponse(Response.class);
		// if (response != null) {
		// Object bodyObj = response.getBody();
		// if (bodyObj instanceof Map<?, ?>) {
		// Map<?, ?> map = (Map<?, ?>) bodyObj;
		// machineConfig = DomainManagementConverter.INSTANCE.toMachineConfig(map);
		// }
		// }
		return machineConfig;
	}

	public PlatformConfig[] convertToPlatformConfigs(Response response) {
		List<PlatformConfig> platformConfigs = new ArrayList<PlatformConfig>();
		// Response response = responses.getResponse(Response.class);
		// if (response != null) {
		// Object bodyObj = response.getBody();
		// if (bodyObj instanceof List) {
		// List<?> elements = (List<?>) bodyObj;
		// for (Object element : elements) {
		// if (element instanceof Map<?, ?>) {
		// Map<?, ?> map = (Map<?, ?>) element;
		// TransferAgentConfig ta = DomainManagementConverter.INSTANCE.toTransferAgentConfig(map);
		// if (ta != null) {
		// taConfigs.add(ta);
		// }
		// }
		// }
		// }
		// }
		return platformConfigs.toArray(new PlatformConfig[platformConfigs.size()]);
	}

	public PlatformConfig convertToPlatformConfig(Response response) {
		PlatformConfig taConfig = null;
		// Response response = responses.getResponse(Response.class);
		// if (response != null) {
		// Object bodyObj = response.getBody();
		// if (bodyObj instanceof Map<?, ?>) {
		// Map<?, ?> map = (Map<?, ?>) bodyObj;
		// taConfig = DomainManagementConverter.INSTANCE.toTransferAgentConfig(map);
		// }
		// }
		return taConfig;
	}

	public NodeConfig[] convertToNodeConfigs(Response response) {
		List<NodeConfig> nodeConfigs = new ArrayList<NodeConfig>();
		// Response response = responses.getResponse(Response.class);
		// if (response != null) {
		// Object bodyObj = response.getBody();
		// if (bodyObj instanceof List) {
		// List<?> elements = (List<?>) bodyObj;
		// for (Object element : elements) {
		// if (element instanceof Map<?, ?>) {
		// Map<?, ?> map = (Map<?, ?>) element;
		// NodeConfig nodeConfig = DomainManagementConverter.INSTANCE.toNodeConfig(map);
		// if (nodeConfig != null) {
		// nodeConfigs.add(nodeConfig);
		// }
		// }
		// }
		// }
		// }
		return nodeConfigs.toArray(new NodeConfig[nodeConfigs.size()]);
	}

	public NodeConfig convertToNodeConfig(Response response) {
		NodeConfig nodeConfig = null;
		// Response response = responses.getResponse(Response.class);
		// if (response != null) {
		// Object bodyObj = response.getBody();
		// if (bodyObj instanceof Map<?, ?>) {
		// Map<?, ?> map = (Map<?, ?>) bodyObj;
		// nodeConfig = DomainManagementConverter.INSTANCE.toNodeConfig(map);
		// }
		// }
		return nodeConfig;
	}

}
