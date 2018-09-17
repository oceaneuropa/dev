package org.orbit.spirit.api.gaia;

import java.util.Map;

import org.origin.common.rest.client.ServiceClientProxy;

public class GaiaClientProxy extends ServiceClientProxy<GaiaClient> implements GaiaClient {

	/**
	 * 
	 * @param properties
	 */
	public GaiaClientProxy(Map<String, Object> properties) {
		super(properties);
	}

	@Override
	protected GaiaClient resolveClient(Map<String, Object> properties) {
		return null;
	}

	@Override
	protected GaiaClient createClientDummy() {
		return new GaiaClientDummyImpl();
	}

	public static class GaiaClientDummyImpl extends ServiceClientDummyImpl implements GaiaClient {

	}

}
