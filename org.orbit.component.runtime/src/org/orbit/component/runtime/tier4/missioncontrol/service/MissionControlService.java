package org.orbit.component.runtime.tier4.missioncontrol.service;

import java.util.List;

import org.orbit.component.model.tier4.mission.MissionRTO;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAware;

public interface MissionControlService extends WebServiceAware {

	WSEditPolicies getEditPolicies();

	String getName();

	List<MissionRTO> getMissions(String typeId) throws ServerException;

	MissionRTO getMission(String typeId, String name) throws ServerException;

	MissionRTO createMission(String typeId, String name) throws ServerException;

	boolean deleteMission(String typeId, String name) throws ServerException;

	boolean startMission(String typeId, String name) throws ServerException;

	boolean stopMission(String typeId, String name) throws ServerException;

}
