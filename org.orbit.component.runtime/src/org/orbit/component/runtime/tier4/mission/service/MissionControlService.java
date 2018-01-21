package org.orbit.component.runtime.tier4.mission.service;

import java.util.List;

import org.orbit.component.model.tier4.mission.rto.Mission;
import org.orbit.component.model.tier4.mission.rto.MissionException;
import org.origin.common.rest.editpolicy.WSEditPolicies;

public interface MissionControlService {

	WSEditPolicies getEditPolicies();

	String getName();

	String getHostURL();

	String getContextRoot();

	List<Mission> getMissions(String typeId) throws MissionException;

	Mission getMission(String typeId, String name) throws MissionException;

	Mission createMission(String typeId, String name) throws MissionException;

	boolean deleteMission(String typeId, String name) throws MissionException;

	boolean startMission(String typeId, String name) throws MissionException;

	boolean stopMission(String typeId, String name) throws MissionException;

}
