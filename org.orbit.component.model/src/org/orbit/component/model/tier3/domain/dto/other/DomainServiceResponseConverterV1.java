package org.orbit.component.model.tier3.domain.dto.other;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.component.model.tier3.domain.dto.ModelConverter;
import org.orbit.component.model.tier3.domain.dto.MachineConfig;
import org.orbit.component.model.tier3.domain.dto.NodeConfig;
import org.orbit.component.model.tier3.domain.dto.PlatformConfig;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class DomainServiceResponseConverterV1 {

	protected static DomainServiceResponseConverterV1 INSTANCE = new DomainServiceResponseConverterV1();

	public static DomainServiceResponseConverterV1 getInstance() {
		return INSTANCE;
	}

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
						MachineConfig machine = ModelConverter.INSTANCE.toMachineConfig(map);
						if (machine != null) {
							machineConfigs.add(machine);
						}
					}
				}
			}
		}
		return machineConfigs.toArray(new MachineConfig[machineConfigs.size()]);
	}

	public MachineConfig convertToMachineConfig(Responses responses) {
		MachineConfig machineConfig = null;
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) bodyObj;
				machineConfig = ModelConverter.INSTANCE.toMachineConfig(map);
			}
		}
		return machineConfig;
	}

	public PlatformConfig[] convertToTransferAgentConfigs(Responses responses) {
		List<PlatformConfig> taConfigs = new ArrayList<PlatformConfig>();
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof List) {
				List<?> elements = (List<?>) bodyObj;
				for (Object element : elements) {
					if (element instanceof Map<?, ?>) {
						Map<?, ?> map = (Map<?, ?>) element;
						PlatformConfig ta = ModelConverter.INSTANCE.toPlatformConfig(map);
						if (ta != null) {
							taConfigs.add(ta);
						}
					}
				}
			}
		}
		return taConfigs.toArray(new PlatformConfig[taConfigs.size()]);
	}

	public PlatformConfig convertToTransferAgentConfig(Responses responses) {
		PlatformConfig taConfig = null;
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) bodyObj;
				taConfig = ModelConverter.INSTANCE.toPlatformConfig(map);
			}
		}
		return taConfig;
	}

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
						NodeConfig nodeConfig = ModelConverter.INSTANCE.toNodeConfig(map);
						if (nodeConfig != null) {
							nodeConfigs.add(nodeConfig);
						}
					}
				}
			}
		}
		return nodeConfigs.toArray(new NodeConfig[nodeConfigs.size()]);
	}

	public NodeConfig convertToNodeConfig(Responses responses) {
		NodeConfig nodeConfig = null;
		Response response = responses.getResponse(Response.class);
		if (response != null) {
			Object bodyObj = response.getBody();
			if (bodyObj instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) bodyObj;
				nodeConfig = ModelConverter.INSTANCE.toNodeConfig(map);
			}
		}
		return nodeConfig;
	}

}
