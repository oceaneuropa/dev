package org.orbit.spirit.api.gaia;

import java.util.Hashtable;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.spirit.api.Clients;
import org.orbit.spirit.api.Constants;
import org.orbit.spirit.model.gaia.dto.ModelConverter;
import org.orbit.spirit.model.gaia.dto.World;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ServiceClient;
import org.origin.common.rest.client.ServiceClientCommand;
import org.origin.common.rest.model.Request;
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
		PropertyUtil.loadProperty(bundleContext, properties, Constants.ORBIT_GAIA_URL);
		this.properties = properties;

		OSGiServiceUtil.register(bundleContext, GAIAClientCommand.class.getName(), this, props);
	}

	public void stop(final BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(GAIAClientCommand.class.getName(), this);
	}

	@Override
	public ServiceClient getServiceClient() {
		return getGAIA();
	}

	protected GAIAClient getGAIA() {
		GAIAClient gaia = Clients.getInstance().getGAIA(this.properties);
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

		this.properties.put(Constants.ORBIT_GAIA_URL, url);
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

		try {
			GAIAClient gaia = getGAIA();

			Request request = new Request(Constants.LIST_WORLDS);
			Response response = gaia.sendRequest(request);

			World[] worlds = ModelConverter.INSTANCE.getWorlds(response);
			String[][] rows = new String[worlds.length][WORLD_COLUMN_NAMES.length];
			int rowIndex = 0;
			for (World world : worlds) {
				String name = world.getName();
				rows[rowIndex++] = new String[] { name };
			}
			PrettyPrinter.prettyPrint(WORLD_COLUMN_NAMES, rows, worlds.length);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Descriptor("Get world")
	public void get_world( //
			@Descriptor("Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "get_world", new String[] { "name", name });

		try {
			GAIAClient gaia = getGAIA();

			Request request = new Request(Constants.GET_WORLD);
			request.setParameter("name", name);

			Response response = gaia.sendRequest(request);

			World world = ModelConverter.INSTANCE.getWorld(response);
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
		}
	}

	@Descriptor("Check whether world exists")
	public void world_exist( //
			@Descriptor("Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "world_exist", new String[] { "name", name });

		try {
			GAIAClient gaia = getGAIA();

			Request request = new Request(Constants.WORLD_EXIST);
			request.setParameter("name", name);

			Response response = gaia.sendRequest(request);
			boolean exists = ModelConverter.INSTANCE.exists(response);
			LOG.info("exists: " + exists);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Create a world")
	public void create_world( //
			@Descriptor("Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "create_world", new String[] { "name", name });

		try {
			GAIAClient gaia = getGAIA();

			Request request = new Request(Constants.CREATE_WORLD);
			request.setParameter("name", name);

			Response response = gaia.sendRequest(request);

			boolean succeed = ModelConverter.INSTANCE.isCreated(response);
			LOG.info("succeed: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Delete a world")
	public void delete_world( //
			@Descriptor("Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "delete_world", new String[] { "name", name });

		try {
			GAIAClient gaia = getGAIA();

			Request request = new Request(Constants.DELETE_WORLD);
			request.setParameter("name", name);

			Response response = gaia.sendRequest(request);

			boolean succeed = ModelConverter.INSTANCE.isDeleted(response);
			LOG.info("succeed: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Get the status of a world")
	public void world_status( //
			@Descriptor("Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "world_status", new String[] { "name", name });

		try {
			GAIAClient gaia = getGAIA();

			Request request = new Request(Constants.WORLD_STATUS);
			request.setParameter("name", name);

			Response response = gaia.sendRequest(request);

			Map<String, String> status = ModelConverter.INSTANCE.getStatus(response);
			LOG.info("status: " + status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
