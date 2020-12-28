package org.orbit.infra.runtime.subscription;

import java.util.List;
import java.util.Map;

import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.SubsSource;
import org.orbit.infra.model.subs.SubsTarget;
import org.orbit.infra.model.subs.SubsType;
import org.origin.common.jdbc.ConnectionProvider;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenProvider;
import org.origin.common.service.IWebService;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface SubsServerService extends IWebService, ConnectionProvider, AccessTokenProvider {

	// ------------------------------------------------------
	// Source Types
	// ------------------------------------------------------
	List<SubsType> getSourceTypes() throws ServerException;

	SubsType getSourceType(Integer id) throws ServerException;

	SubsType getSourceType(String type) throws ServerException;

	boolean sourceTypeExists(String type) throws ServerException;

	SubsType createSourceType(String type, String name) throws ServerException;

	boolean updateSourceTypeType(Integer id, String type) throws ServerException;

	boolean updateSourceTypeName(Integer id, String name) throws ServerException;

	boolean updateSourceTypeName(String type, String name) throws ServerException;

	boolean deleteSourceType(Integer id) throws ServerException;

	boolean deleteSourceType(String type) throws ServerException;

	// ------------------------------------------------------
	// Target Types
	// ------------------------------------------------------
	List<SubsType> getTargetTypes() throws ServerException;

	SubsType getTargetType(Integer id) throws ServerException;

	SubsType getTargetType(String type) throws ServerException;

	boolean targetTypeExists(String type) throws ServerException;

	SubsType createTargetType(String type, String name) throws ServerException;

	boolean updateTargetTypeType(Integer id, String type) throws ServerException;

	boolean updateTargetTypeName(Integer id, String name) throws ServerException;

	boolean updateTargetTypeName(String type, String name) throws ServerException;

	boolean deleteTargetType(Integer id) throws ServerException;

	boolean deleteTargetType(String type) throws ServerException;

	// ------------------------------------------------------
	// Sources
	// ------------------------------------------------------
	List<SubsSource> getSources() throws ServerException;

	List<SubsSource> getSources(String type) throws ServerException;

	SubsSource getSource(Integer sourceId) throws ServerException;

	SubsSource getSource(String type, String instanceId) throws ServerException;

	boolean sourceExists(String type) throws ServerException;

	boolean sourceExists(Integer sourceId) throws ServerException;

	boolean sourceExists(String type, String instanceId) throws ServerException;

	SubsSource createSource(String type, String instanceId, String name, Map<String, Object> properties, boolean createType) throws ServerException;

	boolean updateSourceType(Integer sourceId, String type, boolean createType) throws ServerException;

	boolean updateSourceInstanceId(Integer sourceId, String instanceId) throws ServerException;

	boolean updateSourceName(Integer sourceId, String name) throws ServerException;

	boolean updateSourceProperties(Integer sourceId, Map<String, Object> properties, boolean clearProperties) throws ServerException;

	boolean deleteSource(Integer sourceId) throws ServerException;

	boolean deleteSources(Integer[] sourceIds) throws ServerException;

	// ------------------------------------------------------
	// Targets
	// ------------------------------------------------------
	List<SubsTarget> getTargets() throws ServerException;

	List<SubsTarget> getTargets(String type) throws ServerException;

	SubsTarget getTarget(Integer targetId) throws ServerException;

	SubsTarget getTarget(String type, String instanceId) throws ServerException;

	boolean targetExists(String type) throws ServerException;

	boolean targetExists(Integer targetId) throws ServerException;

	boolean targetExists(String type, String instanceId) throws ServerException;

	SubsTarget createTarget(String type, String instanceId, String name, String serverId, String serverURL, Map<String, Object> properties, boolean createType) throws ServerException;

	boolean updateTargetType(Integer targetId, String type, boolean createType) throws ServerException;

	boolean updateTargetInstanceId(Integer targetId, String instanceId) throws ServerException;

	boolean updateTargetName(Integer targetId, String name) throws ServerException;

	boolean updateTargetProperties(Integer targetId, Map<String, Object> properties, boolean clearProperties) throws ServerException;

	boolean updateServerId(Integer targetId, String serverId) throws ServerException;

	boolean updateServerURL(Integer targetId, String serverURL) throws ServerException;

	boolean updateServerHeartbeat(Integer targetId) throws ServerException;

	boolean deleteTarget(Integer targetId) throws ServerException;

	boolean deleteTargets(Integer[] targetIds) throws ServerException;

	// ------------------------------------------------------
	// Mappings
	// ------------------------------------------------------
	List<SubsMapping> getMappings() throws ServerException;

	List<SubsMapping> getMappingsOfSource(Integer sourceId) throws ServerException;

	List<SubsMapping> getMappingsOfSource(Integer sourceId, String targetType) throws ServerException;

	List<SubsMapping> getMappingsOfSource(Integer sourceId, String targetType, String targetInstanceId) throws ServerException;

	List<SubsMapping> getMappingsOfTarget(Integer targetId) throws ServerException;

	List<SubsMapping> getMappingsOfTarget(Integer targetId, String sourceType) throws ServerException;

	List<SubsMapping> getMappingsOfTarget(Integer targetId, String sourceType, String sourceInstanceId) throws ServerException;

	List<SubsMapping> getMappings(Integer sourceId, Integer targetId) throws ServerException;

	SubsMapping getMapping(Integer mappingId) throws ServerException;

	SubsMapping getMappingByClientId(Integer sourceId, Integer targetId, String clientId) throws ServerException;

	SubsMapping getMappingByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ServerException;

	boolean mappingExists(Integer mappingId) throws ServerException;

	boolean mappingExistsByClientId(Integer sourceId, Integer targetId, String clientId) throws ServerException;

	boolean mappingExistsByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ServerException;

	SubsMapping createMapping(Integer sourceId, Integer targetId, String clientId, String clientURL, Map<String, Object> properties) throws ServerException;

	boolean updateMappingProperties(Integer mappingId, Map<String, Object> properties, boolean clearProperties) throws ServerException;

	boolean updateMappingClientId(Integer mappingId, String clientId) throws ServerException;

	boolean updateMappingClientURL(Integer mappingId, String clientURL) throws ServerException;

	boolean updateMappingClientHeartbeat(Integer mappingId) throws ServerException;

	boolean deleteMapping(Integer mappingId) throws ServerException;

	boolean deleteMappings(Integer[] mappingIds) throws ServerException;

}
