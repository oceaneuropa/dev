package org.orbit.infra.connector.cli;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.channel.Channels;
import org.orbit.infra.connector.InfraConstants;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.annotation.Annotated;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelCommand implements Annotated, CommandActivator {

	public static final String ID = "org.orbit.infra.connector.ChannelCommand";

	protected static Logger LOG = LoggerFactory.getLogger(ChannelCommand.class);

	protected Map<Object, Object> properties;

	public ChannelCommand() {
		this.properties = new HashMap<Object, Object>();
	}

	protected String getScheme() {
		return "orbit";
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(BundleContext bundleContext) {
		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.CHANNEL_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.CHANNEL_CONTEXT_ROOT);
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

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void stop(BundleContext bundleContext) {
		OSGiServiceUtil.unregister(ChannelCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	/**
	 * 
	 * @param properties
	 */
	public synchronized void update(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;
	}

	protected Channels getChannels(String url) throws ClientException {
		Channels channels = InfraClients.getInstance().getChannels(url);
		if (channels == null) {
			LOG.error("Channels is not available.");
			throw new IllegalStateException("Channels is not available. url = " + url);
		}
		return channels;
	}

	@Descriptor("channel_ping")
	public void channel_ping( //
			@Descriptor("URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "channel_ping", new String[] { "url", url });

		Channels channel = getChannels(url);

		boolean result = channel.ping();
		LOG.info("result = " + result);
	}

	@Descriptor("channel_send")
	public void channel_send( //
			@Descriptor("URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Channel Id") @Parameter(names = { "-channelId", "--channelId" }, absentValue = Parameter.UNSPECIFIED) String channelId, //
			@Descriptor("Sender Id") @Parameter(names = { "-senderId", "--senderId" }, absentValue = Parameter.UNSPECIFIED) String senderId, //
			@Descriptor("Message") @Parameter(names = { "-message", "--message" }, absentValue = Parameter.UNSPECIFIED) String message //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "channel_send" //
				, new String[] { "url", url } //
				, new String[] { "channelId", channelId } //
				, new String[] { "senderId", senderId } //
				, new String[] { "message", message } //
		);

		Channels channel = getChannels(url);

		boolean result = channel.send(channelId, senderId, message);
		LOG.info("result = " + result);
	}

}