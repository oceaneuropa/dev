package org.origin.common.command.impl;

import java.util.Stack;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.CommandStack;

public class CommandStackImpl implements CommandStack {

	// Stack for commands that have been executed.
	protected Stack<AbstractCommand> doneStack = new Stack<AbstractCommand>();
	// Stack for commands that have been undo.
	protected Stack<AbstractCommand> undoneStack = new Stack<AbstractCommand>();

	protected ReadWriteLock stackRWLock = new ReentrantReadWriteLock();

	@Override
	public void execute(CommandContext context, AbstractCommand command) throws CommandException {
		this.stackRWLock.writeLock().lock();
		try {
			command.execute(context);
			this.doneStack.add(command);

			// once a new command is executed, existing undone commands should not be allowed to be redo again.
			this.undoneStack.clear();
		} finally {
			this.stackRWLock.writeLock().unlock();
		}
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
	public AbstractCommand peekUndoCommand() {
		this.stackRWLock.readLock().lock();
		try {
			return !this.doneStack.isEmpty() ? this.doneStack.peek() : null;
		} finally {
			this.stackRWLock.readLock().unlock();
		}
	}

	@Override
	public void undo(CommandContext context) throws CommandException {
		this.stackRWLock.writeLock().lock();
		try {
			if (!this.doneStack.isEmpty()) {
				AbstractCommand command = this.doneStack.pop();
				if (command != null) {
					command.undo(context);
					this.undoneStack.push(command);
				}
			}
		} finally {
			this.stackRWLock.writeLock().unlock();
		}
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
	public void redo(CommandContext context) throws CommandException {
		this.stackRWLock.writeLock().lock();
		try {
			if (!this.undoneStack.isEmpty()) {
				AbstractCommand command = this.undoneStack.pop();
				if (command != null) {
					command.execute(context);
					this.doneStack.push(command);
				}
			}
		} finally {
			this.stackRWLock.writeLock().unlock();
		}
	}

	@Override
	public AbstractCommand peekRedoCommand() {
		this.stackRWLock.readLock().lock();
		try {
			return !this.undoneStack.isEmpty() ? this.undoneStack.peek() : null;
		} finally {
			this.stackRWLock.readLock().unlock();
		}
	}

}
