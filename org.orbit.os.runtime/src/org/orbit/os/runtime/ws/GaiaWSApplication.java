package org.orbit.os.runtime.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.os.runtime.gaia.GAIA;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.rest.server.FeatureConstants;

public class GaiaWSApplication extends AbstractJerseyWSApplication {

	public GaiaWSApplication(final GAIA gaia, int feature) {
		super(gaia.getContextRoot(), feature);
		adapt(GAIA.class, gaia);

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(gaia).to(GAIA.class);
			}
		};
		register(serviceBinder);
		register(GaiaWSResource.class);
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
