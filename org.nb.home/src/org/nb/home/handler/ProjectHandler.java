package org.nb.home.handler;

import java.util.Map;

import org.nb.home.model.exception.HomeException;
import org.nb.home.service.HomeAgentService;

public class ProjectHandler {

	protected HomeAgentService service;
	protected Map<Object, Object> props;

	/**
	 * 
	 * @param service
	 * @param props
	 */
	public ProjectHandler(HomeAgentService service, Map<Object, Object> props) {
		this.service = service;
		this.props = props;
	}

	/**
	 * 
	 * @param projectId
	 * @return
	 */
	public boolean projectExists(String projectId) throws HomeException {
		return false;
	}

	/**
	 * 
	 * @param projectId
	 * @return
	 */
	public boolean createProject(String projectId) throws HomeException {
		return false;
	}

	/**
	 * 
	 * @param projectId
	 * @return
	 */
	public boolean deleteProject(String projectId) throws HomeException {
		return false;
	}

}
