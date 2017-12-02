package org.origin.common.rest.editpolicy;

import org.origin.common.command.ICommand;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.AbstractWSApplicationResource;

public abstract class AbstractWSEditPolicyV1 {

	protected String role;
	protected AbstractWSApplicationResource resource;

	public AbstractWSEditPolicyV1() {
	}

	public String getRole() {
		if (role == null) {
			return getClass().getName();
		}
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public AbstractWSApplicationResource getResource() {
		return this.resource;
	}

	public void setResource(AbstractWSApplicationResource resource) {
		this.resource = resource;
	}

	public <T> T getService(Class<T> serviceClass) {
		if (this.resource != null) {
			return this.resource.getService(serviceClass);
		}
		return null;
	}

	public void activate() {

	}

	public void deactivate() {

	}

	/**
	 * EditPolicy implementation to return a ICommand for a request.
	 * 
	 * @param request
	 * @return
	 */
	public abstract ICommand getCommand(EditpolicyWSApplicationResource resource, Request request);

}

/**
 * 
 * @param request
 * @param responses
 * @param commandResult
 */
// public abstract void createResponse(Request request, Responses responses, ICommandResult commandResult);