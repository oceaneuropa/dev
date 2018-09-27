package org.orbit.component.connector.tier3.nodecontrol;

import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.connector.util.ModelConverter;
import org.orbit.component.model.RequestConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.util.ResponseUtil;

public class NodeControlClientImpl extends ServiceClientImpl<NodeControlClient, NodeControlWSClient> implements NodeControlClient {

	private static final NodeInfo[] EMPTY_NODE_INFOS = new NodeInfo[0];

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public NodeControlClientImpl(ServiceConnector<NodeControlClient> connector, Map<String, Object> properties) {
		super(connector, properties);
		// if (connector != null) {
		// adapt(ServiceConnector.class, connector);
		// }
		// this.properties = checkProperties(properties);
		// initClient();
	}

	// protected void initClient() {
	// String realm = (String) this.properties.get(OrbitConstants.REALM);
	// String username = (String) this.properties.get(OrbitConstants.ACCESS_TOKEN);
	// String fullUrl = (String) this.properties.get(OrbitConstants.URL);
	//
	// ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
	// this.client = new NodeControlWSClient(config);
	// }

	@Override
	protected NodeControlWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new NodeControlWSClient(config);
	}

	@Override
	public NodeInfo[] getNodes() throws ClientException {
		Request request = new Request(RequestConstants.GET_NODES);
		NodeInfo[] nodeInfos = null;

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				nodeInfos = ModelConverter.NodeControl.getNodes(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (nodeInfos == null) {
			nodeInfos = EMPTY_NODE_INFOS;
		}
		return nodeInfos;
	}

	@Override
	public NodeInfo getNode(String id) throws ClientException {
		Request request = new Request(RequestConstants.GET_NODE);
		request.setParameter("id", id);

		NodeInfo nodeInfo = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				nodeInfo = ModelConverter.NodeControl.getNode(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return nodeInfo;
	}

	@Override
	public boolean createNode(String id, String name, String typeId) throws ClientException {
		Request request = new Request(RequestConstants.CREATE_NODE);
		request.setParameter("id", id);
		request.setParameter("name", name);
		request.setParameter("typeId", typeId);

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.NodeControl.isCreated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public boolean updateNode(String id, String name, String typeId) throws ClientException {
		Request request = new Request(RequestConstants.UPDATE_NODE);
		request.setParameter("id", id);
		request.setParameter("name", name);
		request.setParameter("typeId", typeId);

		if (name != null) {
			request.setParameter("update_name", true);
		}
		if (typeId != null) {
			request.setParameter("update_type", true);
		}

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.NodeControl.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteNode(String id) throws ClientException {
		Request request = new Request(RequestConstants.DELETE_NODE);
		request.setParameter("id", id);

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.NodeControl.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public boolean startNode(String id, Map<String, Object> options) throws ClientException {
		Request request = new Request(RequestConstants.START_NODE);
		request.setParameter("id", id);

		if (options != null) {
			for (Iterator<String> itor = options.keySet().iterator(); itor.hasNext();) {
				String optionName = itor.next();
				Object optionValue = options.get(optionName);
				if (optionName != null && optionValue != null) {
					request.setParameter(optionName, optionValue);
				}
			}
		}

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.NodeControl.isStarted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public boolean stopNode(String id, Map<String, Object> options) throws ClientException {
		Request request = new Request(RequestConstants.STOP_NODE);
		request.setParameter("id", id);

		if (options != null) {
			for (Iterator<String> itor = options.keySet().iterator(); itor.hasNext();) {
				String optionName = itor.next();
				Object optionValue = options.get(optionName);
				if (optionName != null && optionValue != null) {
					request.setParameter(optionName, optionValue);
				}
			}
		}

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.NodeControl.isStopped(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public boolean addNodeAttribute(String id, String name, Object value) throws ClientException {
		Request request = new Request(RequestConstants.ADD_NODE_ATTR);
		request.setParameter("id", id);
		request.setParameter("name", name);
		request.setParameter("value", value);

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.NodeControl.isSucceed(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public boolean updateNodeAttribute(String id, String oldName, String name, Object value) throws ClientException {
		Request request = new Request(RequestConstants.UPDATE_NODE_ATTR);
		request.setParameter("id", id);
		request.setParameter("oldName", oldName);
		request.setParameter("name", name);
		request.setParameter("value", value);

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.NodeControl.isSucceed(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteNodeAttribute(String id, String name) throws ClientException {
		Request request = new Request(RequestConstants.DELETE_NODE_ATTR);
		request.setParameter("id", id);
		request.setParameter("name", name);

		boolean succeed = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ModelConverter.NodeControl.isSucceed(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	// @Override
	// public boolean close() throws ClientException {
	// @SuppressWarnings("unchecked")
	// ServiceConnector<NodeControlClient> connector = getAdapter(ServiceConnector.class);
	// if (connector != null) {
	// return connector.close(this);
	// }
	// return false;
	// }

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

	// @Override
	// public String getURL() {
	// String fullUrl = (String) properties.get(OrbitConstants.URL);
	// return fullUrl;
	// }
	//
	// @Override
	// public boolean ping() {
	// return this.client.doPing();
	// }
	//
	// @Override
	// public String echo(String message) throws ClientException {
	// return this.client.echo(message);
	// }

	@Override
	public String level(String level1, String level2, String message1, String message2) throws ClientException {
		return this.client.level(level1, level2, message1, message2);
	}

	// @Override
	// public Response sendRequest(Request request) throws ClientException {
	// return this.client.sendRequest(request);
	// }
	//
	// @Override
	// public <T> void adapt(Class<T> clazz, T object) {
	// this.adaptorSupport.adapt(clazz, object);
	// }
	//
	// @Override
	// public <T> T getAdapter(Class<T> adapter) {
	// return this.adaptorSupport.getAdapter(adapter);
	// }
	//
	// @Override
	// public boolean isProxy() {
	// return false;
	// }

}

// boolean ping() throws ClientException;
// Response sendRequest(Request request) throws ClientException;

// private Map<String, Object> checkProperties(Map<String, Object> properties) {
// if (properties == null) {
// properties = new HashMap<String, Object>();
// }
// return properties;
// }
