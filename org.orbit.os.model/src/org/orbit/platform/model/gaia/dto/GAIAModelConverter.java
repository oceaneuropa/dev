package org.orbit.platform.model.gaia.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class GAIAModelConverter {

	public static GAIAModelConverter INSTANCE = new GAIAModelConverter();

	protected World toWorld(WorldDTO worldDTO) {
		WorldImpl world = new WorldImpl();
		world.setName(worldDTO.getName());
		return world;
	}

	public World[] getWorlds(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}
		List<World> worlds = new ArrayList<World>();
		List<WorldDTO> worldDTOs = response.readEntity(new GenericType<List<WorldDTO>>() {
		});
		for (WorldDTO worldDTO : worldDTOs) {
			World world = toWorld(worldDTO);
			worlds.add(world);
		}
		return worlds.toArray(new World[worlds.size()]);
	}

	public World getWorld(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}
		World world = null;
		WorldDTO worldDTO = response.readEntity(WorldDTO.class);
		if (worldDTO != null) {
			world = toWorld(worldDTO);
		}
		return world;
	}

	public boolean exists(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}
		boolean exists = false;
		try {
			exists = ResponseUtil.getSimpleValue(response, "exists", Boolean.class);
		} catch (Exception e) {
			throw new ClientException(500, e.getMessage(), e);
		}
		return exists;
	}

	public boolean isCreated(Response response) throws ClientException {
		return isSucceed(response);
	}

	public boolean isDeleted(Response response) throws ClientException {
		return isSucceed(response);
	}

	public boolean isSucceed(Response response) throws ClientException {
		if (!ResponseUtil.isSuccessful(response)) {
			throw new ClientException(response);
		}
		boolean succeed = false;
		try {
			succeed = ResponseUtil.getSimpleValue(response, "succeed", Boolean.class);

		} catch (Exception e) {
			throw new ClientException(500, e.getMessage(), e);
		}
		return succeed;
	}

	public Map<String, String> getStatus(Response response) {
		return null;
	}

}
