package org.orbit.infra.runtime.datatube.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.orbit.infra.api.datacast.ChannelMetadata;
import org.orbit.infra.api.datacast.DataCastClientResolver;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.infra.io.util.DefaultDataCastClientResolver;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.datatube.service.RuntimeChannel;
import org.orbit.infra.runtime.util.DataTubeConfigPropertiesHandler;
import org.orbit.platform.sdk.http.JWTTokenHandler;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.orbit.platform.sdk.util.ExtensionUtil;
import org.origin.common.event.PropertyChangeEvent;
import org.origin.common.event.PropertyChangeListener;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DataTubeServiceImpl implements LifecycleAware, DataTubeService, PropertyChangeListener {

	protected Map<Object, Object> initProperties;
	protected Map<String, RuntimeChannel> runtimeChannelMap;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies wsEditPolicies;
	// protected Map<Object, Object> properties;

	/**
	 * 
	 * @param initProperties
	 */
	public DataTubeServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = (initProperties != null) ? initProperties : new HashMap<Object, Object>();
		this.wsEditPolicies = new ServiceEditPoliciesImpl(DataTubeService.class, this);
		this.runtimeChannelMap = Collections.synchronizedMap(new HashMap<String, RuntimeChannel>());
		// this.properties = new HashMap<Object, Object>();
	}

	@Override
	public Map<Object, Object> getInitProperties() {
		return this.initProperties;
	}

	protected String getAccessToken() {
		String tokenValue = null;
		try {
			JWTTokenHandler tokenHandler = ExtensionUtil.JWT.getTokenHandler(InfraConstants.TOKEN_PROVIDER__ORBIT);
			if (tokenHandler != null) {
				String roles = OrbitRoles.DATATUBE_ADMIN;
				int securityLevel = Secured.SecurityLevels.LEVEL_1;
				String classificationLevels = Secured.ClassificationLevels.TOP_SECRET + "," + Secured.ClassificationLevels.SECRET + "," + Secured.ClassificationLevels.CONFIDENTIAL;

				Map<String, String> payload = new LinkedHashMap<String, String>();
				payload.put(JWTTokenHandler.PAYLOAD__ROLES, roles);
				payload.put(JWTTokenHandler.PAYLOAD__SECURITY_LEVEL, String.valueOf(securityLevel));
				payload.put(JWTTokenHandler.PAYLOAD__CLASSIFICATION_LEVELS, classificationLevels);

				tokenValue = tokenHandler.createToken(payload);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tokenValue;
	}

	/** LifecycleAware */
	@Override
	public void start(BundleContext bundleContext) {
		DataTubeConfigPropertiesHandler.getInstance().addPropertyChangeListener(this);

		// Map<Object, Object> configProps = new Hashtable<Object, Object>();
		// if (this.initProperties != null) {
		// configProps.putAll(this.initProperties);
		// }
		//
		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.ORBIT_HOST_URL);
		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__DATACAST_ID);
		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__ID);
		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__NAME);
		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__HOST_URL);
		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__CONTEXT_ROOT);
		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.DATATUBE__HTTP_PORT);
		//
		// updateProperties(configProps);

		// Register service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(DataTubeService.class, this, props);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		DataTubeConfigPropertiesHandler.getInstance().removePropertyChangeListener(this);
	}

	// @Override
	// public Map<Object, Object> getProperties() {
	// return this.properties;
	// }
	// /**
	// *
	// * @param configProps
	// */
	// public synchronized void updateProperties(Map<Object, Object> configProps) {
	// if (configProps == null) {
	// configProps = new HashMap<Object, Object>();
	// }
	//
	// String globalHostURL = (String) configProps.get(InfraConstants.ORBIT_HOST_URL);
	// String dataCastId = (String) configProps.get(InfraConstants.DATATUBE__DATACAST_ID);
	// String dataTubeId = (String) configProps.get(InfraConstants.DATATUBE__ID);
	// String name = (String) configProps.get(InfraConstants.DATATUBE__NAME);
	// String hostURL = (String) configProps.get(InfraConstants.DATATUBE__HOST_URL);
	// String contextRoot = (String) configProps.get(InfraConstants.DATATUBE__CONTEXT_ROOT);
	// String webSocketHttpPort = (String) configProps.get(InfraConstants.DATATUBE__HTTP_PORT);
	//
	// boolean printProps = false;
	// if (printProps) {
	// System.out.println();
	// System.out.println("Config properties:");
	// System.out.println("-----------------------------------------------------");
	// System.out.println(InfraConstants.ORBIT_HOST_URL + " = " + globalHostURL);
	// System.out.println(InfraConstants.DATATUBE__DATACAST_ID + " = " + dataCastId);
	// System.out.println(InfraConstants.DATATUBE__ID + " = " + dataTubeId);
	// System.out.println(InfraConstants.DATATUBE__NAME + " = " + name);
	// System.out.println(InfraConstants.DATATUBE__HOST_URL + " = " + hostURL);
	// System.out.println(InfraConstants.DATATUBE__CONTEXT_ROOT + " = " + contextRoot);
	// System.out.println(InfraConstants.DATATUBE__HTTP_PORT + " = " + webSocketHttpPort);
	//
	// System.out.println("-----------------------------------------------------");
	// System.out.println();
	// }
	//
	// this.properties = configProps;
	// }

	/** PropertyChangeListener */
	@Override
	public void notifyEvent(PropertyChangeEvent event) {
		String eventName = event.getName();
		if (InfraConstants.DATATUBE__DATACAST_ID.equals(eventName) //
				|| InfraConstants.DATATUBE__ID.equals(eventName) //
				|| InfraConstants.DATATUBE__NAME.equals(eventName) //
				|| InfraConstants.DATATUBE__HOST_URL.equals(eventName) //
				|| InfraConstants.DATATUBE__CONTEXT_ROOT.equals(eventName) //
				|| InfraConstants.DATATUBE__HTTP_PORT.equals(eventName)) {
		}
	}

	/** WebServiceAware */
	@Override
	public String getName() {
		// return (String) this.properties.get(InfraConstants.DATATUBE__NAME);
		return DataTubeConfigPropertiesHandler.getInstance().getProperty(InfraConstants.DATATUBE__NAME, this.initProperties);
	}

	@Override
	public String getHostURL() {
		// String hostURL = (String) this.properties.get(InfraConstants.DATATUBE__HOST_URL);
		// if (hostURL != null) {
		// return hostURL;
		// }
		// String globalHostURL = (String) this.properties.get(InfraConstants.ORBIT_HOST_URL);
		// if (globalHostURL != null) {
		// return globalHostURL;
		// }
		String hostURL = DataTubeConfigPropertiesHandler.getInstance().getProperty(InfraConstants.DATATUBE__HOST_URL, this.initProperties);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = DataTubeConfigPropertiesHandler.getInstance().getProperty(InfraConstants.ORBIT_HOST_URL, this.initProperties);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		// return (String) this.properties.get(InfraConstants.DATATUBE__CONTEXT_ROOT);
		return DataTubeConfigPropertiesHandler.getInstance().getProperty(InfraConstants.DATATUBE__CONTEXT_ROOT, this.initProperties);
	}

	protected String getIndexServiceURL() {
		return DataTubeConfigPropertiesHandler.getInstance().getProperty(InfraConstants.ORBIT_INDEX_SERVICE_URL, this.initProperties);
	}

	/** EditPoliciesAwareService */
	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	/** DataTubeService */
	@Override
	public String getDataCastId() {
		// return (String) this.properties.get(InfraConstants.DATATUBE__DATACAST_ID);
		return DataTubeConfigPropertiesHandler.getInstance().getProperty(InfraConstants.DATATUBE__DATACAST_ID, this.initProperties);
	}

	@Override
	public String getDataTubeId() {
		// return (String) this.properties.get(InfraConstants.DATATUBE__ID);
		return DataTubeConfigPropertiesHandler.getInstance().getProperty(InfraConstants.DATATUBE__ID, this.initProperties);
	}

	@Override
	public String getWebSocketHttpPort() {
		// return (String) this.properties.get(InfraConstants.DATATUBE__HTTP_PORT);
		return DataTubeConfigPropertiesHandler.getInstance().getProperty(InfraConstants.DATATUBE__HTTP_PORT, this.initProperties);
	}

	protected void checkChannelId(String channelId) {
		if (channelId == null || channelId.isEmpty()) {
			throw new IllegalArgumentException("channelId is empty.");
		}
	}

	protected void checkChannelName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("name is empty.");
		}
	}

	/**
	 * 
	 * @param channelId
	 * @return
	 * @throws ClientException
	 */
	protected ChannelMetadata findChannelMetadataById(String channelId) throws ClientException {
		String accessToken = getAccessToken();
		String indexServiceUrl = getIndexServiceURL();

		DataCastClientResolver clientResolver = new DefaultDataCastClientResolver(indexServiceUrl);
		ChannelMetadata channelMetadata = InfraClientsHelper.DATA_CAST.getChannelMetadataByChannelId(clientResolver, indexServiceUrl, accessToken, channelId);
		return channelMetadata;
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	protected ChannelMetadata findChannelMetadataByName(String name) throws ClientException {
		String accessToken = getAccessToken();
		String indexServiceUrl = getIndexServiceURL();
		DataCastClientResolver clientResolver = new DefaultDataCastClientResolver(indexServiceUrl);

		ChannelMetadata channelMetadata = InfraClientsHelper.DATA_CAST.getChannelMetadataByChannelName(clientResolver, indexServiceUrl, accessToken, name);
		return channelMetadata;
	}

	/**
	 * 
	 * @return
	 */
	protected synchronized RuntimeChannel[] findRuntimeChannels() {
		return this.runtimeChannelMap.values().toArray(new RuntimeChannel[this.runtimeChannelMap.size()]);
	}

	/**
	 * 
	 * @param channelId
	 * @return
	 */
	protected synchronized RuntimeChannel findRuntimeChannelById(String channelId) {
		RuntimeChannel channel = this.runtimeChannelMap.get(channelId);
		return channel;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected synchronized RuntimeChannel findRuntimeChannelByName(String name) {
		RuntimeChannel channel = null;
		for (Iterator<String> itor = this.runtimeChannelMap.keySet().iterator(); itor.hasNext();) {
			String channelId = itor.next();
			RuntimeChannel currChannel = this.runtimeChannelMap.get(channelId);

			String currName = currChannel.getChannelMetadata().getName();
			if (name.equals(currName)) {
				channel = currChannel;
				break;
			}
		}
		return channel;
	}

	/**
	 * 
	 * @param channel
	 * @return
	 */
	protected synchronized boolean checkInRuntimeChannel(RuntimeChannel channel) {
		if (channel != null) {
			String channelId = channel.getChannelMetadata().getChannelId();
			this.runtimeChannelMap.put(channelId, channel);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param channelId
	 * @return
	 */
	protected synchronized RuntimeChannel checkOutRuntimeChannelById(String channelId) {
		RuntimeChannel channel = this.runtimeChannelMap.remove(channelId);
		return channel;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected synchronized RuntimeChannel checkOutRuntimeChannelByName(String name) {
		RuntimeChannel channel = null;
		for (Iterator<String> itor = this.runtimeChannelMap.keySet().iterator(); itor.hasNext();) {
			String channelId = itor.next();
			RuntimeChannel currChannel = this.runtimeChannelMap.get(channelId);

			String currName = currChannel.getChannelMetadata().getName();
			if (name.equals(currName)) {
				channel = currChannel;
				break;
			}
		}
		if (channel != null) {
			String channelId = channel.getChannelMetadata().getChannelId();
			this.runtimeChannelMap.remove(channelId);
		}
		return channel;
	}

	@Override
	public synchronized RuntimeChannel[] getRuntimeChannels() throws ServerException {
		return findRuntimeChannels();
	}

	@Override
	public synchronized boolean runtimeChannelExistsById(String channelId) throws ServerException {
		checkChannelId(channelId);

		boolean exists = false;
		RuntimeChannel channel = findRuntimeChannelById(channelId);
		if (channel != null) {
			exists = true;
		}
		return exists;
	}

	@Override
	public synchronized boolean runtimeChannelExistsByName(String name) throws ServerException {
		checkChannelName(name);

		boolean exists = false;
		RuntimeChannel channel = findRuntimeChannelByName(name);
		if (channel != null) {
			exists = true;
		}
		return exists;
	}

	@Override
	public synchronized RuntimeChannel getRuntimeChannelById(String channelId) throws ServerException {
		checkChannelId(channelId);

		RuntimeChannel channel = findRuntimeChannelById(channelId);
		return channel;
	}

	@Override
	public synchronized RuntimeChannel getRuntimeChannelByName(String name) throws ServerException {
		checkChannelName(name);

		RuntimeChannel channel = findRuntimeChannelByName(name);
		return channel;
	}

	@Override
	public synchronized RuntimeChannel createRuntimeChannelById(String channelId) throws ServerException {
		checkChannelId(channelId);

		RuntimeChannel channel = findRuntimeChannelById(channelId);
		if (channel != null) {
			throw new ServerException("500", "Runtime channel (channelId='" + channelId + "') already exists.");
		}

		ChannelMetadata channelMetadata = null;
		try {
			channelMetadata = findChannelMetadataById(channelId);
		} catch (ClientException e) {
			handleException(e);
		}
		if (channelMetadata == null) {
			throw new ServerException("500", "Channel metadata (channelId='" + channelId + "') is not found.");
		}

		channel = new RuntimeChannelImpl(channelMetadata);
		checkInRuntimeChannel(channel);
		return channel;
	}

	@Override
	public synchronized RuntimeChannel createRuntimeChannelByName(String name) throws ServerException {
		checkChannelName(name);

		RuntimeChannel channel = findRuntimeChannelByName(name);
		if (channel != null) {
			throw new ServerException("500", "Runtime channel (name='" + name + "') already exists.");
		}

		ChannelMetadata channelMetadata = null;
		try {
			channelMetadata = findChannelMetadataByName(name);
		} catch (ClientException e) {
			handleException(e);
		}
		if (channelMetadata == null) {
			throw new ServerException("500", "Channel metadata (name='" + name + "') is not found.");
		}

		channel = new RuntimeChannelImpl(channelMetadata);
		checkInRuntimeChannel(channel);
		return channel;
	}

	@Override
	public synchronized boolean syncChannelMetadataById(String channelId) throws ServerException {
		checkChannelId(channelId);

		ChannelMetadata channelMetadata = null;
		try {
			channelMetadata = findChannelMetadataById(channelId);
		} catch (ClientException e) {
			handleException(e);
		}
		if (channelMetadata == null) {
			throw new ServerException("400", "Channel metadata (channelId='" + channelId + "') is not found.");
		}

		RuntimeChannel channel = findRuntimeChannelById(channelId);
		if (channel != null) {
			channel.setChannelMetadata(channelMetadata);
			return true;
		}

		return false;
	}

	@Override
	public synchronized boolean syncChannelMetadataByName(String name) throws ServerException {
		checkChannelName(name);

		ChannelMetadata channelMetadata = null;
		try {
			channelMetadata = findChannelMetadataByName(name);
		} catch (ClientException e) {
			handleException(e);
		}
		if (channelMetadata == null) {
			throw new ServerException("400", "Channel metadata (name='" + name + "') is not found.");
		}

		RuntimeChannel channel = findRuntimeChannelByName(name);
		if (channel != null) {
			channel.setChannelMetadata(channelMetadata);
			return true;
		}

		return false;
	}

	@Override
	public synchronized boolean removeRuntimeChannelById(String channelId) throws ServerException {
		checkChannelId(channelId);

		RuntimeChannel channel = checkOutRuntimeChannelById(channelId);
		if (channel != null) {
			channel.dispose();
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean removeRuntimeChannelByName(String name) throws ServerException {
		checkChannelName(name);

		RuntimeChannel channel = checkOutRuntimeChannelByName(name);
		if (channel != null) {
			channel.dispose();
			return true;
		}
		return false;
	}

	@Override
	public synchronized void disposeRuntimeChannels() throws ServerException {
		for (Iterator<String> itor = this.runtimeChannelMap.keySet().iterator(); itor.hasNext();) {
			String channelId = itor.next();
			RuntimeChannel channel = this.runtimeChannelMap.get(channelId);
			if (channel != null) {
				channel.dispose();
			}
		}
		this.runtimeChannelMap.clear();
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleException(Exception e) throws ServerException {
		throw new ServerException("500", e);
	}

}

// protected String name;
// protected String hostURL;
// protected String contextRoot;
// protected String httpPort;
// protected String namespace;
// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.COMPONENT_CHANNEL_NAMESPACE);
// @Override
// public String getNamespace() {
// return (String) this.properties.get(InfraConstants.COMPONENT_CHANNEL_NAMESPACE);
// }

// @Override
// public boolean addMessageListener(String channelId, ChannelMessageListener listener) {
// Channel channel = getChannel(channelId);
// return channel.addMessageListener(listener);
// }
//
// @Override
// public boolean removeOutboundListener(String channelId, ChannelMessageListener listener) {
// Channel channel = getChannel(channelId);
// return channel.removeMessageListener(listener);
// }
//
// @Override
// public List<ChannelMessageListener> getOutboundListeners(String channelId) {
// Channel channel = getChannel(channelId);
// List<ChannelMessageListener> listeners = channel.getMessageListeners();
// if (listeners == null) {
// listeners = Collections.emptyList();
// }
// return listeners;
// }
