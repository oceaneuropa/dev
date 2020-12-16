package org.orbit.infra.runtime.subs;

import java.util.List;
import java.util.Map;

import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.SubsSource;
import org.orbit.infra.model.subs.SubsTarget;
import org.origin.common.rest.server.ServerException;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface SubServerService {

	// ------------------------------------------------------
	// Source
	// ------------------------------------------------------
	List<SubsSource> getSources() throws ServerException;

	List<SubsSource> getSources(String type) throws ServerException;

	SubsSource getSource(String sourceId) throws ServerException;

	SubsSource getSource(String type, String typeId) throws ServerException;

	SubsSource createSource(String name, String type, String typeId, Map<String, String> properties);

	boolean updateSourceName(Integer sourceId, String name) throws ServerException;

	boolean updateSourceType(Integer sourceId, String type, String typeId) throws ServerException;

	boolean updateSourceProperties(Integer sourceId, Map<String, String> properties) throws ServerException;

	boolean deleteSource(Integer... sourceId) throws ServerException;

	// ------------------------------------------------------
	// Target
	// ------------------------------------------------------
	List<SubsTarget> getTargets();

	List<SubsTarget> getTargets(String type);

	SubsTarget getTarget(String targetId) throws ServerException;

	SubsTarget getTarget(String type, String typeId) throws ServerException;

	SubsTarget createTarget(String name, String type, String typeId, String serverId, String serverURL, Map<String, String> properties);

	boolean updateTargetName(Integer targetId, String name) throws ServerException;

	boolean updateTargetType(Integer targetId, String type, String typeId) throws ServerException;

	boolean updateTargetProperties(Integer targetId, Map<String, String> properties) throws ServerException;

	boolean deleteTarget(Integer... targetId) throws ServerException;

	boolean updateServerURL(Integer targetId, String serverId, String serverURL) throws ServerException;

	boolean updateServerHeartbeat(Integer targetId) throws ServerException;

	// ------------------------------------------------------
	// Mapping
	// ------------------------------------------------------
	List<SubsMapping> getMappings();

	List<SubsMapping> getMappingsOfSource(Integer sourceId);

	List<SubsMapping> getMappingsOfTarget(Integer targetId);

	List<SubsMapping> getMappings(Integer sourceId, Integer targetId);

	SubsMapping getMapping(Integer mappingId);

	SubsMapping getMappingByClientId(Integer sourceId, Integer targetId, String clientId);

	SubsMapping getMappingByClientURL(Integer sourceId, Integer targetId, String clientURL);

	SubsMapping createMapping(Integer sourceId, Integer targetId, String clientId, String clientURL, Map<String, String> properties);

	boolean deleteMapping(Integer... mappingId);

	boolean updateClientURL(Integer mappingId, String clientId, String clientURL) throws ServerException;

	boolean updateClientHeartbeat(Integer mappingId) throws ServerException;

}
