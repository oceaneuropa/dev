package org.nb.home.command;

import org.nb.home.model.exception.HomeException;
import org.nb.home.service.HomeAgentService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.runtime.Status;

public class PingCommand extends AbstractCommand {

	protected HomeAgentService homeService;

	/**
	 * 
	 * @param homeService
	 */
	public PingCommand(HomeAgentService homeService) {
		this.homeService = homeService;
	}

	@Override
	public ICommandResult execute(CommandContext context) throws CommandException {
		int pingResult = -1;
		try {
			pingResult = this.homeService.ping();
			System.out.println("PingCommand.execute() pingResult = " + pingResult);
			return new CommandResult(Status.OK_STATUS, pingResult);

		} catch (HomeException e) {
			e.printStackTrace();
			throw new CommandException(e);
		}
	}

}
