package org.orbit.spirit.runtime.earth.ws;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.spirit.runtime.earth.service.EarthService;

public class EarthIndexTimerFactory implements ServiceIndexTimerFactory<EarthService> {

	@Override
	public EarthIndexTimer create(IndexServiceClient indexProvider, EarthService service) {
		return new EarthIndexTimer(indexProvider, service);
	}

}
