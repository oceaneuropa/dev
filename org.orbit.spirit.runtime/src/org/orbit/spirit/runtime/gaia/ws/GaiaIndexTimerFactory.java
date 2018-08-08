package org.orbit.spirit.runtime.gaia.ws;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.spirit.runtime.gaia.service.GAIA;

public class GaiaIndexTimerFactory implements ServiceIndexTimerFactory<GAIA> {

	@Override
	public GaiaIndexTimer create(IndexProvider indexProvider, GAIA service) {
		return new GaiaIndexTimer(indexProvider, service);
	}

}
