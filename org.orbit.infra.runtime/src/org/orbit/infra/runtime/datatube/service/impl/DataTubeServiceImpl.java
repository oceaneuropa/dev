package org.orbit.infra.runtime.datatube.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.orbit.infra.api.datacast.ChannelMetadata;
import org.orbit.infra.api.datacast.ChannelStatus;
import org.orbit.infra.api.datacast.DataCastClientResolver;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.ChannelMetadataComparator;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.infra.io.util.InfraIndexItemHelper;
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

		// Register service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(DataTubeService.class, this, props);

		try {
			initRuntimeChannels();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		DataTubeConfigPropertiesHandler.getInstance().removePropertyChangeListener(this);

		try {
			disposeRuntimeChannels();
		} catch (ServerException e) {
			e.printStackTrace();
		}
	}

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

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientException
	 */
	protected synchronized ChannelMetadata[] findChannelMetadatas() throws IOException, ClientException {
		ChannelMetadata[] channelMetadatas = null;
		String dataCastId = getDataCastId();
		String dataTubeId = getDataTubeId();
		String accessToken = getAccessToken();
		String indexServiceUrl = getIndexServiceURL();

		IndexItem dataCastIndexItem = InfraIndexItemHelper.getDataCastIndexItem(indexServiceUrl, accessToken, dataCastId);
		if (dataCastIndexItem != null) {
			boolean isDataCastOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);
			if (isDataCastOnline) {
				String dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__BASE_URL);
				DataCastClientResolver clientResolver = new DefaultDataCastClientResolver(indexServiceUrl);
				channelMetadatas = InfraClientsHelper.DATA_CAST.getChannelMetadatas(clientResolver, dataCastServiceUrl, accessToken, dataTubeId, ChannelMetadataComparator.ASC);

			} else {
				throw new ClientException(500, "DataCast service (dataCastId='" + dataCastId + "') is not online.");
			}
		}
		return channelMetadatas;
	}

	/**
	 * 
	 * @param channelId
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	protected ChannelMetadata getChannelMetadataById(String channelId) throws ClientException, IOException {
		ChannelMetadata channelMetadata = null;

		String dataCastId = getDataCastId();
		String accessToken = getAccessToken();
		String indexServiceUrl = getIndexServiceURL();

		IndexItem dataCastIndexItem = InfraIndexItemHelper.getDataCastIndexItem(indexServiceUrl, accessToken, dataCastId);
		if (dataCastIndexItem != null) {
			boolean isDataCastOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);
			if (isDataCastOnline) {
				String dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__BASE_URL);
				DataCastClientResolver clientResolver = new DefaultDataCastClientResolver(indexServiceUrl);
				channelMetadata = InfraClientsHelper.DATA_CAST.getChannelMetadataByChannelId(clientResolver, dataCastServiceUrl, accessToken, channelId);
			} else {
				throw new ClientException(500, "DataCast service (dataCastId='" + dataCastId + "') is not online.");
			}
		}

		return channelMetadata;
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	protected ChannelMetadata getChannelMetadataByName(String name) throws ClientException, IOException {
		ChannelMetadata channelMetadata = null;

		String dataCastId = getDataCastId();
		String accessToken = getAccessToken();
		String indexServiceUrl = getIndexServiceURL();

		IndexItem dataCastIndexItem = InfraIndexItemHelper.getDataCastIndexItem(indexServiceUrl, accessToken, dataCastId);
		if (dataCastIndexItem != null) {
			boolean isDataCastOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);
			if (isDataCastOnline) {
				String dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__BASE_URL);
				DataCastClientResolver clientResolver = new DefaultDataCastClientResolver(indexServiceUrl);
				channelMetadata = InfraClientsHelper.DATA_CAST.getChannelMetadataByChannelName(clientResolver, dataCastServiceUrl, accessToken, name);
			} else {
				throw new ClientException(500, "DataCast service (dataCastId='" + dataCastId + "') is not online.");
			}
		}

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
		RuntimeChannel runtimeChannel = this.runtimeChannelMap.get(channelId);
		return runtimeChannel;
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
	 * @param runtimeChannel
	 * @return
	 */
	protected synchronized boolean checkInRuntimeChannel(RuntimeChannel runtimeChannel) {
		if (runtimeChannel != null) {
			String channelId = runtimeChannel.getChannelMetadata().getChannelId();
			this.runtimeChannelMap.put(channelId, runtimeChannel);
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
		RuntimeChannel runtimeChannel = this.runtimeChannelMap.remove(channelId);
		return runtimeChannel;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected synchronized RuntimeChannel checkOutRuntimeChannelByName(String name) {
		RuntimeChannel runtimeChannel = null;
		for (Iterator<String> itor = this.runtimeChannelMap.keySet().iterator(); itor.hasNext();) {
			String channelId = itor.next();
			RuntimeChannel currRuntimeChannel = this.runtimeChannelMap.get(channelId);

			String currName = currRuntimeChannel.getChannelMetadata().getName();
			if (name.equals(currName)) {
				runtimeChannel = currRuntimeChannel;
				break;
			}
		}
		if (runtimeChannel != null) {
			String channelId = runtimeChannel.getChannelMetadata().getChannelId();
			this.runtimeChannelMap.remove(channelId);
		}
		return runtimeChannel;
	}

	protected synchronized void initRuntimeChannels() throws IOException, ClientException {
		ChannelMetadata[] channelMetadatas = findChannelMetadatas();
		if (channelMetadatas != null) {
			for (ChannelMetadata channelMetadata : channelMetadatas) {
				RuntimeChannel runtimeChannel = new RuntimeChannelImpl(channelMetadata);
				checkInRuntimeChannel(runtimeChannel);
			}
		}
	}

	@Override
	public synchronized RuntimeChannel[] getRuntimeChannels() throws ServerException {
		return findRuntimeChannels();
	}

	@Override
	public synchronized boolean runtimeChannelExistsById(String channelId) throws ServerException {
		checkChannelId(channelId);

		boolean exists = false;
		RuntimeChannel runtimeChannel = findRuntimeChannelById(channelId);
		if (runtimeChannel != null) {
			exists = true;
		}
		return exists;
	}

	@Override
	public synchronized boolean runtimeChannelExistsByName(String name) throws ServerException {
		checkChannelName(name);

		boolean exists = false;
		RuntimeChannel runtimeChannel = findRuntimeChannelByName(name);
		if (runtimeChannel != null) {
			exists = true;
		}
		return exists;
	}

	@Override
	public synchronized RuntimeChannel getRuntimeChannelById(String channelId) throws ServerException {
		checkChannelId(channelId);

		RuntimeChannel runtimeChannel = findRuntimeChannelById(channelId);
		return runtimeChannel;
	}

	@Override
	public synchronized RuntimeChannel getRuntimeChannelByName(String name) throws ServerException {
		checkChannelName(name);

		RuntimeChannel runtimeChannel = findRuntimeChannelByName(name);
		return runtimeChannel;
	}

	@Override
	public synchronized RuntimeChannel createRuntimeChannelById(String channelId) throws ServerException {
		checkChannelId(channelId);

		RuntimeChannel runtimeChannel = findRuntimeChannelById(channelId);
		if (runtimeChannel != null) {
			throw new ServerException("404", "Runtime channel (channelId='" + channelId + "') already exists.");
		}

		ChannelMetadata channelMetadata = null;
		try {
			channelMetadata = getChannelMetadataById(channelId);
		} catch (ClientException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}

		if (channelMetadata == null) {
			throw new ServerException("404", "Channel metadata (channelId='" + channelId + "') is not found.");
		}

		runtimeChannel = new RuntimeChannelImpl(channelMetadata);
		checkInRuntimeChannel(runtimeChannel);

		return runtimeChannel;
	}

	@Override
	public synchronized RuntimeChannel createRuntimeChannelByName(String name) throws ServerException {
		checkChannelName(name);

		RuntimeChannel runtimeChannel = findRuntimeChannelByName(name);
		if (runtimeChannel != null) {
			throw new ServerException("404", "Runtime channel (name='" + name + "') already exists.");
		}

		ChannelMetadata channelMetadata = null;
		try {
			channelMetadata = getChannelMetadataByName(name);
		} catch (ClientException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}

		if (channelMetadata == null) {
			throw new ServerException("404", "Channel metadata (name='" + name + "') is not found.");
		}

		runtimeChannel = new RuntimeChannelImpl(channelMetadata);
		checkInRuntimeChannel(runtimeChannel);
		return runtimeChannel;
	}

	@Override
	public synchronized boolean syncRuntimeChannelById(String channelId) throws ServerException {
		checkChannelId(channelId);

		boolean succeed = false;

		ChannelMetadata channelMetadata = null;
		try {
			channelMetadata = getChannelMetadataById(channelId);
			if (channelMetadata == null) {
				throw new ServerException("404", "Channel metadata (channelId='" + channelId + "') is not found.");
			}
		} catch (ClientException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}

		RuntimeChannel runtimeChannel = findRuntimeChannelById(channelId);

		ChannelStatus channelStatus = channelMetadata.getStatus();

		if (ChannelStatus.STARTED.contains(channelStatus)) {
			if (runtimeChannel == null) {
				runtimeChannel = new RuntimeChannelImpl(channelMetadata);
				checkInRuntimeChannel(runtimeChannel);
			} else {
				runtimeChannel.setChannelMetadata(channelMetadata);
			}
			succeed = true;

		} else if (ChannelStatus.SUSPENDED.contains(channelStatus)) {
			if (runtimeChannel == null) {
				runtimeChannel = new RuntimeChannelImpl(channelMetadata);
				checkInRuntimeChannel(runtimeChannel);
			} else {
				runtimeChannel.setChannelMetadata(channelMetadata);
			}
			succeed = true;

		} else if (ChannelStatus.STOPPED.contains(channelStatus)) {
			if (runtimeChannel != null) {
				removeRuntimeChannelById(channelId);
			}
			succeed = true;
		}

		return succeed;
	}

	@Override
	public synchronized boolean syncRuntimeChannelByName(String name) throws ServerException {
		checkChannelName(name);

		boolean succeed = false;

		ChannelMetadata channelMetadata = null;
		try {
			channelMetadata = getChannelMetadataByName(name);
			if (channelMetadata == null) {
				throw new ServerException("404", "Channel metadata (name='" + name + "') is not found.");
			}
		} catch (ClientException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}

		RuntimeChannel runtimeChannel = findRuntimeChannelByName(name);

		ChannelStatus channelStatus = channelMetadata.getStatus();
		if (ChannelStatus.STARTED.contains(channelStatus)) {
			if (runtimeChannel == null) {
				runtimeChannel = new RuntimeChannelImpl(channelMetadata);
				checkInRuntimeChannel(runtimeChannel);
			} else {
				runtimeChannel.setChannelMetadata(channelMetadata);
			}
			succeed = true;

		} else if (ChannelStatus.SUSPENDED.contains(channelStatus)) {
			if (runtimeChannel == null) {
				runtimeChannel = new RuntimeChannelImpl(channelMetadata);
				checkInRuntimeChannel(runtimeChannel);
			} else {
				runtimeChannel.setChannelMetadata(channelMetadata);
			}
			succeed = true;

		} else if (ChannelStatus.STOPPED.contains(channelStatus)) {
			if (runtimeChannel != null) {
				removeRuntimeChannelByName(name);
			}
			succeed = true;
		}

		return succeed;
	}

	@Override
	public synchronized boolean startRuntimeChannelById(String channelId) throws ServerException {
		checkChannelId(channelId);

		boolean succeed = syncRuntimeChannelById(channelId);
		return succeed;
	}

	@Override
	public synchronized boolean startRuntimeChannelByName(String name) throws ServerException {
		checkChannelName(name);

		boolean succeed = syncRuntimeChannelByName(name);
		return succeed;
	}

	@Override
	public synchronized boolean suspendRuntimeChannelById(String channelId) throws ServerException {
		checkChannelId(channelId);

		boolean succeed = syncRuntimeChannelById(channelId);
		return succeed;
	}

	@Override
	public synchronized boolean suspendRuntimeChannelByName(String name) throws ServerException {
		checkChannelName(name);

		boolean succeed = syncRuntimeChannelByName(name);
		return succeed;
	}

	@Override
	public synchronized boolean stopRuntimeChannelById(String channelId) throws ServerException {
		checkChannelId(channelId);

		boolean succeed = syncRuntimeChannelById(channelId);
		return succeed;
	}

	@Override
	public synchronized boolean stopRuntimeChannelByName(String name) throws ServerException {
		checkChannelName(name);

		boolean succeed = syncRuntimeChannelByName(name);
		return succeed;
	}

	@Override
	public synchronized boolean removeRuntimeChannelById(String channelId) throws ServerException {
		checkChannelId(channelId);

		RuntimeChannel runtimeChannel = checkOutRuntimeChannelById(channelId);
		if (runtimeChannel != null) {
			runtimeChannel.dispose();
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean removeRuntimeChannelByName(String name) throws ServerException {
		checkChannelName(name);

		RuntimeChannel runtimeChannel = checkOutRuntimeChannelByName(name);
		if (runtimeChannel != null) {
			runtimeChannel.dispose();
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

// String hostURL = (String) this.properties.get(InfraConstants.DATATUBE__HOST_URL);
// if (hostURL != null) {
// return hostURL;
// }
// String globalHostURL = (String) this.properties.get(InfraConstants.ORBIT_HOST_URL);
// if (globalHostURL != null) {
// return globalHostURL;
// }

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

// /**
// *
// * @param channelId
// * @return
// * @throws ServerException
// */
// protected synchronized boolean updateChannelMetadataById(String channelId) throws ServerException {
// checkChannelId(channelId);
//
// ChannelMetadata channelMetadata = null;
// try {
// channelMetadata = findChannelMetadataById(channelId);
// if (channelMetadata == null) {
// throw new ServerException("404", "Channel metadata (channelId='" + channelId + "') is not found.");
// }
// } catch (ClientException e) {
// handleException(e);
// } catch (IOException e) {
// handleException(e);
// }
//
// RuntimeChannel runtimeChannel = findRuntimeChannelById(channelId);
// if (runtimeChannel != null) {
// runtimeChannel.setChannelMetadata(channelMetadata);
// return true;
//
// } else {
// runtimeChannel = createRuntimeChannelById(channelId);
// if (runtimeChannel == null) {
// throw new ServerException("404", "Runtime channel (channelId='" + channelId + "') doesn't exist and cannot be created.");
// }
// return true;
// }
// }
//
// /**
// *
// * @param name
// * @return
// * @throws ServerException
// */
// protected synchronized boolean updateChannelMetadataByName(String name) throws ServerException {
// checkChannelName(name);
//
// ChannelMetadata channelMetadata = null;
// try {
// channelMetadata = findChannelMetadataByName(name);
// if (channelMetadata == null) {
// throw new ServerException("404", "Channel metadata (name='" + name + "') is not found.");
// }
// } catch (ClientException e) {
// handleException(e);
// } catch (IOException e) {
// handleException(e);
// }
//
// RuntimeChannel runtimeChannel = findRuntimeChannelByName(name);
// if (runtimeChannel != null) {
// runtimeChannel.setChannelMetadata(channelMetadata);
// return true;
//
// } else {
// runtimeChannel = createRuntimeChannelByName(name);
// if (runtimeChannel == null) {
// throw new ServerException("404", "Runtime channel (name='" + name + "') doesn't exist and cannot be created.");
// }
// return true;
// }
// }
