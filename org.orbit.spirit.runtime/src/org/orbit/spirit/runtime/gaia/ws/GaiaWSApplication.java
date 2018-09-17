package org.orbit.spirit.runtime.gaia.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.spirit.runtime.gaia.service.GaiaService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.ServerException;

public class GaiaWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param gaia
	 * @param feature
	 */
	public GaiaWSApplication(final GaiaService gaia, int feature) {
		super(gaia, feature);
		// adapt(GAIA.class, gaia);

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(gaia).to(GaiaService.class);
			}
		};
		register(serviceBinder);
		register(GaiaWSResource.class);
		register(WorldsWSResource.class);
	}

	@Override
	protected int checkFeature(int feature) {
		feature = super.checkFeature(feature);

		if ((feature & FeatureConstants.JACKSON) == 0) {
			feature = feature | FeatureConstants.JACKSON;
		}
		if ((feature & FeatureConstants.MULTIPLEPART) == 0) {
			feature = feature | FeatureConstants.MULTIPLEPART;
		}
		return feature;
	}

	@Override
	public ServiceMetadata getMetadata() {
		ServiceMetadata metadata = super.getMetadata();

		GaiaService service = getAdapter(GaiaService.class);
		if (metadata instanceof ServiceMetadataImpl && service != null) {
			String gaiaId = service.getGaiaId();
			long numberOfWorlds = -1;
			try {
				numberOfWorlds = service.getWorlds().size();
			} catch (ServerException e) {
				e.printStackTrace();
			}

			((ServiceMetadataImpl) metadata).setProperty("gaia_id", gaiaId);
			((ServiceMetadataImpl) metadata).setProperty("number_of_earth", numberOfWorlds);
		}

		return metadata;
	}

}
