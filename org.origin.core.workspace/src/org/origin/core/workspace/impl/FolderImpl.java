package org.origin.core.workspace.impl;

import java.io.File;

import org.origin.common.workingcopy.WorkingCopy;
import org.origin.common.workingcopy.WorkingCopyUtil;
import org.origin.core.workspace.IFolder;
import org.origin.core.workspace.IFolderDescription;
import org.origin.core.workspace.IResource;

public class FolderImpl extends ContainerImpl implements IFolder {

	protected IFolderDescription description;

	public FolderImpl() {
	}

	/**
	 * 
	 * @param file
	 */
	public FolderImpl(File file) {
		super(file);
	}

	@Override
	public int getType() {
		return IResource.FOLDER;
	}

	@Override
	public File getDescriptionFile() {
		return null;
	}

	@Override
	public void load() {
		File folderDescFile = new File(this.file, ".metadata" + File.pathSeparator + "." + this.file.getName() + ".json");
		if (folderDescFile.exists()) {

		}
	}

	@Override
	public void save() {

	}

	@Override
	public void create(IFolderDescription description) {
		this.description = description;
	}

	@Override
	public IFolderDescription getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(IFolderDescription description) {
		this.description = description;
	}

	/**
	 * 
	 * @param folderDir
	 * @return
	 */
	protected File getDescriptionFile(File folderDir) {
		return new File(folderDir, ".metadata" + File.pathSeparator + "." + this.file.getName() + ".folder.json");
	}

	/**
	 * 
	 * @param folderDir
	 * @return
	 */
	protected IFolderDescription loadFolderDescription(File folderDir) {
		IFolderDescription projectDesc = null;
		if (folderDir != null && folderDir.isDirectory()) {
			File folderDescriptionFile = getDescriptionFile(folderDir);
			if (folderDescriptionFile.exists()) {
				WorkingCopy<?> workingCopy = WorkingCopyUtil.getWorkingCopy(folderDescriptionFile);
				if (workingCopy != null) {
					projectDesc = workingCopy.getRootElement(IFolderDescription.class);
				}
			}
		}
		return projectDesc;
	}

}
