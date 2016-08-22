package org.nb.home.command;

import java.io.File;
import java.nio.file.Path;

import org.nb.home.model.util.HomeSetupUtil;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.runtime.IStatus;
import org.origin.common.runtime.Status;

public class ProjectDeleteCommand extends AbstractCommand {

	public static String MSG1 = "Workspace directory doesn't exists.";
	public static String MSG2 = "Project '%s' doesn't exists.";
	public static String MSG3 = "Project '%s' is not a directory.";
	public static String MSG4 = "Project '%s' is not deleted.";
	public static String MSG5 = "Project '%s' is deleted.";

	protected Path homePath;
	protected String projectId;

	/**
	 * 
	 * @param homePath
	 * @param projectId
	 */
	public ProjectDeleteCommand(Path homePath, String projectId) {
		assert (homePath != null) : "homePath is null";
		assert (projectId != null) : "projectId is null";

		this.homePath = homePath;
		this.projectId = projectId;
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		// 1. Check whether workspace directory exists
		File workspaceDirectory = HomeSetupUtil.getWorkspacesPath(this.homePath).toFile();
		if (!workspaceDirectory.exists()) {
			throw new CommandException(MSG1);
		}

		// 2. Check whether project directory exists
		File projectDirectory = new File(workspaceDirectory, this.projectId);
		if (!projectDirectory.exists()) {
			throw new CommandException(String.format(MSG2, this.projectId));
		}

		// 3. Check whether project file is directory
		if (!projectDirectory.isDirectory()) {
			throw new CommandException(String.format(MSG3, this.projectId));
		}

		// 4. Delete project directory
		try {
			projectDirectory.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(String.format(MSG4, this.projectId), e);
		}

		return new CommandResult(this, new Status(IStatus.OK, null, String.format(MSG5, this.projectId)));
	}

}
