package org.orbit.infra.runtime.datatube.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;
import org.origin.common.rest.server.AbstractJerseyWSApplication;

public class DataTubeWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public DataTubeWSApplication(final DataTubeService service, int feature) {
		super(service, feature);
		adapt(DataTubeService.class, service);

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(DataTubeService.class);
			}
		};
		register(serviceBinder);
		register(DataTubeWSResource.class);
	}

	@Override
	public ServiceMetadata getMetadata() {
		ServiceMetadata metadata = super.getMetadata();

		DataTubeService service = getAdapter(DataTubeService.class);
		if (metadata instanceof ServiceMetadataImpl && service != null) {
			String webSocketHttpPort = service.getWebSocketHttpPort();
			((ServiceMetadataImpl) metadata).setProperty("web_socket__http_port", webSocketHttpPort);
		}

		return metadata;
	}

}
