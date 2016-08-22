package org.nb.home.command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.nb.home.model.resource.ProjectResourceFactory;
import org.nb.home.model.resource.ProjectWorkingCopy;
import org.nb.home.model.runtime.config.ProjectConfig;
import org.nb.home.model.util.HomeSetupUtil;
import org.nb.home.model.util.ProjectBuilder;
import org.nb.home.model.util.ProjectPathHelper;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.runtime.IStatus;
import org.origin.common.runtime.Status;
import org.origin.common.workingcopy.WorkingCopyUtil;

public class ProjectCreateCommand extends AbstractCommand {

	public static String MSG1 = "Workspace file '%s' exists, but is not a directory.";
	public static String MSG2 = "Project file '%s' exists, but is not a directory.";
	public static String MSG3 = "Cannot create project configuration file '%s'.";
	public static String MSG4 = "Project configuration root cannot be retrieved.";
	public static String MSG5 = "Project configuration file cache cannot be found.";
	public static String MSG6 = "Project configuration file cannot be saved.";
	public static String MSG7 = "Project '%s' is created.";

	protected Path homePath;
	protected String projectId;

	/**
	 * 
	 * @param homePath
	 * @param projectId
	 */
	public ProjectCreateCommand(Path homePath, String projectId) {
		assert (homePath != null) : "homePath is null";
		assert (projectId != null) : "projectId is null";

		this.homePath = homePath;
		this.projectId = projectId;
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		// 1. Create workspace directory if not exist
		File workspaceDirectory = HomeSetupUtil.getWorkspacesPath(this.homePath).toFile();
		if (!workspaceDirectory.exists()) {
			workspaceDirectory.mkdirs();
		}

		// 2. Create project directory if not exist
		File projectDirectory = new File(workspaceDirectory, this.projectId);
		if (!projectDirectory.exists()) {
			projectDirectory.mkdirs();
		}

		// 3. Create project config file if not exists
		File projectConfigFile = ProjectPathHelper.INSTANCE.getProjectConfigFile(this.homePath, this.projectId);
		boolean createNewFile = false;

		String filePath = projectConfigFile.getAbsolutePath();
		if (!projectConfigFile.exists()) {
			try {
				ProjectBuilder.INSTANCE.createNewProjectConfigFile(projectConfigFile, this.projectId);
			} catch (IOException e) {
				e.printStackTrace();
				throw new CommandException(String.format(MSG3, filePath));
			}
		}
		if (!projectConfigFile.exists()) {
			throw new CommandException(String.format(MSG3, filePath));
		}

//		// 4. Reset project config file if corrupted
//		ProjectConfig project = ProjectPathHelper.INSTANCE.getProjectDirectory(homePath, filePath);
//		
//		ProjectWorkingCopy wc = WorkingCopyUtil.getWorkingCopy(projectConfigFile, ProjectResourceFactory.class, ProjectWorkingCopy.class);
//		if (wc != null) {
//			project = wc.getRootElement();
//		}
//		if (project == null) {
//			try {
//				ProjectBuilder.INSTANCE.createNewProjectConfigFile(projectConfigFile, this.projectId);
//			} catch (IOException e) {
//				e.printStackTrace();
//				throw new CommandException(String.format(MSG3, filePath));
//			}
//			project = wc.getRootElement();
//			if (project == null) {
//				throw new CommandException(String.format(MSG4, filePath));
//			}
//		}
//
//		// 5. Update ProjectConfig with projectId
//		boolean doSave = false;
//		ProjectConfig projectConfig = projectRoot.getProject();
//		if (projectConfig == null) {
//			// Create ProjectConfig if not exists
//			projectConfig = new ProjectConfig();
//			projectConfig.setProjectId(this.projectId);
//			projectRoot.setProject(projectConfig);
//			doSave = true;
//
//		} else {
//			// Set projectId to ProjectConfig
//			// Note:
//			// - It is not this class's responsibility to check the existing projectId. This class executes what it need to do.
//			// - The client of this command should check whether the the existing projectId has a different value and confirm with users for what to
//			// do (e.g. Project folder exists but is for another project. 1-Don't create new project. 2-Create new project and update project config
//			// file.) and then decide whether to execute this command.
//			String existingProjectId = projectConfig.getProjectId();
//			if (existingProjectId == null || !existingProjectId.equals(this.projectId)) {
//				projectConfig.setProjectId(this.projectId);
//				doSave = true;
//			}
//		}
//
//		// 6. Save changes to the project config file
//		if (doSave) {
//			ProjectConfigFileCache projectConfigFileCache = WorkingCopyUtil.getWorkingCopy(projectConfigFile, ProjectConfigFileCacheFactory.class, ProjectConfigFileCache.class);
//			if (projectConfigFileCache == null) {
//				throw new CommandException(String.format(MSG5, filePath));
//			}
//			try {
//				projectConfigFileCache.save();
//			} catch (IOException e) {
//				e.printStackTrace();
//				throw new CommandException(String.format(MSG6, filePath), e);
//			}
//		}

		return new CommandResult(this, new Status(IStatus.OK, null, String.format(MSG7, this.projectId)));
	}

}
