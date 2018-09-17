package org.orbit.spirit.api.gaia;

import java.util.Map;

import org.origin.common.rest.client.ClientException;
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

	@Override
	public WorldMetadata[] getWorlds() throws ClientException {
		return resolve().getWorlds();
	}

	@Override
	public boolean worldExists(String name) throws ClientException {
		return resolve().worldExists(name);
	}

	@Override
	public WorldMetadata getWorld(String name) throws ClientException {
		return resolve().getWorld(name);
	}

	@Override
	public WorldMetadata createWorld(String name) throws ClientException {
		return resolve().createWorld(name);
	}

	@Override
	public boolean deleteWorld(String name) throws ClientException {
		return resolve().deleteWorld(name);
	}

	public static class GaiaClientDummyImpl extends ServiceClientDummyImpl implements GaiaClient {

		@Override
		public WorldMetadata[] getWorlds() throws ClientException {
			return null;
		}

		@Override
		public boolean worldExists(String name) throws ClientException {
			return false;
		}

		@Override
		public WorldMetadata getWorld(String name) throws ClientException {
			return null;
		}

		@Override
		public WorldMetadata createWorld(String name) throws ClientException {
			return null;
		}

		@Override
		public boolean deleteWorld(String name) throws ClientException {
			return false;
		}
	}

}
