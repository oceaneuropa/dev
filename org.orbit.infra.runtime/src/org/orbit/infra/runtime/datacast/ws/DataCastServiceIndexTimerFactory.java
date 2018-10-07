package org.orbit.infra.runtime.datacast.ws;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.runtime.datacast.service.DataCastService;

public class DataCastServiceIndexTimerFactory implements ServiceIndexTimerFactory<DataCastService> {

	@Override
	public DataCastServiceIndexTimer create(IndexServiceClient indexService, DataCastService service) {
		return new DataCastServiceIndexTimer(indexService, service);
	}

}
