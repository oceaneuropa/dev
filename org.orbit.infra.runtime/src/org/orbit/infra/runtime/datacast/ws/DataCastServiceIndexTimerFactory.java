package org.orbit.infra.runtime.datacast.ws;

import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.runtime.datacast.service.DataCastService;

public class DataCastServiceIndexTimerFactory implements ServiceIndexTimerFactory<DataCastService> {

	@Override
	public DataCastServiceIndexTimer create(DataCastService service) {
		return new DataCastServiceIndexTimer(service);
	}

}
