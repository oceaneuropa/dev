package org.orbit.infra.connector.subs;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.infra.api.subs.SubsMapping;
import org.orbit.infra.api.subs.SubsServerAPI;
import org.orbit.infra.api.subs.SubsSource;
import org.orbit.infra.api.subs.SubsTarget;
import org.orbit.infra.connector.util.ClientModelConverter;
import org.orbit.infra.model.RequestConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.util.ResponseUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsServerAPIImpl extends ServiceClientImpl<SubsServerAPI, SubsServerWSClient> implements SubsServerAPI {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public SubsServerAPIImpl(ServiceConnector<SubsServerAPI> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected SubsServerWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new SubsServerWSClient(config);
	}

	@Override
	public List<SubsSource> getSources() throws ClientException {
		List<SubsSource> sources = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__LIST_SOURCES);

			response = sendRequest(request);
			if (response != null) {
				sources = ClientModelConverter.SUBS_SERVER.toSources(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (sources == null) {
			sources = Collections.emptyList();
		}
		return sources;
	}

	@Override
	public List<SubsSource> getSources(String type) throws ClientException {
		checkNullParameter(type, "Source type is null.");

		List<SubsSource> sources = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__LIST_SOURCES);
			request.setParameter("type", type);

			response = sendRequest(request);
			if (response != null) {
				sources = ClientModelConverter.SUBS_SERVER.toSources(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (sources == null) {
			sources = Collections.emptyList();
		}
		return sources;
	}

	@Override
	public SubsSource getSource(Integer sourceId) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");

		SubsSource source = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__GET_SOURCE);
			request.setParameter("id", sourceId);

			response = sendRequest(request);
			if (response != null) {
				source = ClientModelConverter.SUBS_SERVER.toSource(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return source;
	}

	@Override
	public SubsSource getSource(String type, String instanceId) throws ClientException {
		checkNullParameter(type, "Source type is null.");
		checkNullParameter(instanceId, "instanceId is null.");

		SubsSource source = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__GET_SOURCE);
			request.setParameter("type", type);
			request.setParameter("instanceId", instanceId);

			response = sendRequest(request);
			if (response != null) {
				source = ClientModelConverter.SUBS_SERVER.toSource(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return source;
	}

	@Override
	public boolean sourceExists(Integer sourceId) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__SOURCE_EXISTS);
		request.setParameter("id", sourceId);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ResponseUtil.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public boolean sourceExists(String type, String instanceId) throws ClientException {
		checkNullParameter(type, "Source type is null.");
		checkNullParameter(instanceId, "Source instanceId is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__SOURCE_EXISTS);
		request.setParameter("type", type);
		request.setParameter("instanceId", instanceId);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ResponseUtil.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public SubsSource createSource(String type, String instanceId, String name, Map<String, Object> properties) throws ClientException {
		checkNullParameter(type, "Source type is null.");
		checkNullParameter(instanceId, "Source instanceId is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__CREATE_SOURCE);
		request.setParameter("type", type);
		request.setParameter("instanceId", instanceId);
		if (name != null) {
			request.setParameter("name", name);
		}
		if (properties != null) {
			request.setParameter("properties", properties);
		}

		SubsSource source = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				source = ClientModelConverter.SUBS_SERVER.toSource(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return source;
	}

	@Override
	public boolean updateSourceTypeAndInstanceId(Integer sourceId, String type, String instanceId) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");
		checkNullParameter(type, "Source type is null.");
		checkNullParameter(instanceId, "Source instanceId is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_SOURCE);
		request.setParameter("id", sourceId);
		request.setParameter("type", type);
		request.setParameter("instanceId", instanceId);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateSourceType(Integer sourceId, String type) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");
		checkNullParameter(type, "Source type is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_SOURCE);
		request.setParameter("id", sourceId);
		request.setParameter("type", type);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateSourceInstanceId(Integer sourceId, String instanceId) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");
		checkNullParameter(instanceId, "Source instanceId is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_SOURCE);
		request.setParameter("id", sourceId);
		request.setParameter("instanceId", instanceId);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateSourceName(Integer sourceId, String name) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");
		checkNullParameter(name, "Source name is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_SOURCE);
		request.setParameter("id", sourceId);
		request.setParameter("name", name);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateSourceProperties(Integer sourceId, Map<String, Object> properties, boolean clearProperties) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");

		if (properties == null) {
			properties = new HashMap<String, Object>();
		}

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_SOURCE);
		request.setParameter("id", sourceId);
		request.setParameter("properties", properties);
		request.setParameter("clearProperties", clearProperties);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteSource(Integer sourceId, boolean force) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__DELETE_SOURCE);
		request.setParameter("id", sourceId);
		request.setParameter("force", force);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ClientModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteSources(Integer[] sourceIds, boolean force) throws ClientException {
		checkNullParameter(sourceIds, "Source Ids are null.");
		checkEmptyArrayParameter(sourceIds, "Source ids are empty.");

		Request request = new Request(RequestConstants.SUBS_SERVER__DELETE_SOURCE);
		request.setParameter("ids", sourceIds);
		request.setParameter("force", force);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ClientModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

	@Override
	public List<SubsTarget> getTargets() throws ClientException {
		List<SubsTarget> targets = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__LIST_TARGETS);

			response = sendRequest(request);
			if (response != null) {
				targets = ClientModelConverter.SUBS_SERVER.toTargets(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (targets == null) {
			targets = Collections.emptyList();
		}
		return targets;
	}

	@Override
	public List<SubsTarget> getTargets(String type) throws ClientException {
		checkNullParameter(type, "Target type is null.");

		List<SubsTarget> targets = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__LIST_TARGETS);
			request.setParameter("type", type);

			response = sendRequest(request);
			if (response != null) {
				targets = ClientModelConverter.SUBS_SERVER.toTargets(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (targets == null) {
			targets = Collections.emptyList();
		}
		return targets;
	}

	@Override
	public SubsTarget getTarget(Integer targetId) throws ClientException {
		checkNullParameter(targetId, "Source Id is null.");

		SubsTarget target = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__GET_TARGET);
			request.setParameter("id", targetId);

			response = sendRequest(request);
			if (response != null) {
				target = ClientModelConverter.SUBS_SERVER.toTarget(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return target;

	}

	@Override
	public SubsTarget getTarget(String type, String instanceId) throws ClientException {
		checkNullParameter(type, "Source type is null.");
		checkNullParameter(instanceId, "instanceId is null.");

		SubsTarget target = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__GET_TARGET);
			request.setParameter("type", type);
			request.setParameter("instanceId", instanceId);

			response = sendRequest(request);
			if (response != null) {
				target = ClientModelConverter.SUBS_SERVER.toTarget(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return target;
	}

	@Override
	public boolean targetExists(Integer targetId) throws ClientException {
		checkNullParameter(targetId, "Source Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__TARGET_EXISTS);
		request.setParameter("id", targetId);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ResponseUtil.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public boolean targetExists(String type, String instanceId) throws ClientException {
		checkNullParameter(type, "Target type is null.");
		checkNullParameter(instanceId, "Target instanceId is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__TARGET_EXISTS);
		request.setParameter("type", type);
		request.setParameter("instanceId", instanceId);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ResponseUtil.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public SubsTarget createTarget(String type, String instanceId, String name, String serverId, String serverURL, Map<String, Object> properties) throws ClientException {
		checkNullParameter(type, "Target type is null.");
		checkNullParameter(instanceId, "Target instanceId is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__CREATE_TARGET);
		request.setParameter("type", type);
		request.setParameter("instanceId", instanceId);
		if (name != null) {
			request.setParameter("name", name);
		}
		if (serverId != null) {
			request.setParameter("serverId", serverId);
		}
		if (serverURL != null) {
			request.setParameter("serverURL", serverURL);
		}
		if (properties != null) {
			request.setParameter("properties", properties);
		}

		SubsTarget target = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				target = ClientModelConverter.SUBS_SERVER.toTarget(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return target;
	}

	@Override
	public boolean updateTargetTypeAndInstanceId(Integer targetId, String type, String instanceId) throws ClientException {
		checkNullParameter(targetId, "Target Id is null.");
		checkNullParameter(type, "Target type is null.");
		checkNullParameter(instanceId, "Target instanceId is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_TARGET);
		request.setParameter("id", targetId);
		request.setParameter("type", type);
		request.setParameter("instanceId", instanceId);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateTargetType(Integer targetId, String type) throws ClientException {
		checkNullParameter(targetId, "Target Id is null.");
		checkNullParameter(type, "Target type is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_TARGET);
		request.setParameter("id", targetId);
		request.setParameter("type", type);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateTargetInstanceId(Integer targetId, String instanceId) throws ClientException {
		checkNullParameter(targetId, "Target Id is null.");
		checkNullParameter(instanceId, "Target instanceId is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_TARGET);
		request.setParameter("id", targetId);
		request.setParameter("instanceId", instanceId);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateTargetName(Integer targetId, String name) throws ClientException {
		checkNullParameter(targetId, "Target Id is null.");
		checkNullParameter(name, "targetId name is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_TARGET);
		request.setParameter("id", targetId);
		request.setParameter("name", name);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateTargetProperties(Integer targetId, Map<String, Object> properties, boolean clearProperties) throws ClientException {
		checkNullParameter(targetId, "Target Id is null.");

		if (properties == null) {
			properties = new HashMap<String, Object>();
		}

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_TARGET);
		request.setParameter("id", targetId);
		request.setParameter("properties", properties);
		request.setParameter("clearProperties", clearProperties);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateServerId(Integer targetId, String serverId) throws ClientException {
		checkNullParameter(targetId, "Target Id is null.");
		checkNullParameter(serverId, "target server Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_TARGET);
		request.setParameter("id", targetId);
		request.setParameter("serverId", serverId);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateServerURL(Integer targetId, String serverURL) throws ClientException {
		checkNullParameter(targetId, "Target Id is null.");
		checkNullParameter(serverURL, "target server URL is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_TARGET);
		request.setParameter("id", targetId);
		request.setParameter("serverURL", serverURL);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateServerHeartbeat(Integer targetId) throws ClientException {
		checkNullParameter(targetId, "Target Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_TARGET);
		request.setParameter("id", targetId);
		request.setParameter("serverHeartbeat", true);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteTarget(Integer targetId, boolean force) throws ClientException {
		checkNullParameter(targetId, "Target Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__DELETE_TARGET);
		request.setParameter("id", targetId);
		request.setParameter("force", force);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ClientModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteTargets(Integer[] targetIds, boolean force) throws ClientException {
		checkNullParameter(targetIds, "Target Ids are null.");
		checkEmptyArrayParameter(targetIds, "Target ids are empty.");

		Request request = new Request(RequestConstants.SUBS_SERVER__DELETE_TARGET);
		request.setParameter("ids", targetIds);
		request.setParameter("force", force);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ClientModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

	@Override
	public List<SubsMapping> getMappings() throws ClientException {
		List<SubsMapping> mappings = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__LIST_MAPPINGS);

			response = sendRequest(request);
			if (response != null) {
				mappings = ClientModelConverter.SUBS_SERVER.toMappings(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (mappings == null) {
			mappings = Collections.emptyList();
		}
		return mappings;
	}

	@Override
	public List<SubsMapping> getMappingsOfSource(Integer sourceId) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");

		List<SubsMapping> mappings = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__LIST_MAPPINGS);
			request.setParameter("sourceId", sourceId);

			response = sendRequest(request);
			if (response != null) {
				mappings = ClientModelConverter.SUBS_SERVER.toMappings(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (mappings == null) {
			mappings = Collections.emptyList();
		}
		return mappings;
	}

	@Override
	public List<SubsMapping> getMappingsOfTarget(Integer targetId) throws ClientException {
		checkNullParameter(targetId, "Target Id is null.");

		List<SubsMapping> mappings = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__LIST_MAPPINGS);
			request.setParameter("targetId", targetId);

			response = sendRequest(request);
			if (response != null) {
				mappings = ClientModelConverter.SUBS_SERVER.toMappings(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (mappings == null) {
			mappings = Collections.emptyList();
		}
		return mappings;
	}

	@Override
	public List<SubsMapping> getMappings(Integer sourceId, Integer targetId) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");
		checkNullParameter(targetId, "Target Id is null.");

		List<SubsMapping> mappings = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__LIST_MAPPINGS);
			request.setParameter("sourceId", sourceId);
			request.setParameter("targetId", targetId);

			response = sendRequest(request);
			if (response != null) {
				mappings = ClientModelConverter.SUBS_SERVER.toMappings(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (mappings == null) {
			mappings = Collections.emptyList();
		}
		return mappings;
	}

	@Override
	public SubsMapping getMapping(Integer mappingId) throws ClientException {
		checkNullParameter(mappingId, "Mapping Id is null.");

		SubsMapping mapping = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__GET_MAPPING);
			request.setParameter("id", mappingId);

			response = sendRequest(request);
			if (response != null) {
				mapping = ClientModelConverter.SUBS_SERVER.toMapping(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return mapping;
	}

	@Override
	public SubsMapping getMappingByClientId(Integer sourceId, Integer targetId, String clientId) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");
		checkNullParameter(targetId, "Target Id is null.");
		checkNullParameter(clientId, "Client Id is null.");

		SubsMapping mapping = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__GET_MAPPING);
			request.setParameter("sourceId", sourceId);
			request.setParameter("targetId", targetId);
			request.setParameter("clientId", clientId);

			response = sendRequest(request);
			if (response != null) {
				mapping = ClientModelConverter.SUBS_SERVER.toMapping(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return mapping;
	}

	@Override
	public SubsMapping getMappingByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");
		checkNullParameter(targetId, "Target Id is null.");
		checkNullParameter(clientURL, "Client URL is null.");

		SubsMapping mapping = null;
		Response response = null;
		try {
			Request request = new Request(RequestConstants.SUBS_SERVER__GET_MAPPING);
			request.setParameter("sourceId", sourceId);
			request.setParameter("targetId", targetId);
			request.setParameter("clientURL", clientURL);

			response = sendRequest(request);
			if (response != null) {
				mapping = ClientModelConverter.SUBS_SERVER.toMapping(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return mapping;
	}

	@Override
	public boolean mappingExists(Integer mappingId) throws ClientException {
		checkNullParameter(mappingId, "Mapping Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__MAPPING_EXISTS);
		request.setParameter("id", mappingId);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ResponseUtil.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public boolean mappingExistsByClientId(Integer sourceId, Integer targetId, String clientId) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");
		checkNullParameter(targetId, "Target Id is null.");
		checkNullParameter(clientId, "Client Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__MAPPING_EXISTS);
		request.setParameter("sourceId", sourceId);
		request.setParameter("targetId", targetId);
		request.setParameter("clientId", clientId);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ResponseUtil.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public boolean mappingExistsByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");
		checkNullParameter(targetId, "Target Id is null.");
		checkNullParameter(clientURL, "Client URL is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__MAPPING_EXISTS);
		request.setParameter("sourceId", sourceId);
		request.setParameter("targetId", targetId);
		request.setParameter("clientURL", clientURL);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ResponseUtil.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public SubsMapping createMapping(Integer sourceId, Integer targetId, String clientId, String clientURL, Map<String, Object> properties) throws ClientException {
		checkNullParameter(sourceId, "Source Id is null.");
		checkNullParameter(targetId, "Target Id is null.");
		checkNullParameter(clientId, "Client Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__CREATE_MAPPING);
		request.setParameter("sourceId", sourceId);
		request.setParameter("targetId", targetId);
		request.setParameter("clientId", clientId);
		if (clientURL != null) {
			request.setParameter("clientURL", clientURL);
		}
		if (properties != null) {
			request.setParameter("properties", properties);
		}

		SubsMapping mapping = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				mapping = ClientModelConverter.SUBS_SERVER.toMapping(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return mapping;
	}

	@Override
	public boolean updateMappingProperties(Integer mappingId, Map<String, Object> properties, boolean clearProperties) throws ClientException {
		checkNullParameter(mappingId, "Mapping Id is null.");

		if (properties == null) {
			properties = new HashMap<String, Object>();
		}

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_MAPPING);
		request.setParameter("id", mappingId);
		request.setParameter("properties", properties);
		request.setParameter("clearProperties", clearProperties);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateMappingClientId(Integer mappingId, String clientId) throws ClientException {
		checkNullParameter(mappingId, "Mapping Id is null.");
		checkNullParameter(clientId, "Mapping client Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_MAPPING);
		request.setParameter("id", mappingId);
		request.setParameter("clientId", clientId);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateMappingClientURL(Integer mappingId, String clientURL) throws ClientException {
		checkNullParameter(mappingId, "Mapping Id is null.");
		checkNullParameter(clientURL, "Mapping client URL is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_MAPPING);
		request.setParameter("id", mappingId);
		request.setParameter("clientURL", clientURL);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateMappingClientHeartbeat(Integer mappingId) throws ClientException {
		checkNullParameter(mappingId, "Mapping Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__UPDATE_MAPPING);
		request.setParameter("id", mappingId);
		request.setParameter("clientHeartbeat", true);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ClientModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteMapping(Integer mappingId) throws ClientException {
		checkNullParameter(mappingId, "Mapping Id is null.");

		Request request = new Request(RequestConstants.SUBS_SERVER__DELETE_MAPPING);
		request.setParameter("id", mappingId);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ClientModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteMappings(Integer[] mappingIds) throws ClientException {
		checkNullParameter(mappingIds, "Mapping Ids are null.");
		checkEmptyArrayParameter(mappingIds, "Mapping ids are empty.");

		Request request = new Request(RequestConstants.SUBS_SERVER__DELETE_MAPPING);
		request.setParameter("ids", mappingIds);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ClientModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

}
