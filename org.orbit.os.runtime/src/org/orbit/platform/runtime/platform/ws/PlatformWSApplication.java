package org.orbit.platform.runtime.platform.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.platform.runtime.platform.Platform;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.rest.server.FeatureConstants;

public class PlatformWSApplication extends AbstractJerseyWSApplication {

	public PlatformWSApplication(final Platform platform, int feature) {
		super(platform.getContextRoot(), feature);
		adapt(Platform.class, platform);

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(platform).to(Platform.class);
			}
		};
		register(serviceBinder);
		register(PlatformWSResource.class);
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
