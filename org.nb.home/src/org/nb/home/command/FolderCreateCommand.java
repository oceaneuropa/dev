package org.nb.home.command;

import java.io.File;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.runtime.IStatus;
import org.origin.common.runtime.Status;

public class FolderCreateCommand extends AbstractCommand {

	public static String MSG1 = "Directory '%s' exists, but is a file.";
	public static String MSG2 = "Directory '%s' already exists.";
	public static String MSG3 = "Directory '%s' is created.";
	public static String MSG4 = "Directory '%s' is not created.";

	protected File directory;

	protected boolean throwExceptionWhenDirectoryIsFile = true;
	protected boolean throwExceptionWhenDirectoryExists = false;

	/**
	 * 
	 * @param directoryPath
	 */
	public FolderCreateCommand(String directoryPath) {
		assert (directoryPath != null) : "directoryPath is null";
		this.directory = new File(directoryPath);
	}

	/**
	 * 
	 * @param directory
	 */
	public FolderCreateCommand(File directory) {
		assert (directory != null) : "directory is null";
		this.directory = directory;
	}

	public boolean throwExceptionWhenDirectoryIsFile() {
		return throwExceptionWhenDirectoryIsFile;
	}

	public void setThrowExceptionWhenDirectoryIsFile(boolean throwExceptionWhenDirectoryIsFile) {
		this.throwExceptionWhenDirectoryIsFile = throwExceptionWhenDirectoryIsFile;
	}

	public boolean throwExceptionWhenDirectoryExists() {
		return throwExceptionWhenDirectoryExists;
	}

	public void setThrowExceptionWhenDirectoryExists(boolean throwExceptionWhenDirectoryExists) {
		this.throwExceptionWhenDirectoryExists = throwExceptionWhenDirectoryExists;
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		String directoryPath = this.directory.getAbsolutePath();

		// Directory exists but is file
		if (this.directory.exists() && this.directory.isFile()) {
			if (throwExceptionWhenDirectoryIsFile()) {
				throw new CommandException(String.format(MSG1, directoryPath));
			}
			// Ignore creating directory
			return new CommandResult(this, new Status(IStatus.WARNING, null, String.format(MSG1, directoryPath)));
		}

		// Directory exists
		if (this.directory.exists()) {
			if (throwExceptionWhenDirectoryExists()) {
				throw new CommandException(String.format(MSG2, directoryPath));
			}
			// Ignore creating directory
			return new CommandResult(this, new Status(IStatus.OK, null, String.format(MSG2, directoryPath)));
		}

		boolean succeed = this.directory.mkdirs();
		if (!succeed) {
			// Directory is not created
			throw new CommandException(String.format(MSG4, directoryPath));
		}

		return new CommandResult(this, new Status(IStatus.OK, null, String.format(MSG3, directoryPath)));
	}

}
