package org.orbit.spirit.runtime.gaia.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.spirit.runtime.gaia.service.GaiaService;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.rest.server.FeatureConstants;

public class GaiaWSApplication extends AbstractJerseyWSApplication {

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

}
