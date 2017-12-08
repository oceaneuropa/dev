package org.orbit.component.model.tier3.domain.dto;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Responses;
import org.origin.common.rest.util.ResponseUtil;

public class DomainManagementResponseConverter {

	protected static DomainManagementResponseConverter INSTANCE = new DomainManagementResponseConverter();

	public static DomainManagementResponseConverter getInstance() {
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

	public TransferAgentConfig[] convertToTransferAgentConfigs(Response response) {
		List<TransferAgentConfig> taConfigs = new ArrayList<TransferAgentConfig>();
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
		return taConfigs.toArray(new TransferAgentConfig[taConfigs.size()]);
	}

	public TransferAgentConfig convertToTransferAgentConfig(Response response) {
		TransferAgentConfig taConfig = null;
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
