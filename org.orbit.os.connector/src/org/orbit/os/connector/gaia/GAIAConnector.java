package org.orbit.os.connector.gaia;

import java.util.Map;

import org.orbit.os.api.gaia.GAIA;
import org.origin.common.rest.client.ServiceConnector;

public class GAIAConnector extends ServiceConnector<GAIA> {

	public GAIAConnector() {
		super(GAIA.class);
	}

	@Override
	protected GAIA create(Map<String, Object> properties) {
		return new GAIAImpl(this, properties);
	}

}
