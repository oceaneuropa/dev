package org.nb.home.command;

import java.io.File;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.runtime.IStatus;
import org.origin.common.runtime.Status;

public class FileDeleteCommand extends AbstractCommand {

	public static String MSG1 = "File '%s' exists, but is a directory.";
	public static String MSG2 = "File '%s' doesn't exist.";
	public static String MSG3 = "File '%s' is deleted.";
	public static String MSG4 = "File '%s' is not deleted.";

	protected File file;

	protected boolean throwExceptionWhenFileIsDirectory = true;
	protected boolean throwExceptionWhenNotExists = false;

	/**
	 * 
	 * @param filePath
	 */
	public FileDeleteCommand(String filePath) {
		assert (filePath != null) : "filePath is null";
		this.file = new File(filePath);
	}

	/**
	 * 
	 * @param file
	 */
	public FileDeleteCommand(File file) {
		assert (file != null) : "file is null";
		this.file = file;
	}

	public boolean throwExceptionWhenFileIsDirectory() {
		return throwExceptionWhenFileIsDirectory;
	}

	public void setThrowExceptionWhenFileIsDirectory(boolean throwExceptionWhenFileIsDirectory) {
		this.throwExceptionWhenFileIsDirectory = throwExceptionWhenFileIsDirectory;
	}

	public boolean throwExceptionWhenNotExists() {
		return throwExceptionWhenNotExists;
	}

	public void setThrowExceptionWhenNotExists(boolean throwExceptionWhenNotExists) {
		this.throwExceptionWhenNotExists = throwExceptionWhenNotExists;
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		String filePath = this.file.getAbsolutePath();

		// File exists and is a directory
		if (this.file.exists() && this.file.isDirectory()) {
			if (throwExceptionWhenFileIsDirectory()) {
				throw new CommandException(String.format(MSG1, filePath));
			}
			// Ignore deleting file
			return new CommandResult(new Status(IStatus.WARNING, null, String.format(MSG1, filePath)));
		}

		// File exists
		if (!this.file.exists()) {
			if (throwExceptionWhenNotExists()) {
				throw new CommandException(String.format(MSG2, filePath));
			}
			// Ignore deleting file
			return new CommandResult(new Status(IStatus.OK, null, String.format(MSG2, filePath)));
		}

		boolean succeed = this.file.delete();
		if (!succeed) {
			// File is not deleted
			throw new CommandException(String.format(MSG4, filePath));
		}

		return new CommandResult(new Status(IStatus.OK, null, String.format(MSG3, filePath)));
	}

}
