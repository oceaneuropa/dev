package org.orbit.infra.webconsole.util;

import java.io.IOException;

import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigRegistry;

public class DataCastHelper {

	protected static final String DATA_CAST_NODES__CONFIG_REGISTRY_TYPE = "NodeConfigList";
	protected static final String DATA_CAST_NODES__CONFIG_REGISTRY_NAME = "DataCastNodes";

	public static DataCastHelper INSTANCE = new DataCastHelper();

	public String getConfigRegistryName__DataCastNodes() {
		return DATA_CAST_NODES__CONFIG_REGISTRY_NAME;
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
			cfgReg = cfg.getConfigRegistryByName(DATA_CAST_NODES__CONFIG_REGISTRY_NAME);
			if (cfgReg == null) {
				if (createIfNotExist) {
					cfgReg = cfg.createConfigRegistry(DATA_CAST_NODES__CONFIG_REGISTRY_TYPE, DATA_CAST_NODES__CONFIG_REGISTRY_NAME, null, false);
				}
			}
		}
		return cfgReg;
	}

}
