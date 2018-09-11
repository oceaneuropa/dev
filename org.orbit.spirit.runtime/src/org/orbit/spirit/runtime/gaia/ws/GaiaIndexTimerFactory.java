package org.orbit.spirit.runtime.gaia.ws;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.spirit.runtime.gaia.service.GAIA;

public class GaiaIndexTimerFactory implements ServiceIndexTimerFactory<GAIA> {

	@Override
	public GaiaIndexTimer create(IndexServiceClient indexProvider, GAIA service) {
		return new GaiaIndexTimer(indexProvider, service);
	}

}
