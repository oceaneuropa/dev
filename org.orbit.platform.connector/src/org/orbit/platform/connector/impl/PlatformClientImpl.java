package org.orbit.platform.connector.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.model.dto.ExtensionDTO;
import org.orbit.platform.model.dto.ExtensionInfo;
import org.orbit.platform.model.dto.ProcessDTO;
import org.orbit.platform.model.dto.ProcessInfo;
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
	public List<ExtensionInfo> getExtensions() throws ClientException {
		List<ExtensionDTO> extensionDTOs = this.client.getExtensions(null);
		List<ExtensionInfo> extensionInfos = ModelConverter.INSTANCE.toExtensionInfos(extensionDTOs);
		return extensionInfos;
	}

	@Override
	public List<ExtensionInfo> getExtensions(String extensionTypeId) throws ClientException {
		List<ExtensionDTO> extensionDTOs = this.client.getExtensions(extensionTypeId);
		List<ExtensionInfo> extensionInfos = ModelConverter.INSTANCE.toExtensionInfos(extensionDTOs);
		return extensionInfos;
	}

	@Override
	public ExtensionInfo getExtension(String extensionTypeId, String extensionId) throws ClientException {
		ExtensionInfo extensionInfo = null;
		ExtensionDTO extensionDTO = this.client.getExtension(extensionTypeId, extensionId);
		if (extensionDTO != null) {
			extensionInfo = ModelConverter.INSTANCE.toExtensionInfo(extensionDTO);
		}
		return extensionInfo;
	}

	@Override
	public List<ProcessInfo> getProcesses() throws ClientException {
		List<ProcessDTO> processDTOs = this.client.getProcesses(null, null);
		List<ProcessInfo> processInfos = ModelConverter.INSTANCE.toProcessInfos(processDTOs);
		return processInfos;
	}

	@Override
	public List<ProcessInfo> getProcesses(String extensionTypeId) throws ClientException {
		List<ProcessDTO> processDTOs = this.client.getProcesses(extensionTypeId, null);
		List<ProcessInfo> processInfos = ModelConverter.INSTANCE.toProcessInfos(processDTOs);
		return processInfos;
	}

	@Override
	public List<ProcessInfo> getProcesses(String extensionTypeId, String extensionId) throws ClientException {
		List<ProcessDTO> processDTOs = this.client.getProcesses(extensionTypeId, extensionId);
		List<ProcessInfo> processInfos = ModelConverter.INSTANCE.toProcessInfos(processDTOs);
		return processInfos;
	}

	@Override
	public ProcessInfo startService(String extensionTypeId, String extensionId, Map<String, Object> parameters) throws ClientException {
		ProcessInfo processInfo = null;
		Response response = null;
		try {
			Request request = new Request();
			request.setRequestName(PlatformConstants.START_SERVICE);
			if (parameters == null) {
				parameters = new HashMap<String, Object>();
			}
			parameters.put("extensionTypeId", extensionTypeId);
			parameters.put("extensionId", extensionId);
			request.setParameters(parameters);

			response = this.client.sendRequest(request);

			ProcessDTO processDTO = response.readEntity(ProcessDTO.class);
			if (processDTO != null) {
				processInfo = ModelConverter.INSTANCE.toProcessInfo(processDTO);
			}

		} finally {
			ResponseUtil.closeQuietly(response, true);
		}

		return processInfo;
	}

	@Override
	public boolean stopService(int pid) throws ClientException {
		StatusDTO statusDTO = null;
		Response response = null;
		try {
			Request request = new Request();
			request.setRequestName(PlatformConstants.STOP_SERVICE);

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("pid", pid);

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
	public void shutdown(long timeout, boolean force) throws ClientException {
		Request request = new Request();
		request.setParameter("timeout", timeout);
		request.setParameter("force", force);
		request.setRequestName(PlatformConstants.SHUTDOWN_PLATFORM);

		Response response = null;
		try {
			response = this.client.sendRequest(request);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

}

// @Override
// public List<String> getExtensionTypeIds() throws ClientException {
// List<String> extensionTypeIds = this.client.getExtensionTypeIds();
// return extensionTypeIds;
// }

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

// @Override
// public boolean startService(String extensionTypeId, String extensionId, Map<String, Object> parameters) throws ClientException {
// StatusDTO statusDTO = null;
// Response response = null;
// try {
// Request request = new Request();
// request.setRequestName(Constants.START_SERVICE);
// if (parameters == null) {
// parameters = new HashMap<String, Object>();
// }
// parameters.put("extensionTypeId", extensionTypeId);
// parameters.put("extensionId", extensionId);
// request.setParameters(parameters);
//
// response = this.client.sendRequest(request);
//
// statusDTO = response.readEntity(StatusDTO.class);
//
// } finally {
// ResponseUtil.closeQuietly(response, true);
// }
// if (statusDTO != null && statusDTO.success()) {
// return true;
// }
// return false;
// }

// @Override
// public boolean stopService(String extensionTypeId, String extensionId, Map<String, Object> parameters) throws ClientException {
// StatusDTO statusDTO = null;
// Response response = null;
// try {
// Request request = new Request();
// request.setRequestName(Constants.STOP_SERVICE);
// if (parameters == null) {
// parameters = new HashMap<String, Object>();
// }
// parameters.put("extensionTypeId", extensionTypeId);
// parameters.put("extensionId", extensionId);
// request.setParameters(parameters);
//
// response = this.client.sendRequest(request);
//
// statusDTO = response.readEntity(StatusDTO.class);
//
// } finally {
// ResponseUtil.closeQuietly(response, true);
// }
// if (statusDTO != null && statusDTO.success()) {
// return true;
// }
// return false;
// }
