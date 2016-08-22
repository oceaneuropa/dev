package org.origin.mgm.service.command;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.runtime.Status;

public class UpdateIndexItemPropertyCommand extends AbstractCommand {

	public final static String COMMAND_NAME = "update_index_item";

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		return new CommandResult(this, Status.OK_STATUS);
	}

	@Override
	public CommandResult undo(CommandContext context) {
		return new CommandResult(this, Status.OK_STATUS);
	}

}
