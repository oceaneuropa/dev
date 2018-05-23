package org.orbit.component.cli;

import java.util.Hashtable;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.Requests;
import org.orbit.component.api.tier4.mission.MissionControlClient;
import org.orbit.component.connector.tier4.mission.MissionControlModelConverter;
import org.orbit.component.model.tier4.mission.MissionRTO;
import org.orbit.platform.sdk.command.ICommandActivator;
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

public class MissionControlCommand extends ServiceClientCommand implements ICommandActivator {

	public static final String ID = "org.orbit.component.cli.MissionControlCommand";

	protected static Logger LOG = LoggerFactory.getLogger(NodeControlCommand.class);

	protected static String[] MISSION_COLUMN_NAMES = new String[] { "Name" };

	protected String scheme = "mission";
	protected Map<Object, Object> properties;

	@Override
	public String getScheme() {
		return this.scheme;
	}

	public void start(final BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function",
				new String[] { //
						"ping", //
						"echo", "level", //
						"list_missions", "get_mission", "mission_exist", "create_mission", "delete_mission", "start_mission", "stop_mission", "mission_status"//
		});

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_MISSION_CONTROL_URL);
		this.properties = properties;

		OSGiServiceUtil.register(bundleContext, MissionControlCommand.class.getName(), this, props);
	}

	public void stop(final BundleContext bundleContext) {
		OSGiServiceUtil.unregister(MissionControlCommand.class.getName(), this);
	}

	@Override
	public ServiceClient getServiceClient() {
		return getMissionControl();
	}

	protected MissionControlClient getMissionControl() {
		MissionControlClient missionControl = OrbitClients.getInstance().getMissionControl(this.properties);
		if (missionControl == null) {
			throw new IllegalStateException("MissionControl is null.");
		}
		return missionControl;
	}

	// -----------------------------------------------------------------------------------------
	// Missions
	// list_missions
	// get_mission
	// mission_exist
	// create_mission
	// delete_mission
	// mission_status
	// -----------------------------------------------------------------------------------------
	@Descriptor("List missions")
	public void list_missions( //
			@Descriptor("Type Id") @Parameter(names = { "-typeId", "--typeId" }, absentValue = Parameter.UNSPECIFIED) String typeId) {
		CLIHelper.getInstance().printCommand(getScheme(), "list_missions", new String[] { "typeId", typeId });
		if (Parameter.UNSPECIFIED.equals(typeId)) {
			LOG.error("'-typeId' parameter is not set.");
			return;
		}

		try {
			MissionControlClient missionControl = getMissionControl();

			Request request = new Request(Requests.GET_MISSIONS);
			Response response = missionControl.sendRequest(request);

			MissionRTO[] missions = MissionControlModelConverter.INSTANCE.getMissions(response);
			String[][] rows = new String[missions.length][MISSION_COLUMN_NAMES.length];
			int rowIndex = 0;
			for (MissionRTO mission : missions) {
				String name = mission.getName();
				rows[rowIndex++] = new String[] { name };
			}
			PrettyPrinter.prettyPrint(MISSION_COLUMN_NAMES, rows, missions.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

// PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_TRANSFER_AGENT_URL);

// protected DomainService getDomainService() {
// DomainService domainService = OrbitClients.getInstance().getDomainService(this.properties);
// if (domainService == null) {
// throw new IllegalStateException("DomainService is null.");
// }
// return domainService;
// }

// protected TransferAgent getTransferAgent() {
// TransferAgent transferAgent = OrbitClients.getInstance().getTransferAgent(this.properties);
// if (transferAgent == null) {
// throw new IllegalStateException("TransferAgent is null.");
// }
// return transferAgent;
// }
