package org.orbit.infra.io.util;

import java.io.IOException;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;

public class DataCastNodeConfigHelper {

	protected static final String CONFIG_REGISTRY_TYPE__NODE_CONFIG_LIST = "NodeConfigList";

	protected static final String CONFIG_REGISTRY_NAME__DATA_CAST_NODES = "DataCastNodes";

	public static DataCastNodeConfigHelper INSTANCE = new DataCastNodeConfigHelper();

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
		IConfigElement result = null;
		if (cfgReg != null && dataCastId != null) {
			IConfigElement[] rootElements = cfgReg.listRootConfigElements();
			if (rootElements != null) {
				for (IConfigElement rootElement : rootElements) {
					String currDataCastId = rootElement.getAttribute(InfraConstants.IDX_PROP__DATACAST__ID, String.class);
					if (dataCastId.equals(currDataCastId)) {
						result = rootElement;
						break;
					}
				}
			}
		}
		return result;
	}

}
