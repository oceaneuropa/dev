package org.orbit.infra.runtime.datatube.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;
import org.origin.common.rest.server.AbstractJerseyWSApplication;

public class DataTubeWSApplication extends AbstractJerseyWSApplication {

	public static String PROP__DATA_CAST_ID = "data_cast_id";
	public static String PROP__DATA_TUBE_ID = "data_tube_id";
	public static String PROP__WEB_SOCKET_PORT = "web_socket_port";

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
			String dataCastId = service.getDataCastId();
			String dataTubeId = service.getDataTubeId();
			String webSocketPort = service.getWebSocketPort();

			((ServiceMetadataImpl) metadata).setProperty(PROP__DATA_CAST_ID, dataCastId);
			((ServiceMetadataImpl) metadata).setProperty(PROP__DATA_TUBE_ID, dataTubeId);
			((ServiceMetadataImpl) metadata).setProperty(PROP__WEB_SOCKET_PORT, webSocketPort);
		}

		return metadata;
	}

}
