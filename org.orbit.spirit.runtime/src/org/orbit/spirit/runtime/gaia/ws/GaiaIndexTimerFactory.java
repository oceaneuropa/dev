package org.orbit.spirit.runtime.gaia.ws;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.spirit.runtime.gaia.service.GaiaService;

public class GaiaIndexTimerFactory implements ServiceIndexTimerFactory<GaiaService> {

	@Override
	public GaiaIndexTimer create(IndexServiceClient indexProvider, GaiaService service) {
		return new GaiaIndexTimer(indexProvider, service);
	}

}
