package org.orbit.component.cli.tier0;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.tier0.channel.ChannelClient;
import org.orbit.component.api.tier0.channel.ChannelConnector;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelCommand implements Annotated {

	protected static Logger LOG = LoggerFactory.getLogger(ChannelCommand.class);
	public static String CHANNEL_HOST_URL = "channel.host.url";
	public static String CHANNEL_CONTEXT_ROOT = "channel.context_root";

	protected Map<Object, Object> properties;

	@Dependency
	protected ChannelConnector connector;

	public ChannelCommand() {
		this.properties = new HashMap<Object, Object>();
	}

	public void start(BundleContext bundleContext) {
		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProps, CHANNEL_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, CHANNEL_CONTEXT_ROOT);
		update(configProps);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function",
				new String[] { //
						"channel_ping", //
						"channel_send" //
		});
		OSGiServiceUtil.register(bundleContext, ChannelCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
	}

	public synchronized void update(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;
	}

	public void stop(BundleContext bundleContext) {
		OSGiServiceUtil.unregister(ChannelCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	protected ChannelClient getChannelClient() throws ClientException {
		if (this.connector == null) {
			LOG.error("ChannelConnector is not available.");
			return null;
		}
		ChannelClient channelClient = this.connector.getChannelClient(this.properties);
		if (channelClient == null) {
			LOG.error("ChannelClient is not available.");
			return null;
		}
		return channelClient;
	}

	@Descriptor("channel_ping")
	public void channel_ping() throws ClientException {
		LOG.info("channel_ping()");
		ChannelClient channelClient = getChannelClient();
		if (channelClient == null) {
			return;
		}

		boolean result = channelClient.ping();
		LOG.info("result = " + result);
	}

	@Descriptor("channel_send")
	public void channel_send( //
			@Descriptor("Channel Id") @Parameter(names = { "-channelId", "--channelId" }, absentValue = Parameter.UNSPECIFIED) String channelId, //
			@Descriptor("Sender Id") @Parameter(names = { "-senderId", "--senderId" }, absentValue = Parameter.UNSPECIFIED) String senderId, //
			@Descriptor("Message") @Parameter(names = { "-message", "--message" }, absentValue = Parameter.UNSPECIFIED) String message //
	) throws ClientException {
		LOG.info("channel_send()");
		ChannelClient channelClient = getChannelClient();
		if (channelClient == null) {
			return;
		}

		boolean result = channelClient.send(channelId, senderId, message);
		LOG.info("result = " + result);
	}

}
