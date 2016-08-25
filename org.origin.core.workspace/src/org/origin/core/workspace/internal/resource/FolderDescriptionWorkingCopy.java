package org.origin.core.workspace.internal.resource;

import java.io.File;

import org.origin.common.workingcopy.AbstractWorkingCopy;
import org.origin.core.workspace.IFolderDescription;

public class FolderDescriptionWorkingCopy extends AbstractWorkingCopy<FolderDescriptionResource, IFolderDescription> {

	/**
	 * 
	 * @param file
	 */
	public FolderDescriptionWorkingCopy(File file) {
		super(file);
	}

	@Override
	protected IFolderDescription getRootElement(FolderDescriptionResource resource) {
		return resource.getFolderDescription();
	}

}
