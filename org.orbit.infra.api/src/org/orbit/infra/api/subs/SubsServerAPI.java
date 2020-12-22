package org.orbit.infra.api.subs;

import java.util.List;
import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;
import org.origin.common.service.Proxy;

public interface SubsServerAPI extends ServiceClient, Proxy, IAdaptable {

	// ------------------------------------------------------
	// Sources
	// ------------------------------------------------------
	List<SubsSource> getSources() throws ClientException;

	List<SubsSource> getSources(String type) throws ClientException;

	SubsSource getSource(Integer sourceId) throws ClientException;

	SubsSource getSource(String type, String instanceId) throws ClientException;

	boolean sourceExists(Integer sourceId) throws ClientException;

	boolean sourceExists(String type, String instanceId) throws ClientException;

	SubsSource createSource(String type, String instanceId, String name, Map<String, Object> properties) throws ClientException;

	boolean updateSourceTypeAndInstanceId(Integer sourceId, String type, String instanceId) throws ClientException;

	boolean updateSourceType(Integer sourceId, String type) throws ClientException;

	boolean updateSourceInstanceId(Integer sourceId, String instanceId) throws ClientException;

	boolean updateSourceName(Integer sourceId, String name) throws ClientException;

	boolean updateSourceProperties(Integer sourceId, Map<String, Object> properties, boolean clearProperties) throws ClientException;

	boolean deleteSource(Integer sourceId, boolean force) throws ClientException;

	boolean deleteSources(Integer[] sourceIds, boolean force) throws ClientException;

	// ------------------------------------------------------
	// Targets
	// ------------------------------------------------------
	List<SubsTarget> getTargets() throws ClientException;

	List<SubsTarget> getTargets(String type) throws ClientException;

	SubsTarget getTarget(Integer targetId) throws ClientException;

	SubsTarget getTarget(String type, String instanceId) throws ClientException;

	boolean targetExists(Integer targetId) throws ClientException;

	boolean targetExists(String type, String instanceId) throws ClientException;

	SubsTarget createTarget(String type, String instanceId, String name, String serverId, String serverURL, Map<String, Object> properties) throws ClientException;

	boolean updateTargetTypeAndInstanceId(Integer targetId, String type, String instanceId) throws ClientException;

	boolean updateTargetType(Integer targetId, String type) throws ClientException;

	boolean updateTargetInstanceId(Integer targetId, String instanceId) throws ClientException;

	boolean updateTargetName(Integer targetId, String name) throws ClientException;

	boolean updateTargetProperties(Integer targetId, Map<String, Object> properties, boolean clearProperties) throws ClientException;

	boolean updateServerId(Integer targetId, String serverId) throws ClientException;

	boolean updateServerURL(Integer targetId, String serverURL) throws ClientException;

	boolean updateServerHeartbeat(Integer targetId) throws ClientException;

	boolean deleteTarget(Integer targetId, boolean force) throws ClientException;

	boolean deleteTargets(Integer[] targetIds, boolean force) throws ClientException;

	// ------------------------------------------------------
	// Mappings
	// ------------------------------------------------------
	List<SubsMapping> getMappings() throws ClientException;

	List<SubsMapping> getMappingsOfSource(Integer sourceId) throws ClientException;

	List<SubsMapping> getMappingsOfTarget(Integer targetId) throws ClientException;

	List<SubsMapping> getMappings(Integer sourceId, Integer targetId) throws ClientException;

	SubsMapping getMapping(Integer mappingId) throws ClientException;

	SubsMapping getMappingByClientId(Integer sourceId, Integer targetId, String clientId) throws ClientException;

	SubsMapping getMappingByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ClientException;

	boolean mappingExists(Integer mappingId) throws ClientException;

	boolean mappingExistsByClientId(Integer sourceId, Integer targetId, String clientId) throws ClientException;

	boolean mappingExistsByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ClientException;

	SubsMapping createMapping(Integer sourceId, Integer targetId, String clientId, String clientURL, Map<String, Object> properties) throws ClientException;

	boolean updateMappingProperties(Integer mappingId, Map<String, Object> properties, boolean clearProperties) throws ClientException;

	boolean updateMappingClientId(Integer mappingId, String clientId) throws ClientException;

	boolean updateMappingClientURL(Integer mappingId, String clientURL) throws ClientException;

	boolean updateMappingClientHeartbeat(Integer mappingId) throws ClientException;

	boolean deleteMapping(Integer mappingId) throws ClientException;

	boolean deleteMappings(Integer[] mappingIds) throws ClientException;

}
