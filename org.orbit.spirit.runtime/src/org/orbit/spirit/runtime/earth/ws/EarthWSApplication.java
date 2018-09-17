package org.orbit.spirit.runtime.earth.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.spirit.runtime.earth.service.EarthService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.ServerException;

public class EarthWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param earth
	 * @param feature
	 */
	public EarthWSApplication(final EarthService earth, int feature) {
		super(earth, feature);

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(earth).to(EarthService.class);
			}
		};
		register(serviceBinder);
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

		EarthService service = getAdapter(EarthService.class);
		if (metadata instanceof ServiceMetadataImpl && service != null) {
			String gaiaId = service.getGaiaId();
			String earthId = service.getEarthId();
			long numberOfWorlds = -1;
			try {
				numberOfWorlds = service.getWorlds().length;
			} catch (ServerException e) {
				e.printStackTrace();
			}

			((ServiceMetadataImpl) metadata).setProperty("gaia_id", gaiaId);
			((ServiceMetadataImpl) metadata).setProperty("earth_id", earthId);
			((ServiceMetadataImpl) metadata).setProperty("number_of_earth", numberOfWorlds);
		}

		return metadata;
	}

}