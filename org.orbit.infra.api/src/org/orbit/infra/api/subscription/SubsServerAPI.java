package org.orbit.infra.api.subscription;

import java.util.List;
import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;
import org.origin.common.service.Proxy;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface SubsServerAPI extends ServiceClient, Proxy, IAdaptable {

	// ------------------------------------------------------
	// Source Types
	// ------------------------------------------------------
	List<ISubsType> getSourceTypes() throws ClientException;

	boolean sourceTypeExists(String type) throws ClientException;

	ISubsType createSourceType(String type, String name) throws ClientException;

	boolean updateSourceTypeType(Integer id, String type) throws ClientException;

	boolean updateSourceTypeName(Integer id, String name) throws ClientException;

	boolean updateSourceTypeName(String type, String name) throws ClientException;

	boolean deleteSourceType(Integer id, boolean force) throws ClientException;

	boolean deleteSourceType(String type, boolean force) throws ClientException;

	// ------------------------------------------------------
	// Target Types
	// ------------------------------------------------------
	List<ISubsType> getTargetTypes() throws ClientException;

	boolean targetTypeExists(String type) throws ClientException;

	ISubsType createTargeteType(String type, String name) throws ClientException;

	boolean updateTargetTypeType(Integer id, String type) throws ClientException;

	boolean updateTargetTypeName(Integer id, String name) throws ClientException;

	boolean updateTargetTypeName(String type, String name) throws ClientException;

	boolean deleteTargetType(Integer id, boolean force) throws ClientException;

	boolean deleteTargetType(String type, boolean force) throws ClientException;

	// ------------------------------------------------------
	// Sources
	// ------------------------------------------------------
	List<ISubsSource> getSources() throws ClientException;

	List<ISubsSource> getSources(String type) throws ClientException;

	ISubsSource getSource(Integer sourceId) throws ClientException;

	ISubsSource getSource(String type, String instanceId) throws ClientException;

	boolean sourceExists(Integer sourceId) throws ClientException;

	boolean sourceExists(String type, String instanceId) throws ClientException;

	ISubsSource createSource(String type, String instanceId, String name, Map<String, Object> properties) throws ClientException;

	boolean updateSourceTypeAndInstanceId(Integer sourceId, String type, String instanceId) throws ClientException;

	boolean updateSourceType(Integer sourceId, String type) throws ClientException;

	boolean updateSourceInstanceId(Integer sourceId, String instanceId) throws ClientException;

	boolean updateSourceName(Integer sourceId, String name) throws ClientException;

	boolean updateSourceProperties(Integer sourceId, Map<String, Object> properties, boolean clearProperties) throws ClientException;

	boolean deleteSource(Integer sourceId, boolean force) throws ClientException;

	boolean deleteSource(String type, String instanceId, boolean force) throws ClientException;

	boolean deleteSources(Integer[] sourceIds, boolean force) throws ClientException;

	// ------------------------------------------------------
	// Targets
	// ------------------------------------------------------
	List<ISubsTarget> getTargets() throws ClientException;

	List<ISubsTarget> getTargets(String type) throws ClientException;

	ISubsTarget getTarget(Integer targetId) throws ClientException;

	ISubsTarget getTarget(String type, String instanceId) throws ClientException;

	boolean targetExists(Integer targetId) throws ClientException;

	boolean targetExists(String type, String instanceId) throws ClientException;

	ISubsTarget createTarget(String type, String instanceId, String name, String serverId, String serverURL, Map<String, Object> properties) throws ClientException;

	boolean updateTargetTypeAndInstanceId(Integer targetId, String type, String instanceId) throws ClientException;

	boolean updateTargetType(Integer targetId, String type) throws ClientException;

	boolean updateTargetInstanceId(Integer targetId, String instanceId) throws ClientException;

	boolean updateTargetName(Integer targetId, String name) throws ClientException;

	boolean updateTargetProperties(Integer targetId, Map<String, Object> properties, boolean clearProperties) throws ClientException;

	boolean updateServerId(Integer targetId, String serverId) throws ClientException;

	boolean updateServerURL(Integer targetId, String serverURL) throws ClientException;

	boolean updateServerHeartbeat(Integer targetId) throws ClientException;

	boolean deleteTarget(Integer targetId, boolean force) throws ClientException;

	boolean deleteTarget(String type, String instanceId, boolean force) throws ClientException;

	boolean deleteTargets(Integer[] targetIds, boolean force) throws ClientException;

	// ------------------------------------------------------
	// Mappings
	// ------------------------------------------------------
	List<ISubsMapping> getMappings() throws ClientException;

	List<ISubsMapping> getMappingsOfSource(Integer sourceId) throws ClientException;

	List<ISubsMapping> getMappingsOfSource(Integer sourceId, String targetType) throws ClientException;

	List<ISubsMapping> getMappingsOfSource(Integer sourceId, String targetType, String targetInstanceId) throws ClientException;

	List<ISubsMapping> getMappingsOfTarget(Integer targetId) throws ClientException;

	List<ISubsMapping> getMappingsOfTarget(Integer targetId, String sourceType) throws ClientException;

	List<ISubsMapping> getMappingsOfTarget(Integer targetId, String sourceType, String sourceInstanceId) throws ClientException;

	List<ISubsMapping> getMappings(Integer sourceId, Integer targetId) throws ClientException;

	ISubsMapping getMapping(Integer mappingId) throws ClientException;

	ISubsMapping getMappingByClientId(Integer sourceId, Integer targetId, String clientId) throws ClientException;

	ISubsMapping getMappingByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ClientException;

	boolean mappingExists(Integer mappingId) throws ClientException;

	boolean mappingExistsByClientId(Integer sourceId, Integer targetId, String clientId) throws ClientException;

	boolean mappingExistsByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ClientException;

	ISubsMapping createMapping(Integer sourceId, Integer targetId, String clientId, String clientURL, Map<String, Object> properties) throws ClientException;

	boolean updateMappingProperties(Integer mappingId, Map<String, Object> properties, boolean clearProperties) throws ClientException;

	boolean updateMappingClientId(Integer mappingId, String clientId) throws ClientException;

	boolean updateMappingClientURL(Integer mappingId, String clientURL) throws ClientException;

	boolean updateMappingClientHeartbeat(Integer mappingId) throws ClientException;

	boolean deleteMapping(Integer mappingId) throws ClientException;

	boolean deleteMappings(Integer[] mappingIds) throws ClientException;

	// ------------------------------------------------------
	// Mappings 2
	// ------------------------------------------------------
	List<ISubsType> getMappingTargetTypesOfSource(Integer sourceId) throws ClientException;

	List<ISubsType> getMappingSourceTypesOfTarget(Integer targetId) throws ClientException;

}
