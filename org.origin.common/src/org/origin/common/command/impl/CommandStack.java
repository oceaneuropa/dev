package org.origin.common.command.impl;

import java.util.Stack;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommand;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.ICommandStack;

public class CommandStack implements ICommandStack {

	// Stack for commands that have been executed.
	protected Stack<ICommand> doneStack = new Stack<ICommand>();
	// Stack for commands that have been undo.
	protected Stack<ICommand> undoneStack = new Stack<ICommand>();

	protected ReadWriteLock stackRWLock = new ReentrantReadWriteLock();

	@Override
	public ICommandResult execute(CommandContext context, ICommand command) throws CommandException {
		ICommandResult commandResult = null;
		this.stackRWLock.writeLock().lock();
		try {
			commandResult = command.execute(context);
			command.setCommandResult(commandResult);

			this.doneStack.add(command);

			// once a new command is executed, existing undone commands should not be allowed to be redo again.
			this.undoneStack.clear();
		} finally {
			this.stackRWLock.writeLock().unlock();
		}
		return commandResult;
	}

	@Override
	public boolean canUndo() {
		this.stackRWLock.readLock().lock();
		try {
			return !this.doneStack.isEmpty() ? true : false;
		} finally {
			this.stackRWLock.readLock().unlock();
		}
	}

	@Override
	public int getUndoableSize() {
		this.stackRWLock.readLock().lock();
		try {
			return doneStack.size();
		} finally {
			this.stackRWLock.readLock().unlock();
		}
	}

	@Override
	public ICommand peekUndoCommand() {
		this.stackRWLock.readLock().lock();
		try {
			return !this.doneStack.isEmpty() ? this.doneStack.peek() : null;
		} finally {
			this.stackRWLock.readLock().unlock();
		}
	}

	@Override
	public ICommandResult undo(CommandContext context) throws CommandException {
		ICommandResult undoResult = null;
		this.stackRWLock.writeLock().lock();
		try {
			if (!this.doneStack.isEmpty()) {
				ICommand command = this.doneStack.pop();
				if (command != null) {
					undoResult = command.undo(context);
					command.setUndoResult(undoResult);

					this.undoneStack.push(command);
				}
			}
		} finally {
			this.stackRWLock.writeLock().unlock();
		}
		return undoResult;
	}

	@Override
	public boolean canRedo() {
		this.stackRWLock.readLock().lock();
		try {
			return !this.undoneStack.isEmpty() ? true : false;
		} finally {
			this.stackRWLock.readLock().unlock();
		}
	}

	@Override
	public int getRedoableSize() {
		this.stackRWLock.readLock().lock();
		try {
			return undoneStack.size();
		} finally {
			this.stackRWLock.readLock().unlock();
		}
	}

	@Override
	public ICommandResult redo(CommandContext context) throws CommandException {
		ICommandResult redoResult = null;
		this.stackRWLock.writeLock().lock();
		try {
			if (!this.undoneStack.isEmpty()) {
				ICommand command = this.undoneStack.pop();
				if (command != null) {
					redoResult = command.execute(context);
					command.setCommandResult(redoResult);

					this.doneStack.push(command);
				}
			}
		} finally {
			this.stackRWLock.writeLock().unlock();
		}
		return redoResult;
	}

	@Override
	public ICommand peekRedoCommand() {
		this.stackRWLock.readLock().lock();
		try {
			return !this.undoneStack.isEmpty() ? this.undoneStack.peek() : null;
		} finally {
			this.stackRWLock.readLock().unlock();
		}
	}

}
