package org.orbit.platform.connector.platform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.api.RequestConstants;
import org.orbit.platform.api.platform.PlatformClient;
import org.orbit.platform.model.platform.dto.PlatformModelConverter;
import org.orbit.platform.model.platform.dto.ServiceExtensionInfo;
import org.orbit.platform.model.platform.dto.ServiceExtensionInfoDTO;
import org.orbit.platform.model.platform.dto.ServiceInstanceInfo;
import org.orbit.platform.model.platform.dto.ServiceInstanceInfoDTO;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.util.ResponseUtil;

public class PlatformClientImpl extends ServiceClientImpl<PlatformClient, PlatformWSClient> implements PlatformClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public PlatformClientImpl(ServiceConnector<PlatformClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected PlatformWSClient createWSClient(Map<String, Object> properties) {
		String realm = (String) properties.get(PlatformConstants.REALM);
		String username = (String) properties.get(PlatformConstants.USERNAME);
		String fullUrl = (String) properties.get(PlatformConstants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		return new PlatformWSClient(config);
	}

	@Override
	public String getURL() {
		String fullUrl = (String) this.properties.get(PlatformConstants.URL);
		return fullUrl;
	}

	@Override
	public List<String> getExtensionTypeIds() throws ClientException {
		List<String> extensionTypeIds = this.client.getExtensionTypeIds();
		return extensionTypeIds;
	}

	@Override
	public List<ServiceExtensionInfo> getServiceExtensions(String extensionTypeId) throws ClientException {
		List<ServiceExtensionInfoDTO> serviceExtensionInfoDTOs = this.client.getServiceExtensions(extensionTypeId);
		List<ServiceExtensionInfo> serviceExtensionInfos = PlatformModelConverter.INSTANCE.toServiceExtensions(serviceExtensionInfoDTOs);
		return serviceExtensionInfos;
	}

	@Override
	public ServiceExtensionInfo getServiceExtension(String extensionTypeId, String extensionId) throws ClientException {
		ServiceExtensionInfo serviceExtensionInfo = null;
		ServiceExtensionInfoDTO serviceExtensionInfoDTO = this.client.getServiceExtension(extensionTypeId, extensionId);
		if (serviceExtensionInfoDTO != null) {
			serviceExtensionInfo = PlatformModelConverter.INSTANCE.toServiceExtensions(serviceExtensionInfoDTO);
		}
		return serviceExtensionInfo;
	}

	@Override
	public List<ServiceInstanceInfo> getServiceInstances(String extensionTypeId) throws ClientException {
		List<ServiceInstanceInfoDTO> serviceInstanceInfoDTOs = this.client.getServiceInstances(extensionTypeId);
		List<ServiceInstanceInfo> serviceInstanceInfos = PlatformModelConverter.INSTANCE.toServiceInstances(serviceInstanceInfoDTOs);
		return serviceInstanceInfos;
	}

	@Override
	public ServiceInstanceInfo getServiceInstance(String extensionTypeId, String extensionId) throws ClientException {
		ServiceInstanceInfo serviceInstanceInfo = null;
		ServiceInstanceInfoDTO serviceInstanceInfoDTO = this.client.getServiceInstance(extensionTypeId, extensionId);
		if (serviceInstanceInfoDTO != null) {
			serviceInstanceInfo = PlatformModelConverter.INSTANCE.toServiceInstance(serviceInstanceInfoDTO);
		}
		return serviceInstanceInfo;
	}

	@Override
	public boolean startService(String extensionTypeId, String extensionId, Map<String, Object> parameters) throws ClientException {
		StatusDTO statusDTO = null;
		Response response = null;
		try {
			Request request = new Request();
			request.setRequestName(RequestConstants.START_SERVICE);
			if (parameters == null) {
				parameters = new HashMap<String, Object>();
			}
			parameters.put("extensionTypeId", extensionTypeId);
			parameters.put("extensionId", extensionId);
			request.setParameters(parameters);

			response = this.client.sendRequest(request);

			statusDTO = response.readEntity(StatusDTO.class);

		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (statusDTO != null && statusDTO.success()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean stopService(String extensionTypeId, String extensionId, Map<String, Object> parameters) throws ClientException {
		StatusDTO statusDTO = null;
		Response response = null;
		try {
			Request request = new Request();
			request.setRequestName(RequestConstants.STOP_SERVICE);
			if (parameters == null) {
				parameters = new HashMap<String, Object>();
			}
			parameters.put("extensionTypeId", extensionTypeId);
			parameters.put("extensionId", extensionId);
			request.setParameters(parameters);

			response = this.client.sendRequest(request);

			statusDTO = response.readEntity(StatusDTO.class);

		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (statusDTO != null && statusDTO.success()) {
			return true;
		}
		return false;
	}

}

// @Override
// public boolean startService(String extensionTypeId, String extensionId, Map<String, Object> properties) throws ClientException {
// StatusDTO statusDTO = this.client.startService(extensionTypeId, extensionId, properties);
// if (statusDTO != null && statusDTO.success()) {
// return true;
// }
// return false;
// }
//
// @Override
// public boolean stopService(String extensionTypeId, String extensionId, Map<String, Object> properties) throws ClientException {
// StatusDTO statusDTO = this.client.stopService(extensionTypeId, extensionId, properties);
// if (statusDTO != null && statusDTO.success()) {
// return true;
// }
// return false;
// }
