package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.net.URI;

import org.origin.common.workingcopy.AbstractWorkingCopy;
import org.origin.core.workspace.IFolderDescription;

public class FolderDescriptionWorkingCopy extends AbstractWorkingCopy<FolderDescriptionResource> {

	/**
	 * 
	 * @param uri
	 */
	public FolderDescriptionWorkingCopy(URI uri) {
		super(uri);
	}

	@Override
	protected <T> T getRootElement(FolderDescriptionResource resource, Class<T> elementClass) throws IOException {
		if (IFolderDescription.class.isAssignableFrom(elementClass)) {
			return (T) resource.getFolderDescription();
		}
		return super.getRootElement(elementClass);
	}

}
