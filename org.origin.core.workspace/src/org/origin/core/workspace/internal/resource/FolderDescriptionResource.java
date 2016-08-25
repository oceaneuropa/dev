package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.origin.common.resource.AbstractResource;
import org.origin.core.workspace.IFolderDescription;

public class FolderDescriptionResource extends AbstractResource {

	/**
	 * 
	 * @param uri
	 */
	public FolderDescriptionResource(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream input) throws IOException {
		FolderDescriptionReader reader = new FolderDescriptionReader();
		reader.read(this, input);
	}

	@Override
	protected void doSave(OutputStream output) throws IOException {
		FolderDescriptionWriter writer = new FolderDescriptionWriter();
		writer.write(this, output);
	}

	public IFolderDescription getFolderDescription() {
		return !getContents().isEmpty() && getContents().get(0) instanceof IFolderDescription ? (IFolderDescription) getContents().get(0) : null;
	}

}