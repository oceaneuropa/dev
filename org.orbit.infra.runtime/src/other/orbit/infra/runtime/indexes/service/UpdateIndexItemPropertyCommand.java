package other.orbit.infra.runtime.indexes.service;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.impl.CommandResultImpl;
import org.origin.common.runtime.Status;

public class UpdateIndexItemPropertyCommand extends AbstractCommand {

	public final static String COMMAND_NAME = "update_index_item";

	@Override
	public CommandResultImpl execute(CommandContext context) throws CommandException {
		return new CommandResultImpl(Status.OK_STATUS);
	}

	@Override
	public CommandResultImpl undo(CommandContext context) {
		return new CommandResultImpl(Status.OK_STATUS);
	}

}
