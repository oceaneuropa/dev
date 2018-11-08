package org.orbit.infra.connector.datatube;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.datatube.DataTubeServiceMetadata;
import org.orbit.infra.api.datatube.RuntimeChannel;
import org.orbit.infra.connector.util.ModelConverter;
import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.datatube.ChannelMessageDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadataDTO;
import org.origin.common.rest.util.ResponseUtil;

public class DataTubeClientImpl extends ServiceClientImpl<DataTubeClient, DataTubeWSClient> implements DataTubeClient {

	private static final RuntimeChannel[] EMPTY_RUNTIME_CHANNELS = new RuntimeChannel[0];

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public DataTubeClientImpl(ServiceConnector<DataTubeClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected DataTubeWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new DataTubeWSClient(config);
	}

	@Override
	public DataTubeServiceMetadata getMetadata() throws ClientException {
		DataTubeServiceMetadataImpl metadata = new DataTubeServiceMetadataImpl();
		ServiceMetadataDTO metadataDTO = getWSClient().getMetadata();
		if (metadataDTO != null) {
			Map<String, Object> properties = metadataDTO.getProperties();
			if (properties != null && !properties.isEmpty()) {
				metadata.getProperties().putAll(properties);
			}
		}
		return metadata;
	}

	@Override
	public boolean send(String channelId, String senderId, String message) throws ClientException {
		try {
			ChannelMessageDTO messageDTO = new ChannelMessageDTO();
			messageDTO.setChannelId(channelId);
			messageDTO.setSenderId(senderId);
			messageDTO.setMessage(message);

			int result = this.client.sendMessage(messageDTO);
			if (result > 0) {
				return true;
			}

		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public RuntimeChannel[] getRuntimeChannels() throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__LIST_RUNTIME_CHANNELS);

		RuntimeChannel[] runtimeChannels = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				runtimeChannels = ModelConverter.DATA_TUBE.getRuntimeChannels(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (runtimeChannels == null) {
			runtimeChannels = EMPTY_RUNTIME_CHANNELS;
		}
		return runtimeChannels;
	}

	@Override
	public RuntimeChannel getRuntimeChannelId(String channelId, boolean createIfNotExist) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__GET_RUNTIME_CHANNEL);
		request.setParameter("channel_id", channelId);
		if (createIfNotExist) {
			request.setParameter("create_if_not_exist", createIfNotExist);
		}

		RuntimeChannel runtimeChannel = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				runtimeChannel = ModelConverter.DATA_TUBE.getRuntimeChannel(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return runtimeChannel;
	}

	@Override
	public RuntimeChannel getRuntimeChannelByName(String name, boolean createIfNotExist) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__GET_RUNTIME_CHANNEL);
		request.setParameter("name", name);
		if (createIfNotExist) {
			request.setParameter("create_if_not_exist", createIfNotExist);
		}

		RuntimeChannel runtimeChannel = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				runtimeChannel = ModelConverter.DATA_TUBE.getRuntimeChannel(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return runtimeChannel;
	}

	@Override
	public boolean runtimeChannelExistsById(String channelId) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__RUNTIME_CHANNEL_EXISTS);
		request.setParameter("channel_id", channelId);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ModelConverter.COMMON.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public boolean runtimeChannelExistsByName(String name) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__RUNTIME_CHANNEL_EXISTS);
		request.setParameter("name", name);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ModelConverter.COMMON.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public RuntimeChannel createRuntimeChannelId(String channelId, boolean useExisting) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__CREATE_RUNTIME_CHANNEL);
		request.setParameter("channel_id", channelId);
		request.setParameter("use_existing", useExisting);

		RuntimeChannel runtimeChannel = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				runtimeChannel = ModelConverter.DATA_TUBE.getRuntimeChannel(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return runtimeChannel;
	}

	@Override
	public RuntimeChannel createRuntimeChannelByName(String name, boolean useExisting) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__CREATE_RUNTIME_CHANNEL);
		request.setParameter("name", name);
		request.setParameter("use_existing", useExisting);

		RuntimeChannel runtimeChannel = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				runtimeChannel = ModelConverter.DATA_TUBE.getRuntimeChannel(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return runtimeChannel;
	}

	@Override
	public boolean syncChannelMetadataId(String channelId, boolean createIfNotExist) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__SYNC_CHANNEL_METADATA);
		request.setParameter("channel_id", channelId);
		if (createIfNotExist) {
			request.setParameter("create_if_not_exist", createIfNotExist);
		}

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.COMMON.isSucceed(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public boolean syncChannelMetadataByName(String name, boolean createIfNotExist) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__SYNC_CHANNEL_METADATA);
		request.setParameter("name", name);
		if (createIfNotExist) {
			request.setParameter("create_if_not_exist", createIfNotExist);
		}

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.COMMON.isSucceed(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public boolean startRuntimeChannelById(String channelId) throws ClientException {
		return runtimeChannelOnActionById(channelId, RequestConstants.RUNTIME_CHANNEL_ACTION__START);
	}

	@Override
	public boolean startRuntimeChannelByName(String name) throws ClientException {
		return runtimeChannelOnActionByName(name, RequestConstants.RUNTIME_CHANNEL_ACTION__START);
	}

	@Override
	public boolean suspendRuntimeChannelById(String channelId) throws ClientException {
		return runtimeChannelOnActionById(channelId, RequestConstants.RUNTIME_CHANNEL_ACTION__SUSPEND);
	}

	@Override
	public boolean suspendRuntimeChannelByName(String name) throws ClientException {
		return runtimeChannelOnActionByName(name, RequestConstants.RUNTIME_CHANNEL_ACTION__SUSPEND);
	}

	@Override
	public boolean stopRuntimeChannelById(String channelId) throws ClientException {
		return runtimeChannelOnActionById(channelId, RequestConstants.RUNTIME_CHANNEL_ACTION__STOP);
	}

	@Override
	public boolean stopRuntimeChannelByName(String name) throws ClientException {
		return runtimeChannelOnActionByName(name, RequestConstants.RUNTIME_CHANNEL_ACTION__STOP);
	}

	/**
	 * 
	 * @param channelId
	 * @param action
	 * @return
	 * @throws ClientException
	 */
	protected boolean runtimeChannelOnActionById(String channelId, String action) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__RUNTIME_CHANNEL_ON_ACTION);
		request.setParameter("channel_id", channelId);
		request.setParameter("action", action);

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.COMMON.isSucceed(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	/**
	 * 
	 * @param name
	 * @param action
	 * @return
	 * @throws ClientException
	 */
	protected boolean runtimeChannelOnActionByName(String name, String action) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__RUNTIME_CHANNEL_ON_ACTION);
		request.setParameter("name", name);
		request.setParameter("action", action);

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.COMMON.isSucceed(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteRuntimeChannelId(String channelId) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__DELETE_RUNTIME_CHANNEL);
		request.setParameter("channel_id", channelId);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteRuntimeChannelByName(String name) throws ClientException {
		Request request = new Request(RequestConstants.DATATUBE__DELETE_RUNTIME_CHANNEL);
		request.setParameter("name", name);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

}

// Map<String, Object> getProperties();
// void update(Map<String, Object> properties);
// boolean ping();
// boolean close() throws ClientException;
// protected Map<String, Object> properties;
// protected DataTubeWSClient client;
// protected AdaptorSupport adaptorSupport = new AdaptorSupport();
// /**
// *
// * @param connector
// * @param properties
// */
// public DataTubeClientImpl(ServiceConnector<DataTubeClient> connector, Map<String, Object> properties) {
// if (connector != null) {
// adapt(ServiceConnector.class, connector);
// }
// this.properties = checkProperties(properties);
// initClient();
// }
// protected Map<String, Object> checkProperties(Map<String, Object> properties) {
// if (properties == null) {
// properties = new HashMap<String, Object>();
// }
// return properties;
// }
// @Override
// public boolean close() throws ClientException {
// @SuppressWarnings("unchecked")
// ServiceConnector<DataTubeClient> connector = getAdapter(ServiceConnector.class);
// if (connector != null) {
// return connector.close(this);
// }
// return false;
// }
//
// @Override
// public Map<String, Object> getProperties() {
// return this.properties;
// }
//
// @Override
// public void update(Map<String, Object> properties) {
// this.properties = checkProperties(properties);
// initClient();
// }
//
// protected void initClient() {
// WSClientConfiguration config = WSClientConfiguration.create(this.properties);
// this.client = new DataTubeWSClient(config);
// }
//
// @Override
// public boolean ping() {
// return this.client.doPing();
// }
//
// @Override
// public <T> void adapt(Class<T> clazz, T object) {
// this.adaptorSupport.adapt(clazz, object);
// }
//
// @Override
// public <T> void adapt(Class<T>[] classes, T object) {
// this.adaptorSupport.adapt(classes, object);
// }
//
// @Override
// public <T> T getAdapter(Class<T> adapter) {
// return this.adaptorSupport.getAdapter(adapter);
// }
