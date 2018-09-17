package org.orbit.spirit.connector.earth;

import org.orbit.spirit.api.earth.EarthServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;

public class EarthServiceMetadataImpl extends ServiceMetadataImpl implements EarthServiceMetadata {

	public static String PROP__GAIA_ID = "gaia_id";
	public static String PROP__EARTH_ID = "earth_id";
	public static String PROP__NUMBER_OF_WORLDS = "number_of_worlds";

	@Override
	public String getGaiaId() {
		String gaiaId = null;
		if (hasProperty(PROP__GAIA_ID)) {
			gaiaId = getProperty(PROP__GAIA_ID, String.class);
		}
		return gaiaId;
	}

	@Override
	public String getEarthId() {
		String earthId = null;
		if (hasProperty(PROP__EARTH_ID)) {
			earthId = getProperty(PROP__EARTH_ID, String.class);
		}
		return earthId;
	}

	@Override
	public long getNumberOfWorlds() {
		long numberOfWorlds = -1;
		if (hasProperty(PROP__NUMBER_OF_WORLDS)) {
			Object value = getProperty(PROP__NUMBER_OF_WORLDS);
			if (value != null) {
				numberOfWorlds = Long.valueOf(value.toString());
			}
		}
		return numberOfWorlds;
	}

}
