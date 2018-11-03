package org.orbit.infra.runtime.datatube.service;

import org.origin.common.rest.editpolicy.EditPoliciesAware;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAware;

public interface DataTubeService extends WebServiceAware, EditPoliciesAware {

	String getDataCastId();

	String getDataTubeId();

	String getWebSocketHttpPort();

	RuntimeChannel[] getRuntimeChannels() throws ServerException;

	RuntimeChannel getRuntimeChannelById(String channelId) throws ServerException;

	RuntimeChannel getRuntimeChannelByName(String name) throws ServerException;

	boolean runtimeChannelExistsById(String channelId) throws ServerException;

	boolean runtimeChannelExistsByName(String name) throws ServerException;

	RuntimeChannel createRuntimeChannelById(String channelId) throws ServerException;

	RuntimeChannel createRuntimeChannelByName(String name) throws ServerException;

	boolean syncChannelMetadataById(String channelId) throws ServerException;

	boolean syncChannelMetadataByName(String name) throws ServerException;

	boolean removeRuntimeChannelById(String channelId) throws ServerException;

	boolean removeRuntimeChannelByName(String name) throws ServerException;

	void disposeRuntimeChannels() throws ServerException;

}
