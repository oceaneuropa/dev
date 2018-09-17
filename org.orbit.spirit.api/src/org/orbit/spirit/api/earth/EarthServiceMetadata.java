package org.orbit.spirit.api.earth;

import org.origin.common.rest.model.ServiceMetadata;

public interface EarthServiceMetadata extends ServiceMetadata {

	String getGaiaId();

	String getEarthId();

	long getNumberOfWorlds();

}
