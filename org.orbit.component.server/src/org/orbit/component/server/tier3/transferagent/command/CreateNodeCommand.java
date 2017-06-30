package org.orbit.component.server.tier3.transferagent.command;

import org.orbit.component.model.tier3.transferagent.request.CreateNodeRequest;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;

public class CreateNodeCommand extends AbstractCommand {

	protected CreateNodeRequest request;

	/**
	 * 
	 * @param request
	 */
	public CreateNodeCommand(CreateNodeRequest request) {
		this.request = request;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		return null;
	}

}
