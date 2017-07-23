package org.nb.home.command;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.io.FileUtil;
import org.origin.common.io.IOUtil;
import org.origin.common.runtime.IStatus;
import org.origin.common.runtime.Status;

public class FileCreateCommand extends AbstractCommand {

	public static String MSG1 = "File '%s' exists, but is a directory.";
	public static String MSG2 = "File '%s' already exists.";
	public static String MSG3 = "File '%s' is created.";
	public static String MSG4 = "Cannot create new file '%s'.";
	public static String MSG5 = "Cannot copy input stream to file '%s'.";
	public static String MSG6 = "Cannot copy bytes array to file '%s'.";

	protected File file;

	protected InputStream inputStream;
	protected boolean autoCloseInputStream = false;

	protected byte[] bytes;

	protected boolean throwExceptionWhenFileIsDirectory = true;
	protected boolean throwExceptionWhenFileExists = false;

	/**
	 * 
	 * @param filePath
	 */
	public FileCreateCommand(String filePath) {
		assert (filePath != null) : "filePath is null";
		this.file = new File(filePath);
	}

	/**
	 * 
	 * @param file
	 */
	public FileCreateCommand(File file) {
		assert (file != null) : "file is null";
		this.file = file;
	}

	public boolean throwExceptionWhenFileIsDirectory() {
		return throwExceptionWhenFileIsDirectory;
	}

	public void setThrowExceptionWhenFileIsDirectory(boolean throwExceptionWhenFileIsDirectory) {
		this.throwExceptionWhenFileIsDirectory = throwExceptionWhenFileIsDirectory;
	}

	public boolean throwExceptionWhenFileExists() {
		return throwExceptionWhenFileExists;
	}

	public void setThrowExceptionWhenFileExists(boolean throwExceptionWhenFileExists) {
		this.throwExceptionWhenFileExists = throwExceptionWhenFileExists;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public boolean isAutoCloseInputStream() {
		return autoCloseInputStream;
	}

	public void setAutoCloseInputStream(boolean autoCloseInputStream) {
		this.autoCloseInputStream = autoCloseInputStream;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		String filePath = this.file.getAbsolutePath();

		// File exists and is a directory
		if (this.file.exists() && this.file.isDirectory()) {
			if (throwExceptionWhenFileIsDirectory()) {
				throw new CommandException(String.format(MSG1, filePath));
			}
			// Ignore creating new file
			return new CommandResult(new Status(IStatus.WARNING, null, String.format(MSG1, filePath)));
		}

		// File exists
		if (this.file.exists()) {
			if (throwExceptionWhenFileExists()) {
				return new CommandResult(new Status(IStatus.ERROR, null, String.format(MSG2, filePath)));
			}
			// Ignore creating new file
			return new CommandResult(new Status(IStatus.OK, null, String.format(MSG2, filePath)));
		}

		boolean succeed = false;
		try {
			succeed = this.file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			// File is not created
			throw new CommandException(String.format(MSG4, filePath), e);
		}
		if (!succeed) {
			// File is not created
			throw new CommandException(String.format(MSG4, filePath));
		}

		if (this.inputStream != null) {
			// Copy input stream to the new file
			try {
				FileUtil.copyInputStreamToFile(this.inputStream, this.file);
			} catch (IOException e) {
				e.printStackTrace();
				// Cannot copy input stream to file
				throw new CommandException(String.format(MSG5, filePath), e);
			} finally {
				if (isAutoCloseInputStream()) {
					IOUtil.closeQuietly(this.inputStream, true);
				}
			}

		} else if (this.bytes != null && this.bytes.length > 0) {
			// Copy bytes array input stream to the new file
			ByteArrayInputStream bytesInputStream = new ByteArrayInputStream(bytes);
			try {
				FileUtil.copyInputStreamToFile(bytesInputStream, this.file);
			} catch (IOException e) {
				e.printStackTrace();
				// Cannot copy bytes array to file
				throw new CommandException(String.format(MSG6, filePath), e);
			} finally {
				IOUtil.closeQuietly(bytesInputStream, true);
			}
		}

		return new CommandResult(new Status(IStatus.OK, null, String.format(MSG3, filePath)));
	}

}
