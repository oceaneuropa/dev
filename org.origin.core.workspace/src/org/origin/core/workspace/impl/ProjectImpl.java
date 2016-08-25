package org.origin.core.workspace.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.origin.common.workingcopy.WorkingCopy;
import org.origin.common.workingcopy.WorkingCopyUtil;
import org.origin.core.workspace.IProject;
import org.origin.core.workspace.IProjectDescription;
import org.origin.core.workspace.IResource;
import org.origin.core.workspace.WorkspaceConstants;
import org.origin.core.workspace.nature.NatureRegistry;
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
	 */
	public static IProjectDescription loadProjectDescription(File projectDir) {
		IProjectDescription projectDesc = null;
		if (projectDir != null && projectDir.isDirectory()) {
			File projectDescFile = getProjectDescriptionFile(projectDir);
			if (projectDescFile != null && projectDescFile.exists()) {
				WorkingCopy<?> workingCopy = WorkingCopyUtil.getWorkingCopy(projectDescFile);
				if (workingCopy != null) {
					projectDesc = workingCopy.getRootElement(IProjectDescription.class);
				}
			}
		}
		return projectDesc;
	}

	protected IProjectDescription projectDesc;
	protected Map<String, ProjectNature> natureMap = new LinkedHashMap<String, ProjectNature>();

	public ProjectImpl() {
	}

	/**
	 * 
	 * @param file
	 */
	public ProjectImpl(File file) {
		super(file);
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
		this.projectDesc = loadProjectDescription(this.file);

		// project natures to load extended configurations from the project.
		if (this.projectDesc != null) {
			for (String natureId : this.projectDesc.getNatureIds()) {
				ProjectNature nature = getProjectNature(natureId);
				if (nature != null) {
					nature.load();
				}
			}
		}
	}

	@Override
	public void save() throws IOException {
		if (this.projectDesc != null) {
			// project natures to save extended configurations to the project.
			for (String natureId : this.projectDesc.getNatureIds()) {
				ProjectNature nature = getProjectNature(natureId);
				if (nature != null) {
					nature.save();
				}
			}

			WorkingCopy<?> workingCopy = WorkingCopyUtil.getWorkingCopy(this.projectDesc);
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

	/**
	 * Called by load() and save() methods to get/create ProjectNature by natureId.
	 * 
	 * @param natureId
	 * @return
	 */
	protected synchronized ProjectNature getProjectNature(String natureId) {
		ProjectNature nature = this.natureMap.get(natureId);
		if (nature == null || this != nature.getResource()) {
			nature = NatureRegistry.INSTANCE.createNature(natureId, ProjectNature.class);
			if (nature != null) {
				nature.setResource(this);
				this.natureMap.put(natureId, nature);
			}
		}
		return nature;
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
