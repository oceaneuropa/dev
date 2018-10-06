package org.orbit.infra.runtime.datatube.ws;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.runtime.datatube.service.DataTubeService;

public class DataTubeServiceIndexTimerFactory implements ServiceIndexTimerFactory<DataTubeService> {

	@Override
	public DataTubeServiceIndexTimer create(IndexServiceClient indexProvider, DataTubeService service) {
		return new DataTubeServiceIndexTimer(indexProvider, service);
	}

}
