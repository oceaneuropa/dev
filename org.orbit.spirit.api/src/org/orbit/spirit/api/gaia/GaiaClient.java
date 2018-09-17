package org.orbit.spirit.api.gaia;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface GaiaClient extends ServiceClient {

	WorldMetadata[] getWorlds() throws ClientException;

	boolean worldExists(String worldId) throws ClientException;

	WorldMetadata getWorld(String worldId) throws ClientException;

	WorldMetadata createWorld(String worldId) throws ClientException;

	boolean deleteWorld(String worldId) throws ClientException;

}
