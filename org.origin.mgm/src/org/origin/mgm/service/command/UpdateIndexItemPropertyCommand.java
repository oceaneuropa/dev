package org.origin.mgm.service.command;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;

public class UpdateIndexItemPropertyCommand extends AbstractCommand {

	public final static String COMMAND_NAME = "update_index_item";

	@Override
	public void execute(CommandContext context) throws CommandException {

	}

	@Override
	public void undo(CommandContext context) {

	}

}
