package org.orbit.infra.runtime.subs;

import java.util.List;
import java.util.Map;

import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.SubsSource;
import org.orbit.infra.model.subs.SubsTarget;
import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAware;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenAware;
import org.origin.common.service.WebServiceAware;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface SubsServerService extends WebServiceAware, ConnectionAware, EditPoliciesAware, AccessTokenAware {

	// ------------------------------------------------------
	// Sources
	// ------------------------------------------------------
	List<SubsSource> getSources() throws ServerException;

	List<SubsSource> getSources(String type) throws ServerException;

	SubsSource getSource(Integer sourceId) throws ServerException;

	SubsSource getSource(String type, String typeId) throws ServerException;

	boolean sourceExists(Integer sourceId) throws ServerException;

	boolean sourceExists(String type, String typeId) throws ServerException;

	SubsSource createSource(String type, String typeId, String name, Map<String, Object> properties) throws ServerException;

	boolean updateSourceName(Integer sourceId, String name) throws ServerException;

	boolean updateSourceType(Integer sourceId, String type, String typeId) throws ServerException;

	boolean updateSourceProperties(Integer sourceId, Map<String, Object> properties) throws ServerException;

	boolean deleteSource(Integer sourceId) throws ServerException;

	boolean deleteSource(Integer[] sourceIds) throws ServerException;

	// ------------------------------------------------------
	// Targets
	// ------------------------------------------------------
	List<SubsTarget> getTargets() throws ServerException;

	List<SubsTarget> getTargets(String type) throws ServerException;

	SubsTarget getTarget(Integer targetId) throws ServerException;

	SubsTarget getTarget(String type, String typeId) throws ServerException;

	boolean targetExists(Integer targetId) throws ServerException;

	boolean targetExists(String type, String typeId) throws ServerException;

	SubsTarget createTarget(String type, String typeId, String name, String serverId, String serverURL, Map<String, Object> properties) throws ServerException;

	boolean updateTargetName(Integer targetId, String name) throws ServerException;

	boolean updateTargetType(Integer targetId, String type, String typeId) throws ServerException;

	boolean updateTargetProperties(Integer targetId, Map<String, Object> properties) throws ServerException;

	boolean updateServerURL(Integer targetId, String serverId, String serverURL) throws ServerException;

	boolean updateServerHeartbeat(Integer targetId) throws ServerException;

	boolean deleteTarget(Integer targetId) throws ServerException;

	boolean deleteTarget(Integer[] targetIds) throws ServerException;

	// ------------------------------------------------------
	// Mappings
	// ------------------------------------------------------
	List<SubsMapping> getMappings() throws ServerException;

	List<SubsMapping> getMappingsOfSource(Integer sourceId) throws ServerException;

	List<SubsMapping> getMappingsOfTarget(Integer targetId) throws ServerException;

	List<SubsMapping> getMappings(Integer sourceId, Integer targetId) throws ServerException;

	SubsMapping getMapping(Integer mappingId) throws ServerException;

	SubsMapping getMappingByClientId(Integer sourceId, Integer targetId, String clientId) throws ServerException;

	SubsMapping getMappingByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ServerException;

	boolean mappingExists(Integer mappingId) throws ServerException;

	boolean mappingExistsByClientId(Integer sourceId, Integer targetId, String clientId) throws ServerException;

	boolean mappingExistsByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ServerException;

	SubsMapping createMapping(Integer sourceId, Integer targetId, String clientId, String clientURL, Map<String, Object> properties) throws ServerException;

	boolean updateClientURL(Integer mappingId, String clientId, String clientURL) throws ServerException;

	boolean updateClientHeartbeat(Integer mappingId) throws ServerException;

	boolean deleteMapping(Integer mappingId) throws ServerException;

	boolean deleteMapping(Integer[] mappingIds) throws ServerException;

}
