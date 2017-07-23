package org.orbit.component.server.tier3.transferagent.command;

import org.orbit.component.model.tier3.transferagent.request.DeleteNodeRequest;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;

public class DeleteNodeCommand extends AbstractCommand {

	protected DeleteNodeRequest request;

	/**
	 * 
	 * @param request
	 */
	public DeleteNodeCommand(DeleteNodeRequest request) {
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		return null;
	}

}