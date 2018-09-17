package org.orbit.spirit.api.gaia;

import org.origin.common.rest.model.ServiceMetadata;

public interface GaiaServiceMetadata extends ServiceMetadata {

	String getGaiaId();

	long getNumberOfWorlds();

}
