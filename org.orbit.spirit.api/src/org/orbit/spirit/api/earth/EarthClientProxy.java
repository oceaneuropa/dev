package org.orbit.spirit.api.earth;

import java.util.Map;

import org.origin.common.rest.client.ServiceClientProxy;

public class EarthClientProxy extends ServiceClientProxy<EarthClient> implements EarthClient {

	/**
	 * 
	 * @param properties
	 */
	public EarthClientProxy(Map<String, Object> properties) {
		super(properties);
	}

	@Override
	protected EarthClient resolveClient(Map<String, Object> properties) {
		return null;
	}

	@Override
	protected EarthClient createClientDummy() {
		return new EarthClientDummyImpl();
	}

	public static class EarthClientDummyImpl extends ServiceClientDummyImpl implements EarthClient {

	}

}
