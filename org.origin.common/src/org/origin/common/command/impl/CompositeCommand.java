package org.origin.common.command.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommand;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.ICompositeCommand;

public class CompositeCommand extends AbstractCommand implements ICompositeCommand {

	protected List<ICommand> commands = new ArrayList<ICommand>();

	@Override
	public boolean isCompositeCommand() {
		return true;
	}

	@Override
	public void add(ICommand command) {
		if (command != null && !commands.contains(command)) {
			this.commands.add(command);
		}
	}

	@Override
	public void remove(ICommand command) {
		if (command != null && commands.contains(command)) {
			this.commands.remove(command);
		}
	}

	@Override
	public boolean isEmpty() {
		return this.commands.isEmpty();
	}

	@Override
	public int size() {
		return this.commands.size();
	}

	@Override
	public ICommand[] getChildren() {
		return this.commands.toArray(new ICommand[this.commands.size()]);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		CompositeCommandResult compositeCommandResult = new CompositeCommandResult();

		for (Iterator<ICommand> commandItor = this.commands.iterator(); commandItor.hasNext();) {
			ICommand command = commandItor.next();

			ICommandResult currCommandResult = command.execute(context);
			command.setCommandResult(currCommandResult);

			if (currCommandResult != null) {
				compositeCommandResult.add(currCommandResult);
			}
		}

		return compositeCommandResult;
	}

	@Override
	public CommandResult undo(CommandContext context) throws CommandException {
		CompositeCommandResult compositeCommandResult = new CompositeCommandResult();

		for (Iterator<ICommand> commandItor = this.commands.iterator(); commandItor.hasNext();) {
			ICommand command = commandItor.next();

			ICommandResult currCommandResult = command.undo(context);
			command.setUndoResult(currCommandResult);

			if (currCommandResult != null) {
				compositeCommandResult.add(currCommandResult);
			}
		}

		return compositeCommandResult;
	}

}
