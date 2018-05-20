package org.orbit.component.runtime.tier4.missioncontrol.service;

import java.io.IOException;
import java.util.List;

import org.orbit.component.model.tier4.mission.MissionRTO;

public interface MissionPersistenceHandler {

	/**
	 * Get a list of missions.
	 * 
	 * @param typeId
	 * @return
	 * @throws IOException
	 */
	List<MissionRTO> getMissions(String typeId) throws IOException;

	/**
	 * Get a list of mission names.
	 * 
	 * @param typeId
	 * @return
	 * @throws IOException
	 */
	List<String> getMissionNames(String typeId) throws IOException;

	/**
	 * Get a mission by id.
	 * 
	 * @param typeId
	 * @param id
	 * @return
	 * @throws IOException
	 */
	MissionRTO getMission(String typeId, Integer id) throws IOException;

	/**
	 * Get a mission by name.
	 * 
	 * @param typeId
	 * @param name
	 * @return
	 * @throws IOException
	 */
	MissionRTO getMission(String typeId, String name) throws IOException;

	/**
	 * Check whether mission name exists.
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	boolean nameExists(String typeId, String name) throws IOException;

	/**
	 * Insert an mission.
	 * 
	 * @param typeId
	 * @param name
	 * @return
	 * @throws IOException
	 */
	MissionRTO insert(String typeId, String name) throws IOException;

	/**
	 * Delete a mission by id.
	 * 
	 * @param typeId
	 * @param id
	 * @return
	 * @throws IOException
	 */
	boolean delete(String typeId, Integer id) throws IOException;

	/**
	 * Delete a mission by name.
	 * 
	 * @param typeId
	 * @param name
	 * @return
	 * @throws IOException
	 */
	boolean delete(String typeId, String name) throws IOException;

}