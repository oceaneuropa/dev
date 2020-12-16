package org.orbit.infra.runtime.subs.impl;

import java.util.List;
import java.util.Map;

import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.SubsSource;
import org.orbit.infra.model.subs.SubsTarget;
import org.orbit.infra.runtime.subs.SubServerService;
import org.origin.common.rest.server.ServerException;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubServerServiceImpl implements SubServerService {

	@Override
	public List<SubsSource> getSources() throws ServerException {
		return null;
	}

	@Override
	public List<SubsSource> getSources(String type) throws ServerException {
		return null;
	}

	@Override
	public SubsSource getSource(String sourceId) throws ServerException {
		return null;
	}

	@Override
	public SubsSource getSource(String type, String typeId) throws ServerException {
		return null;
	}

	@Override
	public SubsSource createSource(String name, String type, String typeId, Map<String, String> properties) {
		return null;
	}

	@Override
	public boolean updateSourceName(Integer sourceId, String name) throws ServerException {
		return false;
	}

	@Override
	public boolean updateSourceType(Integer sourceId, String type, String typeId) throws ServerException {
		return false;
	}

	@Override
	public boolean updateSourceProperties(Integer sourceId, Map<String, String> properties) throws ServerException {
		return false;
	}

	@Override
	public boolean deleteSource(Integer... sourceId) throws ServerException {
		return false;
	}

	@Override
	public List<SubsTarget> getTargets() {
		return null;
	}

	@Override
	public List<SubsTarget> getTargets(String type) {
		return null;
	}

	@Override
	public SubsTarget getTarget(String targetId) throws ServerException {
		return null;
	}

	@Override
	public SubsTarget getTarget(String type, String typeId) throws ServerException {
		return null;
	}

	@Override
	public SubsTarget createTarget(String name, String type, String typeId, String serverId, String serverURL, Map<String, String> properties) {
		return null;
	}

	@Override
	public boolean updateTargetName(Integer targetId, String name) throws ServerException {
		return false;
	}

	@Override
	public boolean updateTargetType(Integer targetId, String type, String typeId) throws ServerException {
		return false;
	}

	@Override
	public boolean updateTargetProperties(Integer targetId, Map<String, String> properties) throws ServerException {
		return false;
	}

	@Override
	public boolean deleteTarget(Integer... targetId) throws ServerException {
		return false;
	}

	@Override
	public boolean updateServerURL(Integer targetId, String serverId, String serverURL) throws ServerException {
		return false;
	}

	@Override
	public boolean updateServerHeartbeat(Integer targetId) throws ServerException {
		return false;
	}

	@Override
	public List<SubsMapping> getMappings() {
		return null;
	}

	@Override
	public List<SubsMapping> getMappingsOfSource(Integer sourceId) {
		return null;
	}

	@Override
	public List<SubsMapping> getMappingsOfTarget(Integer targetId) {
		return null;
	}

	@Override
	public List<SubsMapping> getMappings(Integer sourceId, Integer targetId) {
		return null;
	}

	@Override
	public SubsMapping getMapping(Integer mappingId) {
		return null;
	}

	@Override
	public SubsMapping getMappingByClientId(Integer sourceId, Integer targetId, String clientId) {
		return null;
	}

	@Override
	public SubsMapping getMappingByClientURL(Integer sourceId, Integer targetId, String clientURL) {
		return null;
	}

	@Override
	public SubsMapping createMapping(Integer sourceId, Integer targetId, String clientId, String clientURL, Map<String, String> properties) {
		return null;
	}

	@Override
	public boolean deleteMapping(Integer... mappingId) {
		return false;
	}

	@Override
	public boolean updateClientURL(Integer mappingId, String clientId, String clientURL) throws ServerException {
		return false;
	}

	@Override
	public boolean updateClientHeartbeat(Integer mappingId) throws ServerException {
		return false;
	}

}
