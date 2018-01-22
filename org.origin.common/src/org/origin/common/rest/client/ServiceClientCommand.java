package org.origin.common.rest.client;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.origin.common.util.CLIHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServiceClientCommand {

	protected static Logger LOG = LoggerFactory.getLogger(ServiceClientCommand.class);

	public abstract String getScheme();

	public abstract ServiceClient getServiceClient();

	@Descriptor("ping")
	public void ping() {
		CLIHelper.getInstance().printCommand(getScheme(), "ping");
		try {
			ServiceClient serviceClient = getServiceClient();

			boolean ping = serviceClient.ping();
			System.out.println("ping result = " + ping);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("echo")
	public void echo( //
			@Descriptor("Message") @Parameter(names = { "-message", "--message" }, absentValue = Parameter.UNSPECIFIED) String message //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "ping", new String[] { "message", message });
		if (Parameter.UNSPECIFIED.equals(message)) {
			LOG.error("'-message' parameter is not set.");
		}

		try {
			ServiceClient serviceClient = getServiceClient();

			String echoMessage = serviceClient.echo(message);
			System.out.println("echo message = " + echoMessage);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
