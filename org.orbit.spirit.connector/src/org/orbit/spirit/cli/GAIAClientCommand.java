package org.orbit.spirit.cli;

import java.util.Hashtable;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.spirit.api.SpiritConstants;
import org.orbit.spirit.api.gaia.GaiaClient;
import org.orbit.spirit.api.gaia.World;
import org.orbit.spirit.api.util.SpiritClients;
import org.orbit.spirit.connector.util.ModelConverter;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ServiceClient;
import org.origin.common.rest.client.ServiceClientCommand;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.util.ResponseUtil;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PrettyPrinter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GAIAClientCommand extends ServiceClientCommand {

	protected static Logger LOG = LoggerFactory.getLogger(GAIAClientCommand.class);

	protected static String[] WORLD_COLUMN_NAMES = new String[] { "Name" };

	protected String scheme = "gaia";
	protected Map<Object, Object> properties;

	@Override
	public String getScheme() {
		return this.scheme;
	}

	public void start(final BundleContext bundleContext) {
		LOG.info("start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function",
				new String[] { //
						"ping", "echo", "connect", //
						"list_worlds", "get_world", "world_exist", "create_world", "delete_world", "world_status"//
				});

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.ORBIT__GAIA_SERVICE_URL);
		this.properties = properties;

		OSGiServiceUtil.register(bundleContext, GAIAClientCommand.class.getName(), this, props);
	}

	public void stop(final BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(GAIAClientCommand.class.getName(), this);
	}

	@Override
	public ServiceClient getServiceClient() {
		return getGaiaClient();
	}

	protected GaiaClient getGaiaClient() {
		GaiaClient gaia = SpiritClients.getInstance().getGaiaClient(this.properties, true);
		if (gaia == null) {
			throw new IllegalStateException("GAIA is null.");
		}
		return gaia;
	}

	@Descriptor("connect")
	public void connect(//
			@Descriptor("URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "set_url", new String[] { "url", url });
		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'-url' parameter is not set.");
			return;
		}

		this.properties.put(SpiritConstants.ORBIT__GAIA_SERVICE_URL, url);
	}

	// -----------------------------------------------------------------------------------------
	// Missions
	// list_worlds
	// get_world
	// world_exist
	// create_world
	// delete_world
	// world_status
	// -----------------------------------------------------------------------------------------
	@Descriptor("List worlds")
	public void list_worlds() {
		CLIHelper.getInstance().printCommand(getScheme(), "list_worlds");

		Response response = null;
		try {
			GaiaClient gaia = getGaiaClient();

			Request request = new Request(SpiritConstants.LIST_WORLDS);
			response = gaia.sendRequest(request);

			World[] worlds = ModelConverter.GAIA.getWorlds(response);
			String[][] rows = new String[worlds.length][WORLD_COLUMN_NAMES.length];
			int rowIndex = 0;
			for (World world : worlds) {
				String name = world.getName();
				rows[rowIndex++] = new String[] { name };
			}
			PrettyPrinter.prettyPrint(WORLD_COLUMN_NAMES, rows, worlds.length);

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("Get world")
	public void get_world( //
			@Descriptor("Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "get_world", new String[] { "name", name });

		Response response = null;
		try {
			GaiaClient gaia = getGaiaClient();

			Request request = new Request(SpiritConstants.GET_WORLD);
			request.setParameter("name", name);

			response = gaia.sendRequest(request);

			World world = ModelConverter.GAIA.getWorld(response);
			World[] worlds = (world != null) ? new World[] { world } : new World[] {};
			String[][] rows = new String[worlds.length][WORLD_COLUMN_NAMES.length];
			int rowIndex = 0;
			for (World currWorld : worlds) {
				String currName = currWorld.getName();
				rows[rowIndex++] = new String[] { currName };
			}
			PrettyPrinter.prettyPrint(WORLD_COLUMN_NAMES, rows, worlds.length);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("Check whether world exists")
	public void world_exist( //
			@Descriptor("Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "world_exist", new String[] { "name", name });

		Response response = null;
		try {
			GaiaClient gaia = getGaiaClient();

			Request request = new Request(SpiritConstants.WORLD_EXIST);
			request.setParameter("name", name);

			response = gaia.sendRequest(request);
			boolean exists = ModelConverter.GAIA.exists(response);
			LOG.info("exists: " + exists);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("Create a world")
	public void create_world( //
			@Descriptor("Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "create_world", new String[] { "name", name });

		Response response = null;
		try {
			GaiaClient gaia = getGaiaClient();

			Request request = new Request(SpiritConstants.CREATE_WORLD);
			request.setParameter("name", name);

			response = gaia.sendRequest(request);

			boolean succeed = ModelConverter.GAIA.isCreated(response);
			LOG.info("succeed: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("Delete a world")
	public void delete_world( //
			@Descriptor("Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "delete_world", new String[] { "name", name });

		Response response = null;
		try {
			GaiaClient gaia = getGaiaClient();

			Request request = new Request(SpiritConstants.DELETE_WORLD);
			request.setParameter("name", name);

			response = gaia.sendRequest(request);

			boolean succeed = ModelConverter.GAIA.isDeleted(response);
			LOG.info("succeed: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("Get the status of a world")
	public void world_status( //
			@Descriptor("Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "world_status", new String[] { "name", name });

		Response response = null;
		try {
			GaiaClient gaia = getGaiaClient();

			Request request = new Request(SpiritConstants.WORLD_STATUS);
			request.setParameter("name", name);

			response = gaia.sendRequest(request);

			Map<String, String> status = ModelConverter.GAIA.getStatus(response);
			LOG.info("status: " + status);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

}
