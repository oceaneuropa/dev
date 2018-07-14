package org.orbit.platform.runtime.core.ws;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.runtime.core.Platform;

public class PlatformIndexTimerFactory implements ServiceIndexTimerFactory<Platform> {

	@Override
	public PlatformIndexTimer create(IndexProvider indexProvider, Platform platform) {
		return new PlatformIndexTimer(indexProvider, platform);
	}

}
