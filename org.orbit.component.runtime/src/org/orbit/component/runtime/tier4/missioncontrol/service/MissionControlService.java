package org.orbit.component.runtime.tier4.missioncontrol.service;

import java.util.List;

import org.orbit.component.runtime.model.missioncontrol.Mission;
import org.origin.common.jdbc.ConnectionProvider;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenProvider;
import org.origin.common.service.IWebService;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface MissionControlService extends IWebService, ConnectionProvider, AccessTokenProvider {

	List<Mission> getMissions(String typeId) throws ServerException;

	Mission getMission(String typeId, String name) throws ServerException;

	Mission createMission(String typeId, String name) throws ServerException;

	boolean startMission(String typeId, String name) throws ServerException;

	boolean stopMission(String typeId, String name) throws ServerException;

	boolean deleteMission(String typeId, String name) throws ServerException;

}
