package org.orbit.infra.runtime.datatube.service;

import java.util.Map;

import org.origin.common.rest.editpolicy.EditPoliciesAware;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAware;

public interface DataTubeService extends WebServiceAware, EditPoliciesAware {

	Map<Object, Object> getInitProperties();

	String getDataCastId();

	String getDataTubeId();

	String getWebSocketPort();

	RuntimeChannel[] getRuntimeChannels() throws ServerException;

	RuntimeChannel getRuntimeChannelById(String channelId) throws ServerException;

	RuntimeChannel getRuntimeChannelByName(String name) throws ServerException;

	boolean runtimeChannelExistsById(String channelId) throws ServerException;

	boolean runtimeChannelExistsByName(String name) throws ServerException;

	RuntimeChannel createRuntimeChannelById(String channelId) throws ServerException;

	RuntimeChannel createRuntimeChannelByName(String name) throws ServerException;

	boolean syncRuntimeChannelById(String channelId) throws ServerException;

	boolean syncRuntimeChannelByName(String name) throws ServerException;

	boolean startRuntimeChannelById(String channelId) throws ServerException;

	boolean startRuntimeChannelByName(String name) throws ServerException;

	boolean suspendRuntimeChannelById(String channelId) throws ServerException;

	boolean suspendRuntimeChannelByName(String name) throws ServerException;

	boolean stopRuntimeChannelById(String channelId) throws ServerException;

	boolean stopRuntimeChannelByName(String name) throws ServerException;

	boolean removeRuntimeChannelById(String channelId) throws ServerException;

	boolean removeRuntimeChannelByName(String name) throws ServerException;

	void disposeRuntimeChannels() throws ServerException;

}
