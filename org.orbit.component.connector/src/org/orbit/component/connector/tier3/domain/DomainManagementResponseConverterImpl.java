package org.orbit.component.connector.tier3.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainManagementResponseConverter;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.NodeConfig;
import org.orbit.component.api.tier3.domain.TransferAgentConfig;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class DomainManagementResponseConverterImpl implements DomainManagementResponseConverter {

	protected DomainManagementImpl domainManagementImpl;

	/**
	 * 
	 * @param domainManagementImpl
	 */
	public DomainManagementResponseConverterImpl(DomainManagementImpl domainManagementImpl) {
		this.domainManagementImpl = domainManagementImpl;
	}

	@Override
	public MachineConfig[] convertToMachineConfigs(Responses responses) {
		List<MachineConfig> machineConfigs = new ArrayList<MachineConfig>();
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof List) {
				List<?> elements = (List<?>) bodyObj;
				for (Object element : elements) {
					if (element instanceof Map<?, ?>) {
						Map<?, ?> map = (Map<?, ?>) element;
						MachineConfig machine = DomainManagementConverter.INSTANCE.toMachineConfig(map);
						if (machine != null) {
							machineConfigs.add(machine);
						}
					}
				}
			}
		}
		return machineConfigs.toArray(new MachineConfig[machineConfigs.size()]);
	}

	@Override
	public MachineConfig convertToMachineConfig(Responses responses) {
		MachineConfig machineConfig = null;
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) bodyObj;
				machineConfig = DomainManagementConverter.INSTANCE.toMachineConfig(map);
			}
		}
		return machineConfig;
	}

	@Override
	public TransferAgentConfig[] convertToTransferAgentConfigs(Responses responses) {
		List<TransferAgentConfig> taConfigs = new ArrayList<TransferAgentConfig>();
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof List) {
				List<?> elements = (List<?>) bodyObj;
				for (Object element : elements) {
					if (element instanceof Map<?, ?>) {
						Map<?, ?> map = (Map<?, ?>) element;
						TransferAgentConfig ta = DomainManagementConverter.INSTANCE.toTransferAgentConfig(map);
						if (ta != null) {
							taConfigs.add(ta);
						}
					}
				}
			}
		}
		return taConfigs.toArray(new TransferAgentConfig[taConfigs.size()]);
	}

	@Override
	public TransferAgentConfig convertToTransferAgentConfig(Responses responses) {
		TransferAgentConfig taConfig = null;
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) bodyObj;
				taConfig = DomainManagementConverter.INSTANCE.toTransferAgentConfig(map);
			}
		}
		return taConfig;
	}

	@Override
	public NodeConfig[] convertToNodeConfigs(Responses responses) {
		List<NodeConfig> nodeConfigs = new ArrayList<NodeConfig>();
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof List) {
				List<?> elements = (List<?>) bodyObj;
				for (Object element : elements) {
					if (element instanceof Map<?, ?>) {
						Map<?, ?> map = (Map<?, ?>) element;
						NodeConfig nodeConfig = DomainManagementConverter.INSTANCE.toNodeConfig(map);
						if (nodeConfig != null) {
							nodeConfigs.add(nodeConfig);
						}
					}
				}
			}
		}
		return nodeConfigs.toArray(new NodeConfig[nodeConfigs.size()]);
	}

	@Override
	public NodeConfig convertToNodeConfig(Responses responses) {
		NodeConfig nodeConfig = null;
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) bodyObj;
				nodeConfig = DomainManagementConverter.INSTANCE.toNodeConfig(map);
			}
		}
		return nodeConfig;
	}

}
