package org.orbit.infra.io.util;

import java.io.IOException;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;

public class NodeConfigHelper {

	protected static final String CONFIG_REGISTRY_TYPE__NODE_CONFIG_LIST = "NodeConfigList";

	protected static final String CONFIG_REGISTRY_NAME__DATA_CAST_NODES = "DataCastNodes";

	public static NodeConfigHelper INSTANCE = new NodeConfigHelper();

	public String getNodeConfigListType() {
		return CONFIG_REGISTRY_TYPE__NODE_CONFIG_LIST;
	}

	public String getConfigRegistryName__DataCastNodes() {
		return CONFIG_REGISTRY_NAME__DATA_CAST_NODES;
	}

	/**
	 * 
	 * @param accessToken
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public IConfigRegistry getDataCastNodesConfigRegistry(String accessToken, boolean createIfNotExist) throws IOException {
		IConfigRegistry cfgReg = null;
		CFG cfg = CFG.getDefault(accessToken);
		if (cfg != null) {
			cfgReg = cfg.getConfigRegistryByName(getConfigRegistryName__DataCastNodes());
			if (cfgReg == null) {
				if (createIfNotExist) {
					cfgReg = cfg.createConfigRegistry(getNodeConfigListType(), getConfigRegistryName__DataCastNodes(), null, false);
				}
			}
		}
		return cfgReg;
	}

	/**
	 * 
	 * @param cfgReg
	 * @param dataCastId
	 * @return
	 * @throws IOException
	 */
	public IConfigElement getDataCastConfigElement(IConfigRegistry cfgReg, String dataCastId) throws IOException {
		IConfigElement dataCastConfigElement = null;
		if (cfgReg != null && dataCastId != null) {
			IConfigElement[] rootElements = cfgReg.listRootConfigElements();
			if (rootElements != null) {
				for (IConfigElement rootElement : rootElements) {
					String currDataCastId = rootElement.getAttribute(InfraConstants.IDX_PROP__DATACAST__ID, String.class);
					if (dataCastId.equals(currDataCastId)) {
						dataCastConfigElement = rootElement;
						break;
					}
				}
			}
		}
		return dataCastConfigElement;
	}

	/**
	 * 
	 * @param cfgReg
	 * @param dataCastId
	 * @param dataTubeId
	 * @return
	 * @throws IOException
	 */
	public IConfigElement getDataTubeConfigElement(IConfigRegistry cfgReg, String dataCastId, String dataTubeId) throws IOException {
		IConfigElement dataTubeConfigElement = null;
		if (cfgReg != null && dataCastId != null && dataTubeId != null) {
			IConfigElement dataCastConfigElement = getDataCastConfigElement(cfgReg, dataCastId);
			if (dataCastConfigElement != null) {
				IConfigElement[] configElements = dataCastConfigElement.memberConfigElements();
				if (configElements != null) {
					for (IConfigElement configElement : configElements) {
						String currDataTubeId = configElement.getAttribute(InfraConstants.IDX_PROP__DATATUBE__ID, String.class);
						if (dataTubeId.equals(currDataTubeId)) {
							dataTubeConfigElement = configElement;
							break;
						}
					}
				}
			}
		}
		return dataTubeConfigElement;
	}

}