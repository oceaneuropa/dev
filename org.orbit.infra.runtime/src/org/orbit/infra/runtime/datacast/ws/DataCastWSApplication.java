package org.orbit.infra.runtime.datacast.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;
import org.origin.common.rest.server.AbstractJerseyWSApplication;

public class DataCastWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public DataCastWSApplication(final DataCastService service, int feature) {
		super(service, feature);
		adapt(DataCastService.class, service);

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(DataCastService.class);
			}
		};
		register(serviceBinder);
		register(DataCastWSResource.class);
	}

	@Override
	public ServiceMetadata getMetadata() {
		ServiceMetadata metadata = super.getMetadata();
		DataCastService service = getAdapter(DataCastService.class);

		if (metadata instanceof ServiceMetadataImpl && service != null) {
			String dataCastId = service.getDataCastId();
			((ServiceMetadataImpl) metadata).setProperty("data_cast_id", dataCastId);
		}

		return metadata;
	}

}
