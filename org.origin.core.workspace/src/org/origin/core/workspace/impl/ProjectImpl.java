package org.origin.core.workspace.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.origin.common.workingcopy.WorkingCopy;
import org.origin.common.workingcopy.WorkingCopyUtil;
import org.origin.core.workspace.IProject;
import org.origin.core.workspace.IProjectDescription;
import org.origin.core.workspace.IResource;
import org.origin.core.workspace.WorkspaceConstants;
import org.origin.core.workspace.nature.ProjectNature;

public class ProjectImpl extends ContainerImpl implements IProject {

	/**
	 * 
	 * @param projectDir
	 * @return
	 */
	public static File getProjectDescriptionFile(File projectDir) {
		return new File(projectDir, WorkspaceConstants.METADATA_FOLDER + File.pathSeparator + WorkspaceConstants.PROJECT_JSON);
	}

	/**
	 * 
	 * @param projectDir
	 * @return
	 * @throws IOException 
	 */
	public static IProjectDescription loadProjectDescription(File projectDir) throws IOException {
		IProjectDescription projectDesc = null;
		if (projectDir != null && projectDir.isDirectory()) {
			File projectDescFile = getProjectDescriptionFile(projectDir);
			if (projectDescFile != null && projectDescFile.exists()) {
				WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(projectDescFile);
				if (workingCopy != null) {
					projectDesc = workingCopy.getRootElement(IProjectDescription.class);
				}
			}
		}
		return projectDesc;
	}

	protected IProjectDescription projectDesc;

	/**
	 * 
	 * @param file
	 */
	public ProjectImpl(File file) {
		super(file);
	}

	/**
	 * 
	 * @param file
	 * @param projectDesc
	 */
	public ProjectImpl(File file, IProjectDescription projectDesc) {
		super(file);
		this.projectDesc = projectDesc;
	}

	@Override
	public int getType() {
		return IResource.PROJECT;
	}

	@Override
	public File getDescriptionFile() {
		return getProjectDescriptionFile(this.file);
	}

	@Override
	public void load() throws IOException {
		// load project description
		this.projectDesc = loadProjectDescription(this.file);

		// project natures to load extended configurations from the project.
		if (this.projectDesc != null) {
			for (String natureId : this.projectDesc.getNatureIds()) {
				ProjectNature nature = getNatureHandler().getNature(natureId, ProjectNature.class);
				if (nature != null) {
					try {
						nature.load();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void save() throws IOException {
		if (this.projectDesc != null) {
			// project natures to save extended configurations to the project.
			for (String natureId : this.projectDesc.getNatureIds()) {
				ProjectNature nature = getNatureHandler().getNature(natureId, ProjectNature.class);
				if (nature != null) {
					try {
						nature.save();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// save project description
			WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(this.projectDesc);
			if (workingCopy != null) {
				workingCopy.save();
			}

			// File projectDescFile = getProjectDescriptionFile(this.file);
			// if (projectDescFile != null && projectDescFile.exists()) {
			// WorkingCopy<?> workingCopy = WorkingCopyUtil.getWorkingCopy(projectDescFile);
			// if (workingCopy != null) {
			// workingCopy.save();
			// }
			// }
		}
	}

	@Override
	public IProject getProject() {
		return this;
	}

	@Override
	public void create(IProjectDescription description) {

	}

	@Override
	public void setDescription(IProjectDescription description) {

	}

	@Override
	public IProjectDescription getDescription() {
		return null;
	}

	@Override
	public void delete() {

	}

	public static void main(String[] args) {
		Map<File, String> map = new HashMap<File, String>();
		File file1 = new File("/Users/yayang/Downloads/abc.txt");
		String value1 = "abc_txt";
		map.put(file1, value1);

		File file2 = new File("/Users/yayang/Downloads/abc.txt");
		File file3 = new File("/Users/yayang/Downloads/abc.txt");

		String value2 = map.get(file2);
		String value3 = map.get(file3);

		System.out.println("value1 = " + value1);
		System.out.println("value2 = " + value2);
		System.out.println("value3 = " + value3);
	}

}
