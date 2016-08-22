package org.origin.common.rest.agent;

import org.origin.common.command.CommandException;
import org.origin.common.command.ICommand;
import org.origin.common.command.ICommandResult;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public abstract class AbstractEditPolicy {

	protected String role;
	protected AbstractAgentResource resource;

	public AbstractEditPolicy() {
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

	public AbstractAgentResource getResource() {
		return resource;
	}

	public void setResource(AbstractAgentResource resource) {
		this.resource = resource;
	}

	public void activate() {

	}

	public void deactivate() {

	}

	/**
	 * Check whether a request can be understood by a EditPolicy implementation. If yes, getCommand(Request) will be called, which allows the
	 * EditPolicy implementation to return a ICommand. The ICommand, if available, will be executed to return a CommandResult.
	 * 
	 * @param request
	 * @return
	 */
	public abstract boolean understandsRequest(Request request);

	/**
	 * EditPolicy implementation to return a ICommand for a request.
	 * 
	 * @param request
	 * @return
	 */
	public abstract ICommand getCommand(Request request);

	/**
	 * 
	 * @param request
	 * @param responses
	 * @param commandResult
	 */
	public abstract void createResponse(Request request, Responses responses, ICommandResult commandResult);

	/**
	 * 
	 * @param request
	 * @param responses
	 * @param commandException
	 */
	public void createErrorResponse(Request request, Responses responses, CommandException commandException) {
		responses.set(request.getLabel(), new Response(Response.EXCEPTION, commandException.getMessage(), commandException));
	}

}
