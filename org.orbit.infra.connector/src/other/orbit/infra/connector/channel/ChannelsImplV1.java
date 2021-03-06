package other.orbit.infra.connector.channel;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

//import org.orbit.datatube.api.datatube.DataTubeClient;
//import org.orbit.datatube.api.datatube.DataTubeServiceMetadata;
//import org.orbit.datatube.api.datatube.RuntimeChannel;
//import org.orbit.datatube.connector.datatube.DataTubeWSClient;
//import org.orbit.datatube.model.datatube.ChannelMessageDTO;
import org.orbit.infra.api.InfraConstants;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.model.Request;
import org.origin.common.util.StringUtil;

public class ChannelsImplV1 /* implements DataTubeClient */ {

	protected Map<String, Object> properties;
	// protected DataTubeWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	// public ChannelsImplV1(ServiceConnector<DataTubeClient> connector, Map<String, Object> properties) {
	public ChannelsImplV1(ServiceConnector<?> connector, Map<String, Object> properties) {
		if (connector != null) {
			adapt(ServiceConnector.class, connector);
		}
		this.properties = checkProperties(properties);
		init();
	}

	protected Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	// @Override
	public boolean close() throws ClientException {
		// @SuppressWarnings("unchecked")
		// ServiceConnector<DataTubeClient> connector = getAdapter(ServiceConnector.class);
		// if (connector != null) {
		// return connector.close(this);
		// }
		return false;
	}

	// @Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	// @Override
	public void update(Map<String, Object> properties) {
		properties = checkProperties(properties);

		String oldUrl = (String) this.properties.get(InfraConstants.SERVICE__HOST_URL);
		String oldContextRoot = (String) this.properties.get(InfraConstants.SERVICE__CONTEXT_ROOT);

		this.properties.putAll(properties);

		String newUrl = (String) properties.get(InfraConstants.SERVICE__HOST_URL);
		String newContextRoot = (String) properties.get(InfraConstants.SERVICE__CONTEXT_ROOT);

		boolean reinit = false;
		if (!StringUtil.equals(oldUrl, newUrl) || !StringUtil.equals(oldContextRoot, newContextRoot)) {
			reinit = true;
		}
		if (reinit) {
			init();
		}
	}

	protected void init() {
		String realm = (String) this.properties.get(WSClientConstants.REALM);
		String accessToken = (String) this.properties.get(WSClientConstants.ACCESS_TOKEN);
		String url = (String) this.properties.get(InfraConstants.SERVICE__HOST_URL);
		String contextRoot = (String) this.properties.get(InfraConstants.SERVICE__CONTEXT_ROOT);

		WSClientConfiguration clientConfig = WSClientConfiguration.create(realm, accessToken, url, contextRoot);
		// this.client = new DataTubeWSClient(clientConfig);
	}

	// @Override
	public boolean ping() {
		// return this.client.doPing();
		return false;
	}

	// @Override
	public boolean send(String channelId, String senderId, String message) throws ClientException {
		// try {
		// ChannelMessageDTO messageDTO = new ChannelMessageDTO();
		// messageDTO.setChannelId(channelId);
		// messageDTO.setSenderId(senderId);
		// messageDTO.setMessage(message);
		//
		// int result = this.client.sendMessage(messageDTO);
		// if (result > 0) {
		// return true;
		// }
		//
		// } catch (ClientException e) {
		// throw e;
		// }
		return false;
	}

	// @Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	// @Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	// @Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

	// @Override
	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	// public DataTubeServiceMetadata getMetadata() throws ClientException {
	public Object getMetadata() throws ClientException {
		return null;
	}

	// @Override
	public String getName() throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	public String echo(String message) throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	public Response sendRequest(Request request) throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	public boolean isProxy() {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	// public RuntimeChannel[] getRuntimeChannels() throws ClientException {
	public Object[] getRuntimeChannels() throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	// public RuntimeChannel getRuntimeChannelId(String channelId, boolean createIfNotExist) throws ClientException {
	public Object getRuntimeChannelId(String channelId, boolean createIfNotExist) throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	// public RuntimeChannel getRuntimeChannelByName(String name, boolean createIfNotExist) throws ClientException {
	public Object getRuntimeChannelByName(String name, boolean createIfNotExist) throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	public boolean runtimeChannelExistsById(String channelId) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public boolean runtimeChannelExistsByName(String name) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	// public RuntimeChannel createRuntimeChannelById(String channelId, boolean useExisting) throws ClientException {
	public Object createRuntimeChannelById(String channelId, boolean useExisting) throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	// public RuntimeChannel createRuntimeChannelByName(String name, boolean useExisting) throws ClientException {
	public Object createRuntimeChannelByName(String name, boolean useExisting) throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	public boolean syncRuntimeChannelById(String channelId) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public boolean syncRuntimeChannelByName(String name) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public boolean startRuntimeChannelById(String channelId) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public boolean startRuntimeChannelByName(String name) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public boolean suspendRuntimeChannelById(String channelId) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public boolean suspendRuntimeChannelByName(String name) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public boolean stopRuntimeChannelById(String channelId) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public boolean stopRuntimeChannelByName(String name) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public boolean deleteRuntimeChannelId(String channelId) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	public boolean deleteRuntimeChannelByName(String name) throws ClientException {
		// TODO Auto-generated method stub
		return false;
	}

}
