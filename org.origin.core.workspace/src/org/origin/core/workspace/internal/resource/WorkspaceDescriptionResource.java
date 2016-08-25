package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.origin.common.resource.AbstractResource;
import org.origin.core.workspace.IWorkspaceDescription;

public class WorkspaceDescriptionResource extends AbstractResource {

	/**
	 * 
	 * @param uri
	 */
	public WorkspaceDescriptionResource(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream input) throws IOException {
		WorkspaceDescriptionReader reader = new WorkspaceDescriptionReader();
		reader.read(this, input);
	}

	@Override
	protected void doSave(OutputStream output) throws IOException {
		WorkspaceDescriptionWriter writer = new WorkspaceDescriptionWriter();
		writer.write(this, output);
	}

	public IWorkspaceDescription getWorkspaceDescription() {
		return !getContents().isEmpty() && getContents().get(0) instanceof IWorkspaceDescription ? (IWorkspaceDescription) getContents().get(0) : null;
	}

}
